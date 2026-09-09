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
    val source: String = "مركز الفتوى - إسلام ويب",
    val keywords: List<String> = emptyList()
)

data class ChatbotMatchResult(
    val entry: ChatbotFatwaEntry,
    val confidence: Float,
    val matchedKeywords: List<String>
)

object ArabicFatwaChatbotEngine {

    private var corpus: List<ChatbotFatwaEntry> = emptyList()
    private var isInitialized = false

    // Arabic stop words to filter out during semantic token matching
    private val arabicStopWords = setOf(
        "هل", "ما", "ماذا", "حكم", "عن", "في", "من", "الى", "إلى", "على", "هو", "هي",
        "مع", "هذا", "هذه", "ذلك", "تلك", "ان", "إن", "أن", "كان", "كانت", "يكون",
        "اريد", "أريد", "اسال", "أسأل", "سؤال", "سؤالي", "ارجو", "أرجو", "يا", "شيخ",
        "لو", "سمحت", "مسالة", "مسألة", "بيان", "توضيح", "كيف", "متى", "اين", "أين",
        "يجوز", "ماحكم", "حكمه", "حكمها"
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
                val kwList = mutableListOf<String>()
                val kwArr = obj.optJSONArray("keywords")
                if (kwArr != null) {
                    for (k in 0 until kwArr.length()) {
                        kwList.add(kwArr.getString(k))
                    }
                }
                if (q.isNotBlank() && a.isNotBlank()) {
                    list.add(ChatbotFatwaEntry(id, q, a, s, kwList))
                }
            }

            corpus = list
            isInitialized = true
            Log.d("FatwaChatbot", "Loaded ${corpus.size} clean curated fatwas.")
        } catch (e: Exception) {
            Log.e("FatwaChatbot", "Error loading fatwa chatbot corpus: ${e.message}")
        }
    }

    /**
     * Remove Arabic diacritics (tashkeel)
     */
    fun removeTashkeel(text: String): String {
        return text.replace(Regex("[\\u064B-\\u065F\\u0670\\u0640]"), "")
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
        // Normalize Hamza
        res = res.replace(Regex("[ؤئ]"), "ء")
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
            .filter { it.length > 2 && !arabicStopWords.contains(it) }
            .toSet()
    }

    /**
     * High-Precision BestMatch Q&A retrieval algorithm
     */
    suspend fun findBestAnswer(userQuery: String): ChatbotMatchResult? = withContext(Dispatchers.Default) {
        if (corpus.isEmpty()) return@withContext null

        val queryNormalized = normalizeArabic(userQuery)
        if (queryNormalized.isBlank()) return@withContext null

        // 1. Direct Greetings Check
        val greetings = listOf("سلام", "السلام", "مرحبا", "اهلا", "صباح الخير", "مساء الخير", "حياك", "من انت", "كيف حالك", "هلا", "شكرا")
        if (greetings.any { queryNormalized.contains(normalizeArabic(it)) }) {
            val greetingEntry = corpus.find { it.id == 1 } ?: corpus.first()
            return@withContext ChatbotMatchResult(greetingEntry, 100f, listOf("تحية"))
        }

        val queryTokens = tokenize(userQuery)
        var bestEntry: ChatbotFatwaEntry? = null
        var highestScore = 0f
        var bestMatchedKeywords = emptyList<String>()

        for (entry in corpus) {
            var score = 0f
            val matched = mutableListOf<String>()
            val normQ = normalizeArabic(entry.question)
            val normKeywords = entry.keywords.map { normalizeArabic(it) }

            // 2. High-precision keyword hits
            for (kw in normKeywords) {
                if (kw.length > 2 && queryNormalized.contains(kw)) {
                    score += 25f
                    matched.add(kw)
                }
            }

            // 3. Question containment match
            if (normQ.contains(queryNormalized) && queryNormalized.length > 4) {
                score += 30f
            } else if (queryNormalized.contains(normQ) && normQ.length > 4) {
                score += 25f
            }

            // 4. Content tokens overlap (only counts meaningful terms)
            if (queryTokens.isNotEmpty()) {
                val qTokens = tokenize(entry.question)
                var hits = 0
                for (t in queryTokens) {
                    if (qTokens.contains(t) || normKeywords.any { it.contains(t) }) {
                        hits++
                    }
                }
                val overlapRatio = hits.toFloat() / queryTokens.size.toFloat()
                if (overlapRatio >= 0.5f) {
                    score += overlapRatio * 20f
                }
            }

            if (score > highestScore) {
                highestScore = score
                bestScore = score
                bestEntry = entry
                bestMatchedKeywords = matched.distinct()
            }
        }

        // Strict threshold: score must be >= 20.0f to eliminate hallucinations
        if (bestEntry != null && highestScore >= 20f) {
            ChatbotMatchResult(bestEntry, highestScore, bestMatchedKeywords)
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
            "ما حكم بخاخ الربو للصائم؟",
            "هل يشترط الوضوء لقراءة القرآن من الجوال؟",
            "كيفية ودعاء صلاة الاستخارة",
            "أحكام المسح على الجوارب في الوضوء",
            "كيف تحسب زكاة الأسهم الاستثمارية؟",
            "ما حكم استخدام المسبحة الإلكترونية؟",
            "ما هي كفارة اليمين المنعقدة؟"
        )
    }
}
