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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.components.IosGrabberHandle
import com.example.ui.components.IosSegmentedControl
import com.example.ui.theme.*

data class QuranWordEntry(
    val word: String,
    val root: String,
    val surah: String,
    val ayahNum: Int,
    val meaning: String,
    val contextText: String
)

data class VerseIrabEntry(
    val surahName: String,
    val ayahNum: Int,
    val verseText: String,
    val irabBreakdown: List<Pair<String, String>>
)

object QuranLexiconData {
    val dictionaryWords = listOf(
        QuranWordEntry("الصَّمَدُ", "ص-م-د", "الإخلاص", 2, "السيد الذي كمل في سؤدده ويُصمد إليه في الحوائج، والذي لا جوف له ولا يطعم ولا يشرب.", "﴿اللَّهُ الصَّمَدُ﴾"),
        QuranWordEntry("غَاسِقٍ إِذَا وَقَبَ", "غ-س-ق / و-ق-ب", "الفلق", 3, "الغاسق: الليل إذا أظلم ودخل ظلامه في كل شيء.", "﴿وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ﴾"),
        QuranWordEntry("الْوَسْوَاسِ الْخَنَّاسِ", "خ-ن-س", "الناس", 4, "الخناس: الشيطان الذي يختفي وينقبض عند ذكر الله، فإذا غفل العبد وسوس.", "﴿مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ﴾"),
        QuranWordEntry("سِنَةٌ", "و-س-ن", "البقرة", 255, "السِّنَة: النعاس والفتور الذي يسبق النوم ويكون في العين والقلب يقظان.", "﴿لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ﴾"),
        QuranWordEntry("ضِيزَىٰ", "ض-و-ز", "النجم", 22, "قسمة جائرة ظالمة مائلة عن العدل والحق.", "﴿تِلْكَ إِذًا قِسْمَةٌ ضِيزَىٰ﴾"),
        QuranWordEntry("عَسْعَسَ", "ع-س-س", "التكوير", 17, "أقبل بظلامه، أو أدبر وولّى، وهي من الكلمات التي تدل على الضدين.", "﴿وَاللَّيْلِ إِذَا عَسْعَسَ﴾"),
        QuranWordEntry("فَاقِعٌ لَّوْنُهَا", "ف-ق-ع", "البقرة", 69, "شديد الصفرة خالص لا تشوبه حمرة ولا بياض يسر الناظرين.", "﴿صَفْرَاءُ فَاقِعٌ لَّوْنُهَا تَسُرُّ النَّاظِرِينَ﴾"),
        QuranWordEntry("حَصْحَصَ الْحَقُّ", "ح-ص-ح-ص", "يوسف", 51, "ظهر وتبين وانكشف الحق بعد خفائه.", "﴿الْآنَ حَصْحَصَ الْحَقُّ أَنَا رَاوَدتُّهُ عَن نَّفْسِهِ﴾")
    )

    val irabVerses = listOf(
        VerseIrabEntry(
            surahName = "الفاتحة",
            ayahNum = 1,
            verseText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            irabBreakdown = listOf(
                "بِـ" to "حرف جر مبني على الكسر لا محل له من الإعراب.",
                "اسْمِ" to "اسم مجرور بالباء وعلامة جره الكسرة، والجار والمجرور متعلقان بمحذوف تقديره (أقرأ أو أبدأ). وهو مضاف.",
                "اللَّهِ" to "اسم الجلالة مضاف إليه مجرور وعلامة جره الكسرة الظاهرة تعظيماً.",
                "الرَّحْمَٰنِ" to "نعت أول لاسم الجلالة مجرور وعلامة جره الكسرة الظاهرة.",
                "الرَّحِيمِ" to "نعت ثانٍ لاسم الجلالة مجرور وعلامة جره الكسرة الظاهرة."
            )
        ),
        VerseIrabEntry(
            surahName = "الإخلاص",
            ayahNum = 1,
            verseText = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            irabBreakdown = listOf(
                "قُلْ" to "فعل أمر مبني على السكون، والفاعل ضمير مستتر وجوباً تقديره (أنت).",
                "هُوَ" to "ضمير الشأن منفصل مبني على الفتح في محل رفع مبتدأ أول.",
                "اللَّهُ" to "اسم الجلالة مبتدأ ثانٍ مرفوع وعلامة رفعه الضمة الظاهرة.",
                "أَحَدٌ" to "خبر المبتدأ الثاني مرفوع وعلامة رفعه الضمة، والجملة الاسمية خبر المبتدأ الأول."
            )
        ),
        VerseIrabEntry(
            surahName = "الكوثر",
            ayahNum = 1,
            verseText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ",
            irabBreakdown = listOf(
                "إِنَّا" to "إنّ: حرف توكيد ونصب، و(نا) ضمير متصل مبني في محل نصب اسم إنّ.",
                "أَعْطَيْنَاكَ" to "أعطى: فعل ماضٍ مبني على السكون، و(نا) فاعل، والكاف ضمير متصل في محل نصب مفعول به أول.",
                "الْكَوْثَرَ" to "مفعول به ثانٍ منصوب وعلامة نصبه الفتحة الظاهرة، وجملة (أعطيناك) في محل رفع خبر إنّ."
            )
        )
    )
}

