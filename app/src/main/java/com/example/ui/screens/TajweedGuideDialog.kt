package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

data class TajweedRule(
    val id: String,
    val name: String,
    val category: String,
    val color: Color,
    val letterCount: String,
    val letters: String,
    val description: String,
    val exampleText: String,
    val exampleExplanation: String
)

data class TajweedVerseSample(
    val surahName: String,
    val ayahNumber: Int,
    val fullText: String,
    val segments: List<TajweedSegment>
)

data class TajweedSegment(
    val text: String,
    val ruleId: String? = null
)

object TajweedData {
    val rules = listOf(
        TajweedRule(
            id = "madd_lazim",
            name = "المد اللازم (٦ حركات)",
            category = "المدود",
            color = Color(0xFFE53935),
            letterCount = "٦ حركات",
            letters = "الألف والواو والياء بعدها سكون أصلي أو شدة",
            description = "هو أن يأتي بعد حرف المد سكون أصلي أو حرف مشدد، ويمد لزوماً بمقدار ٦ حركات.",
            exampleText = "الْحَاقَّةُ • الصَّاخَّةُ • الضَّالِّينَ",
            exampleExplanation = "مد لازم كلمي مثقل يمد ست حركات لزوماً لالتقاء الساكنين."
        ),
        TajweedRule(
            id = "madd_wajib",
            name = "المد الواجب المتصل (٤-٥ حركات)",
            category = "المدود",
            color = Color(0xFFFF7043),
            letterCount = "٤ أو ٥ حركات",
            letters = "حرف مد بعده همزة في كلمة واحدة",
            description = "أن يجتمع حرف المد والهمزة في كلمة واحدة، وحكمه وجوب المد عند جميع القراء.",
            exampleText = "جَاءَ • السَّمَاءِ • سُوءَ",
            exampleExplanation = "مد متصل لوجود الهمزة بعد الألف في نفس الكلمة."
        ),
        TajweedRule(
            id = "madd_jaiz",
            name = "المد الجائز المنفصل (٢ أو ٤ حركات)",
            category = "المدود",
            color = Color(0xFFFFB74D),
            letterCount = "حركتان أو ٤",
            letters = "حرف مد بآخر كلمة وهمزة بأول كلمة تالية",
            description = "أن يكون حرف المد في آخر الكلمة الأولى، وهمزة القطع في بداية الكلمة التالية.",
            exampleText = "إِنَّا أَعْطَيْنَاكَ • يَا أَيُّهَا",
            exampleExplanation = "يجوز قصره حركتين وتوسطه ٤ حركات بحسب طريق الرواية."
        ),
        TajweedRule(
            id = "ghunnah",
            name = "الغنة المشددة (حركتان)",
            category = "الغنن",
            color = Color(0xFF26A69A),
            letterCount = "حركتان",
            letters = "النُّون والمِيم المشددتان (نّ، مّ)",
            description = "صوت رخيم يخرج من الخيشوم ملازم للنوني والميم المشددتين بمقدار حركتين.",
            exampleText = "إِنَّ • عَمَّ • النَّاسِ",
            exampleExplanation = "تغني النون أو الميم بمقدار حركتين مع حبس الصوت يسيراً في الخيشوم."
        ),
        TajweedRule(
            id = "ikhfa",
            name = "الإخفاء الحقيقي والشفوي",
            category = "النون والميم",
            color = Color(0xFF42A5F5),
            letterCount = "١٥ حرفاً للنون / حرف الباء للميم",
            letters = "ص، ذ، ث، ك، ج، ش، ق، س، د، ط، ز، ف، ت، ض، ظ",
            description = "نطق الحرف بصفة بين الإظهار والإدغام عارياً عن التشديد مع بقاء الغنة بمقدار حركتين.",
            exampleText = "مِن قَبْلُ • كُنتُمْ • يَعْتَصِم بِاللَّهِ",
            exampleExplanation = "إخفاء النون أو الميم مع إخراج الغنة كاملة من الخيشوم دون ضغط الشفتين بقوة."
        ),
        TajweedRule(
            id = "idgham_ghunnah",
            name = "الإدغام بغنة (ينمو)",
            category = "النون والميم",
            color = Color(0xFFAB47BC),
            letterCount = "٤ أحرف (ي، ن، م، و)",
            letters = "الياء، النون، الميم، الواو",
            description = "إدخال النون الساكنة أو التنوين في الحرف التالي بحيث يصيران حرفاً واحداً مشدداً مع غنة.",
            exampleText = "مَن يَقُولُ • مِّن وَالٍ • هُدًى لِّلْمُتَّقِينَ",
            exampleExplanation = "تدغم النون في الياء مع إظهار غنة حركتين من الخيشوم."
        ),
        TajweedRule(
            id = "iqlab",
            name = "الإقلاب (قلب النون ميماً)",
            category = "النون والميم",
            color = Color(0xFF7E57C2),
            letterCount = "حرف واحد (ب)",
            letters = "الباء بعد النون الساكنة أو التنوين",
            description = "قلب النون الساكنة أو التنوين ميماً مخفاة بغنة عند ملاقاة حرف الباء (يُرمز له بميم صغيرة مۘ).",
            exampleText = "مِنۢ بَعْدِ • أَنۢبِئْهُم • سَمِيعٌۢ بَصِيرٌ",
            exampleExplanation = "تنقلب النون الساكنة ميماً خالصة مخفاة مع فرجة يسيرة بين الشفتين وغنة حركتين."
        ),
        TajweedRule(
            id = "qalqalah",
            name = "القلقلة (قطب جد)",
            category = "صفات الحروف",
            color = Color(0xFF29B6F6),
            letterCount = "٥ أحرف (ق، ط، ب، ج، د)",
            letters = "القاف، الطاء، الباء، الجيم، الدال الساكنة",
            description = "اضطراب صوت الحرف عند النطق به ساكناً حتى يُسمع له نبرة قوية (كبرى عند الوقف، صغرى وسط الكلمة).",
            exampleText = "قُلْ هُوَ اللَّهُ أَحَدٌ • الْفَلَقِ • الْفَجْرِ",
            exampleExplanation = "اضطراب واهتزاز المخرج عند السكون ليخرج الحرف فصيحاً دون أن يختلط بحركة."
        )
    )

