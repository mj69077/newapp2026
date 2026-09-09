package com.example.data.ai

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

data class ChatbotFatwaEntry(
    val id: Int,
    val question: String,
    val answer: String,
    val source: String = "مركز الفتوى - إسلام ويب (Arabic-Fatwa-ChatBot)"
)

data class ChatbotMatchResult(
    val entry: ChatbotFatwaEntry,
    val confidence: Float,
    val matchedKeywords: List<String>
)

object ArabicFatwaChatbotEngine {

    private var corpus: List<ChatbotFatwaEntry> = emptyList()
    private var normalizedCorpus: List<Pair<ChatbotFatwaEntry, Set<String>>> = emptyList()
    private var isInitialized = false

    // Arabic stop words to filter out during semantic token matching
    private val arabicStopWords = setOf(
        "هل", "ما", "ماذا", "حكم", "عن", "في", "من", "الى", "إلى", "على", "هو", "هي",
        "مع", "هذا", "هذه", "ذلك", "تلك", "ان", "إن", "أن", "كان", "كانت", "يكون",
        "اريد", "أريد", "اسال", "أسأل", "سؤال", "سؤالي", "ارجو", "أرجو", "يا", "شيخ",
        "السلام", "عليكم", "ورحمة", "الله", "وبركاته", "شكرا", "جزاكم", "خيرا", "لو",
        "سمحت", "مسالة", "مسألة", "بيان", "توضيح", "كيف", "متى", "اين", "أين"
    )

    /**
     * Initialize the knowledge base from assets/fatwa_chatbot_corpus.json
     */
    suspend fun initialize(context: Context) = withContext(Dispatchers.IO) {
        if (isInitialized && corpus.isNotEmpty()) return@withContext
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open("fatwa_chatbot_corpus.json")
            val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
            val jsonString = reader.use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val list = ArrayList<ChatbotFatwaEntry>(jsonArray.length())

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optInt("id", i + 1)
                val q = obj.optString("question", "")
                val a = obj.optString("answer", "")
                val s = obj.optString("source", "مركز الفتوى - إسلام ويب")
                if (q.isNotBlank() && a.isNotBlank()) {
                    list.add(ChatbotFatwaEntry(id, q, a, s))
                }
            }

            corpus = list
            normalizedCorpus = list.map { entry ->
                entry to tokenize(entry.question)
            }
            isInitialized = true
            Log.d("FatwaChatbot", "Loaded ${corpus.size} fatwa Q&A pairs from Arabic-Fatwa-ChatBot corpus.")
        } catch (e: Exception) {
            Log.e("FatwaChatbot", "Error loading fatwa chatbot corpus: ${e.message}")
        }
    }

    /**
     * Remove Arabic diacritics (tashkeel)
     */
    fun removeTashkeel(text: String): String {
        return text.replace(Regex("[\\u064B-\\u0652\\u0670\\u0640]"), "")
    }

    /**
     * Normalize Arabic characters (Alef, Yaa, Taa Marbuta)
     */
    fun normalizeArabic(text: String): String {
        var res = removeTashkeel(text)
        // Normalize Alef variants
        res = res.replace(Regex("[إأآٱ]"), "ا")
        // Normalize Taa Marbuta
        res = res.replace("ة", "ه")
        // Normalize Yaa / Alef Maqsura
        res = res.replace("ى", "ي")
        // Remove punctuation and special symbols
        res = res.replace(Regex("[؟!?.,،:;\"'()\\[\\]{}\\-_/\\\\<>@#$%^&*+=~`]"), " ")
        // Normalize multiple whitespaces
        return res.replace(Regex("\\s+"), " ").trim()
    }

    /**
     * Tokenize text into meaningful content words
     */
    fun tokenize(text: String): Set<String> {
        val normalized = normalizeArabic(text)
        return normalized.split(" ")
            .map { it.trim() }
            .filter { it.length > 1 && !arabicStopWords.contains(it) }
            .toSet()
    }

    /**
     * BestMatch Q&A retrieval algorithm
     */
    suspend fun findBestAnswer(userQuery: String): ChatbotMatchResult? = withContext(Dispatchers.Default) {
        if (normalizedCorpus.isEmpty()) return@withContext null

        val queryNormalized = normalizeArabic(userQuery)
        val queryTokens = tokenize(userQuery)

        if (queryTokens.isEmpty() && queryNormalized.length < 3) return@withContext null

        var bestEntry: ChatbotFatwaEntry? = null
        var bestScore = 0f
        var bestMatchedKeywords = emptyList<String>()

        for ((entry, questionTokens) in normalizedCorpus) {
            var score = 0f
            val matched = mutableListOf<String>()

            // 1. Exact or substring match in normalized question
            val normQuestion = normalizeArabic(entry.question)
            if (normQuestion.contains(queryNormalized) || queryNormalized.contains(normQuestion)) {
                score += 0.6f
            }

            // 2. Token overlap (Jaccard-like score)
            if (questionTokens.isNotEmpty() && queryTokens.isNotEmpty()) {
                val intersection = queryTokens.intersect(questionTokens)
                if (intersection.isNotEmpty()) {
                    val overlapScore = intersection.size.toFloat() / (queryTokens.size.toFloat() + questionTokens.size.toFloat() - intersection.size.toFloat())
                    score += overlapScore * 0.7f
                    matched.addAll(intersection)
                }
            }

            // 3. Answer keyword match bonus
            val normAnswer = normalizeArabic(entry.answer)
            var answerHitCount = 0
            for (token in queryTokens) {
                if (normAnswer.contains(token)) {
                    answerHitCount++
                    if (!matched.contains(token)) matched.add(token)
                }
            }
            if (queryTokens.isNotEmpty()) {
                score += (answerHitCount.toFloat() / queryTokens.size.toFloat()) * 0.25f
            }

            if (score > bestScore) {
                bestScore = score
                bestEntry = entry
                bestMatchedKeywords = matched
            }
        }

        if (bestEntry != null && bestScore >= 0.18f) {
            ChatbotMatchResult(bestEntry, bestScore, bestMatchedKeywords)
        } else {
            null
        }
    }

    /**
     * Suggested questions for quick prompts
     */
    fun getFeaturedQuestions(): List<String> {
        return listOf(
            "ما حكم فتح حساب ادخار في بنك إسلامي؟",
            "كيفية ودعاء صلاة الاستخارة",
            "أحكام المسح على الجوارب في الوضوء",
            "حكم بيع الذهب بالتقسيط",
            "ما حكم التسويق بالعمولة؟",
            "حكم قضاء الصلوات الفائتة وكيفيتها",
            "هل يجوز قراءة القرآن للحائض؟",
            "أحكام سجود السهو ومتى يكون قبلياً أو بعدياً"
        )
    }
}