@Composable
fun QuranDictionaryIrabDialog(
    onDismiss: () -> Unit
) {
    var activeTab by remember { mutableIntStateOf(0) } // 0: معجم غريب القرآن, 1: إعراب الآيات
    var searchQuery by remember { mutableStateOf("") }

    val filteredWords = remember(searchQuery) {
        if (searchQuery.isBlank()) QuranLexiconData.dictionaryWords
        else QuranLexiconData.dictionaryWords.filter {
            it.word.contains(searchQuery.trim()) ||
            it.meaning.contains(searchQuery.trim()) ||
            it.root.contains(searchQuery.trim()) ||
            it.surah.contains(searchQuery.trim())
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(32.dp)),
            color = IosBgBlack,
            border = BorderStroke(1.2.dp, IosBorderGlass)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                IosGrabberHandle()

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0x33FFFFFF), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White, modifier = Modifier.size(18.dp))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📚 معجم القرآن الكريم وإعراب الآيات",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "غريب الألفاظ، الجذور، والنحو القرآني الميسر",
                            fontSize = 11.5.sp,
                            color = IosTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x2EE2B84D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("معجم غريب القرآن", "إعراب الآيات الميسر"),
                    selectedIndex = activeTab,
                    onSelectIndex = { activeTab = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (activeTab == 0) {
                    // Search box
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("ابحث عن كلمة، جذر، أو معنى قرآني...", color = Color.Gray, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IosGoldApple,
                            unfocusedBorderColor = IosBorderGlass,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0x33000000),
                            unfocusedContainerColor = Color(0x33000000)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredWords) { entry ->
                            GlassCard(
                                backgroundColor = IosCardGlass,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = entry.word,
                                            color = IosGoldApple,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily.Serif
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0x332DD4BF)
                                        ) {
                                            Text(
                                                text = "جذر: ${entry.root}",
                                                color = IosEmeraldPro,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = entry.contextText,
                                        fontSize = 13.sp,
                                        color = Color(0xFFFFD54F),
                                        fontFamily = FontFamily.Serif
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = entry.meaning,
                                        color = IosTextPrimary,
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "سورة ${entry.surah} • آية ${entry.ayahNum}",
                                        color = IosTextSecondary,
                                        fontSize = 10.5.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Irab Breakdown View
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(QuranLexiconData.irabVerses) { vIrab ->
                            GlassCard(
                                backgroundColor = IosCardGlass,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "سورة ${vIrab.surahName} [آية ${vIrab.ayahNum}]",
                                            color = IosGoldApple,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0x2EE2B84D)
                                        ) {
                                            Text(
                                                text = "إعراب الآية",
                                                color = IosGoldApple,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = vIrab.verseText,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = Color.White,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0x33000000), RoundedCornerShape(10.dp))
                                            .padding(10.dp)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    vIrab.irabBreakdown.forEach { (wordPart, irabDesc) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text(
                                                text = "• $wordPart : ",
                                                color = IosEmeraldPro,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp
                                            )
                                            Text(
                                                text = irabDesc,
                                                color = IosTextPrimary,
                                                fontSize = 12.sp,
                                                lineHeight = 17.sp,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