    val sampleVerses = listOf(
        TajweedVerseSample(
            surahName = "الإخلاص",
            ayahNumber = 1,
            fullText = "قُلْ هُوَ اللَّهُ أَحَدٌ ﴿١﴾ اللَّهُ الصَّمَدُ ﴿٢﴾ لَمْ يَلِدْ وَلَمْ يُولَدْ ﴿٣﴾ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ ﴿٤﴾",
            segments = listOf(
                TajweedSegment("قُلْ هُوَ اللَّهُ "),
                TajweedSegment("أَحَدٌ", "qalqalah"),
                TajweedSegment(" ﴿١﴾ اللَّهُ "),
                TajweedSegment("الصَّمَدُ", "qalqalah"),
                TajweedSegment(" ﴿٢﴾ لَمْ يَلِ"),
                TajweedSegment("دْ", "qalqalah"),
                TajweedSegment(" وَلَمْ يُولَ"),
                TajweedSegment("دْ", "qalqalah"),
                TajweedSegment(" ﴿٣﴾ وَلَمْ يَكُ"),
                TajweedSegment("ن لَّهُ", "idgham_ghunnah"),
                TajweedSegment(" كُفُوًا "),
                TajweedSegment("أَحَدٌ", "qalqalah"),
                TajweedSegment(" ﴿٤﴾")
            )
        ),
        TajweedVerseSample(
            surahName = "الفلق",
            ayahNumber = 1,
            fullText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ﴿١﴾ مِن شَرِّ مَا خَلَقَ ﴿٢﴾ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ﴿٣﴾ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ﴿٤﴾",
            segments = listOf(
                TajweedSegment("قُلْ أَعُوذُ بِرَبِّ الْفَلَ"),
                TajweedSegment("قِ", "qalqalah"),
                TajweedSegment(" ﴿١﴾ "),
                TajweedSegment("مِن شَرِّ", "ikhfa"),
                TajweedSegment(" مَا خَلَ"),
                TajweedSegment("قَ", "qalqalah"),
                TajweedSegment(" ﴿٢﴾ وَ"),
                TajweedSegment("مِن شَرِّ", "ikhfa"),
                TajweedSegment(" غَاسِقٍ إِذَا وَقَ"),
                TajweedSegment("بَ", "qalqalah"),
                TajweedSegment(" ﴿٣﴾ وَ"),
                TajweedSegment("مِن شَرِّ", "ikhfa"),
                TajweedSegment(" "),
                TajweedSegment("النَّفَّاثَاتِ", "ghunnah"),
                TajweedSegment(" فِي الْعُقَ"),
                TajweedSegment("دِ", "qalqalah"),
                TajweedSegment(" ﴿٤﴾")
            )
        ),
        TajweedVerseSample(
            surahName = "الكوثر والنصر",
            ayahNumber = 1,
            fullText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ ﴿١﴾ فَصَلِّ لِرَبِّكَ وَانْحَرْ ﴿٢﴾ إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ ﴿٣﴾",
            segments = listOf(
                TajweedSegment("إِنَّا", "ghunnah"),
                TajweedSegment(" "),
                TajweedSegment("إِنَّا أَعْطَيْنَاكَ", "madd_jaiz"),
                TajweedSegment(" الْكَوْثَرَ ﴿١﴾ فَصَلِّ لِرَبِّكَ وَانْحَرْ ﴿٢﴾ "),
                TajweedSegment("إِنَّ", "ghunnah"),
                TajweedSegment(" شَانِئَكَ هُوَ الْأَبْتَرُ ﴿٣﴾")
            )
        ),
        TajweedVerseSample(
            surahName = "آية الكرسي (مقطع)",
            ayahNumber = 255,
            fullText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ",
            segments = listOf(
                TajweedSegment("اللَّهُ "),
                TajweedSegment("لَا إِلَٰهَ", "madd_jaiz"),
                TajweedSegment(" إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ "),
                TajweedSegment("سِنَةٌ وَلَا", "idgham_ghunnah"),
                TajweedSegment(" نَوْمٌ ۚ لَّهُ مَا فِي "),
                TajweedSegment("السَّمَاوَاتِ", "madd_wajib"),
                TajweedSegment(" وَمَا فِي الْأَرْضِ")
            )
        )
    )
}

