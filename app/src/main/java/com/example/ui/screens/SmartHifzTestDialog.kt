package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

data class HifzQuestion(
    val id: Int,
    val surahName: String,
    val questionText: String,
    val promptAyah: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

object SmartHifzQuizData {
    val questions = listOf(
        HifzQuestion(
            id = 1,
            surahName = "الملك",
            questionText = "ما هي الآية التالية لقوله تعالى:",
            promptAyah = "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ ﴿١﴾",
            options = listOf(
                "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ",
                "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ",
                "وَلَقَدْ زَيَّنَّا السَّمَاءَ الدُّنْيَا بِمَصَابِيحَ وَجَعَلْنَاهَا رُجُومًا لِّلشَّيَاطِينِ",
                "وَإِذَا أُلْقُوا فِيهَا سَمِعُوا لَهَا شَهِيقًا وَهِيَ تَفُورُ"
            ),
            correctIndex = 0,
            explanation = "الآية رقم ٢ من سورة الملك تذكر حكمة خلق الموت والحياة وهو الابتلاء بحسن العمل."
        ),
        HifzQuestion(
            id = 2,
            surahName = "الكهف",
            questionText = "أكمل قوله تعالى من أوائل سورة الكهف:",
            promptAyah = "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُۥ عِوَجَاۜ ﴿١﴾",
            options = listOf(
                "وَيُنذِرَ الَّذِينَ قَالُوا اتَّخَذَ اللَّهُ وَلَدًا",
                "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا",
                "مَّاكِثِينَ فِيهِ أَبَدًا",
                "فَلَعَلَّكَ بَاخِعٌ نَّفْسَكَ عَلَىٰ آثَارِهِمْ إِن لَّمْ يُؤْمِنُوا بِهَٰذَا الْحَدِيثِ أَسَفًا"
            ),
            correctIndex = 1,
            explanation = "الآية رقم ٢ مكملة لنعت الكتاب بأنه 'قيماً لينذر بأساً شديداً'."
        ),
        HifzQuestion(
            id = 3,
            surahName = "النبأ",
            questionText = "ما هي السورة التي وردت فيها هذه الآية الكريمة؟",
            promptAyah = "إِنَّ لِلْمُتَّقِينَ مَفَازًا ﴿٣١﴾ حَدَائِقَ وَأَعْنَابًا ﴿٣٢﴾",
            options = listOf(
                "سورة النازعات",
                "سورة النبأ",
                "سورة التكوير",
                "سورة المطففين"
            ),
            correctIndex = 1,
            explanation = "هذه الآيات في سورة النبأ تصف جزاء المتقين ونعيمهم في الجنات."
        ),
        HifzQuestion(
            id = 4,
            surahName = "يس",
            questionText = "أكمل الآية التالية من سورة يس:",
            promptAyah = "وَاضْرِبْ لَهُم مَّثَلًا أَصْحَابَ الْقَرْيَةِ إِذْ جَاءَهَا الْمُرْسَلُونَ ﴿١٣﴾",
            options = listOf(
                "إِذْ أَرْسَلْنَا إِلَيْهِمُ اثْنَيْنِ فَكَذَّبُوهُمَا فَعَزَّزْنَا بِثَالِثٍ فَقَالُوا إِنَّا إِلَيْكُم مُّرْسَلُونَ",
                "قَالُوا مَا أَنتُمْ إِلَّا بَشَرٌ مِّثْلُنَا وَمَا أَنزَلَ الرَّحْمَٰنُ مِن شَيْءٍ",
                "قَالُوا رَبُّنَا يَعْلَمُ إِنَّا إِلَيْكُمْ لَمُرْسَلُونَ",
                "وَجَاءَ مِنْ أَقْصَى الْمَدِينَةِ رَجُلٌ يَسْعَىٰ قَالَ يَا قَوْمِ اتَّبِعُوا الْمُرْسَلِينَ"
            ),
            correctIndex = 0,
            explanation = "الآية ١٤ تبيّن إرسال الرسولين ثم تعزيزهما بثالث."
        ),
        HifzQuestion(
            id = 5,
            surahName = "الرحمن",
            questionText = "ما هي الآية المتكررة في سورة الرحمن 31 مرة؟",
            promptAyah = "فَبِأَيِّ آلَاءِ رَبِّكُمَا ...",
            options = listOf(
                "تَكْذِيبٌ",
                "تُكَذِّبَانِ",
                "تَكْفُرَانِ",
                "تَشْكُرَانِ"
            ),
            correctIndex = 1,
            explanation = "الآية الكريمة: 'فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ' تتكرر كترديد لآلاء الله ونعمه."
        ),
        HifzQuestion(
            id = 6,
            surahName = "البقرة",
            questionText = "في أي موضع تنتهي آية الكرسي العظيمة؟",
            promptAyah = "يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ ...",
            options = listOf(
                "وَهُوَ الْغَفُورُ الرَّحِيمُ",
                "وَاللَّهُ سَمِيعٌ عَلِيمٌ",
                "وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
                "إِنَّ اللَّهَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ"
            ),
            correctIndex = 2,
            explanation = "خاتمة آية الكرسي: 'وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ'."
        ),
        HifzQuestion(
            id = 7,
            surahName = "الإنسان",
            questionText = "ما الآية التي تبدأ بها سورة الإنسان؟",
            promptAyah = "هَلْ أَتَىٰ عَلَى الْإِنسَانِ حِينٌ مِّنَ الدَّهْرِ ...",
            options = listOf(
                "لَمْ يَكُن شَيْئًا مَّذْكُورًا",
                "إِنَّا خَلَقْنَا الْإِنسَانَ مِن نُّطْفَةٍ أَمْشَاجٍ",
                "إِنَّا هَدَيْنَاهُ السَّبِيلَ إِمَّا شَاكِرًا وَإِمَّا كَفُورًا",
                "إِنَّ الْأَبْرَارَ يَشْرَبُونَ مِن كَأْسٍ كَانَ مِزَاجُهَا كَافُورًا"
            ),
            correctIndex = 0,
            explanation = "'هَلْ أَتَىٰ عَلَى الْإِنسَانِ حِينٌ مِّنَ الدَّهْرِ لَمْ يَكُن شَيْئًا مَّذْكُورًا' ﴿١﴾"
        )
    )
}

@Composable
fun SmartHifzTestDialog(
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    val questions = SmartHifzQuizData.questions
    val currentQuestion = questions.getOrNull(currentIndex)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(RoundedCornerShape(26.dp)),
            color = Color(0xFF061A13),
            border = BorderStroke(1.5.dp, IslamicGoldPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFF0F3225), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = IslamicGoldPrimary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🧠 اختبار الحفظ القرآني الذكي",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (!isFinished) "سؤال ${currentIndex + 1} من ${questions.size}" else "نتيجة الاختبار",
                            style = MaterialTheme.typography.bodySmall,
                            color = IslamicTextSecondary
                        )
                    }

                    // Score Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F3528),
                        border = BorderStroke(1.dp, IslamicBorderGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$score",
                                color = IslamicGoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!isFinished && currentQuestion != null) {
                    // Progress Bar
                    val progress = (currentIndex + 1).toFloat() / questions.size.toFloat()
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = IslamicGoldPrimary,
                        trackColor = Color(0xFF0E2A20)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Prompt Ayah Card
                        item {
                            GlassCard(
                                backgroundColor = Color(0xFF0B261D),
                                borderColor = IslamicBorderGold
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF133E2F)
                                        ) {
                                            Text(
                                                text = "سورة ${currentQuestion.surahName}",
                                                color = IslamicMintLight,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        if (streak > 1) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0x33FFB300)
                                            ) {
                                                Text(
                                                    text = "🔥 متتالية: $streak",
                                                    color = Color(0xFFFFD54F),
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = currentQuestion.questionText,
                                        color = IslamicGoldLight,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = currentQuestion.promptAyah,
                                        fontSize = 17.sp,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 26.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFF061812), RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    )
                                }
                            }
                        }

                        // Choices
                        items(currentQuestion.options.indices.toList()) { optIndex ->
                            val option = currentQuestion.options[optIndex]
                            val isSelected = selectedOptionIndex == optIndex
                            val isCorrect = optIndex == currentQuestion.correctIndex

                            val backgroundColor = when {
                                !isAnswered -> if (isSelected) Color(0xFF133E2F) else Color(0xFF092118)
                                isCorrect -> Color(0xFF144D29) // Green
                                isSelected -> Color(0xFF4D1414) // Red for wrong
                                else -> Color(0xFF092118)
                            }

                            val borderColor = when {
                                !isAnswered -> if (isSelected) IslamicGoldPrimary else IslamicBorderGold
                                isCorrect -> Color(0xFF4CAF50)
                                isSelected -> Color(0xFFE53935)
                                else -> IslamicBorderGold
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = backgroundColor,
                                border = BorderStroke(1.2.dp, borderColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !isAnswered) {
                                        selectedOptionIndex = optIndex
                                        isAnswered = true
                                        if (isCorrect) {
                                            score += 10
                                            streak += 1
                                        } else {
                                            streak = 0
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isAnswered && isCorrect) Color(0xFF4CAF50)
                                                else if (isAnswered && isSelected) Color(0xFFE53935)
                                                else Color(0xFF123427)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${optIndex + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = option,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 22.sp,
                                        color = if (isAnswered && isCorrect) Color(0xFFA5D6A7) else IslamicTextPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Explanation upon answer
                        if (isAnswered) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF0D2C20),
                                    border = BorderStroke(1.dp, IslamicBorderGold)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                if (selectedOptionIndex == currentQuestion.correctIndex) Icons.Default.CheckCircle else Icons.Default.Info,
                                                contentDescription = null,
                                                tint = if (selectedOptionIndex == currentQuestion.correctIndex) Color(0xFF81C784) else IslamicGoldPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (selectedOptionIndex == currentQuestion.correctIndex) "إجابة صحيحة بارك الله فيك! 🎉" else "توضيح الحفظ:",
                                                fontWeight = FontWeight.Bold,
                                                color = IslamicGoldPrimary,
                                                fontSize = 12.5.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentQuestion.explanation,
                                            color = IslamicTextSecondary,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Next Button
                    if (isAnswered) {
                        Button(
                            onClick = {
                                if (currentIndex + 1 < questions.size) {
                                    currentIndex += 1
                                    selectedOptionIndex = null
                                    isAnswered = false
                                } else {
                                    isFinished = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                        ) {
                            Text(
                                text = if (currentIndex + 1 < questions.size) "السؤال التالي ⬅" else "عرض نتيجة الاختبار 🏆",
                                color = Color(0xFF061A13),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    // Result Screen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0D3325)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = IslamicGoldPrimary,
                                modifier = Modifier.size(46.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "مبارك! أتممت اختبار الحفظ بنجاح",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val percentage = (score.toFloat() / (questions.size * 10).toFloat() * 100).toInt()
                        Text(
                            text = "حصلت على $score نقطة من إجمالي ${questions.size * 10} ($percentage%)",
                            color = IslamicMintLight,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        GlassCard(
                            backgroundColor = Color(0xFF09241B),
                            borderColor = IslamicBorderGold,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (percentage >= 80) "🌟 حافظ متقن ومتميز! واصل المراجعة والتثبيت."
                                    else if (percentage >= 50) "📖 مستوى جيد! داوم على مراجعة المتشابهات بانتظام."
                                    else "💪 بداية طيبة، كرر سماع السورة وقراءتها في الصلاة لترسيخها.",
                                    color = Color.White,
                                    fontSize = 13.5.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    currentIndex = 0
                                    score = 0
                                    streak = 0
                                    selectedOptionIndex = null
                                    isAnswered = false
                                    isFinished = false
                                },
                                modifier = Modifier.weight(1f).height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF133E2F)),
                                border = BorderStroke(1.dp, IslamicGoldPrimary)
                            ) {
                                Text("إعادة الاختبار", color = IslamicGoldLight, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f).height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                            ) {
                                Text("إغلاق", color = Color(0xFF061A13), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