@Composable
fun TajweedGuideDialog(
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("الكل") }
    var selectedRule by remember { mutableStateOf<TajweedRule?>(null) }
    var activeTab by remember { mutableIntStateOf(0) } // 0: الأحكام والقواعد, 1: مصحف التجويد النموذجي

    val categories = listOf("الكل", "المدود", "النون والميم", "الغنن", "صفات الحروف")

    val filteredRules = remember(selectedCategory) {
        if (selectedCategory == "الكل") TajweedData.rules
        else TajweedData.rules.filter { it.category == selectedCategory }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(RoundedCornerShape(26.dp)),
            color = Color(0xFF061A14),
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
                            .background(Color(0xFF0F3226), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = IslamicGoldPrimary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "مصحف التجويد الملون وأحكام التلاوة",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "دليل علم التجويد الميسر بالألوان الدلالية",
                            style = MaterialTheme.typography.bodySmall,
                            color = IslamicTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x33E2B84D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mode Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D281E))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { activeTab = 0 },
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeTab == 0) IslamicGoldPrimary else Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "قواعد وألوان التجويد",
                            color = if (activeTab == 0) Color(0xFF061A14) else IslamicTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { activeTab = 1 },
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeTab == 1) IslamicGoldPrimary else Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "مصحف التجويد العملي",
                            color = if (activeTab == 1) Color(0xFF061A14) else IslamicTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (activeTab == 0) {
                    // Category Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            val isSelected = cat == selectedCategory
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) IslamicGoldPrimary else Color(0xFF0E2A20),
                                border = BorderStroke(1.dp, if (isSelected) IslamicGoldPrimary else IslamicBorderGold),
                                modifier = Modifier.clickable { selectedCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) Color(0xFF061A14) else IslamicGoldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredRules) { rule ->
                            GlassCard(
                                backgroundColor = Color(0xFF092219),
                                borderColor = rule.color.copy(alpha = 0.5f),
                                borderWidth = 1.2.dp,
                                onClick = { selectedRule = rule }
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .clip(CircleShape)
                                                    .background(rule.color)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = rule.name,
                                                color = IslamicGoldLight,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = rule.color.copy(alpha = 0.2f),
                                            border = BorderStroke(0.5.dp, rule.color)
                                        ) {
                                            Text(
                                                text = rule.letterCount,
                                                color = rule.color,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = rule.description,
                                        color = IslamicTextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF04140F)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "مثال قرآني: ",
                                                color = IslamicGoldPrimary,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = rule.exampleText,
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontFamily = FontFamily.Serif
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Practical Tajweed Quran Samples
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF0C2B20),
                                border = BorderStroke(1.dp, IslamicBorderGold)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.TouchApp, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "اضغط على أي كلمة ملونة أدناه لمعرفة حكمها التجويدي وكيفية نطقها الصحيح.",
                                        color = IslamicGoldLight,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        items(TajweedData.sampleVerses) { sample ->
                            GlassCard(
                                backgroundColor = Color(0xFF0A241B),
                                borderColor = IslamicBorderGold
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "سورة ${sample.surahName}",
                                            color = IslamicGoldPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFF133C2D)
                                        ) {
                                            Text(
                                                text = "مصحف المدينة الملون",
                                                color = IslamicMintLight,
                                                fontSize = 9.5.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Render Colored Tajweed Text
                                    val annotatedText = buildAnnotatedString {
                                        sample.segments.forEach { segment ->
                                            val rule = TajweedData.rules.find { it.id == segment.ruleId }
                                            if (rule != null) {
                                                withStyle(
                                                    style = SpanStyle(
                                                        color = rule.color,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                ) {
                                                    append(segment.text)
                                                }
                                            } else {
                                                withStyle(style = SpanStyle(color = Color.White)) {
                                                    append(segment.text)
                                                }
                                            }
                                        }
                                    }

                                    Text(
                                        text = annotatedText,
                                        fontSize = 17.sp,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 28.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFF051711), RoundedCornerShape(12.dp))
                                            .padding(14.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Quick tags for rules present in this verse
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        val uniqueRules = sample.segments.mapNotNull { it.ruleId }.distinct()
                                        uniqueRules.forEach { rId ->
                                            val r = TajweedData.rules.find { it.id == rId } ?: return@forEach
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = r.color.copy(alpha = 0.2f),
                                                border = BorderStroke(0.5.dp, r.color),
                                                modifier = Modifier.clickable { selectedRule = r }
                                            ) {
                                                Text(
                                                    text = "● ${r.name.split(" ").take(2).joinToString(" ")}",
                                                    color = r.color,
                                                    fontSize = 9.5.sp,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
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

    // Rule Detail Bottom Sheet / Alert Dialog
    if (selectedRule != null) {
        val rule = selectedRule!!
        AlertDialog(
            onDismissRequest = { selectedRule = null },
            containerColor = Color(0xFF09241B),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(18.dp).clip(CircleShape).background(rule.color))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(rule.name, color = IslamicGoldPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("التعريف والحكم:", color = IslamicGoldLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(rule.description, color = IslamicTextPrimary, fontSize = 13.sp, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("الأحرف والقواعد:", color = IslamicGoldLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(rule.letters, color = rule.color, fontSize = 12.5.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("الشرح والتطبيق:", color = IslamicGoldLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(rule.exampleExplanation, color = IslamicTextSecondary, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedRule = null },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                ) {
                    Text("تم الفهم", color = Color(0xFF061A14), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
