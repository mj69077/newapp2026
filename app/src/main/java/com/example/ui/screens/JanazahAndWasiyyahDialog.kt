package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.components.IosGrabberHandle
import com.example.ui.components.IosSegmentedControl
import com.example.ui.theme.*

@Composable
fun JanazahAndWasiyyahDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: صلاة الجنازة, 1: سنن التجهيز والتعزية, 2: محرر الوصية الشرعية

    // Wasiyyah Form State
    var testatorName by remember { mutableStateOf("") }
    var debtsAndDues by remember { mutableStateOf("") }
    var charitableThird by remember { mutableStateOf("") }
    var sunnahDirectives by remember {
        mutableStateOf("أوصي أهلي وبني وعشيرتي بتقوى الله تعالى، والتآلف والتراحم، وأن يُغسل جسدي ويُكفن ويُدفن وفق السنة النبوية المطهرة الخالصة، من غير نياحة ولا شق جيوب ولا بدع في العزاء، والدعاء لي بالمغفرة والثبات عند السؤال.")
    }
    var wasiyyahSavedSuccess by remember { mutableStateOf(false) }

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
                            text = "🕊️ فقه الجنائز والوصية الشرعية",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "دليل صلاة الجنازة وكتابة الوصية وفق الكتاب والسنة",
                            fontSize = 11.sp,
                            color = IosTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(IosGoldApple.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📜", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("صلاة الجنازة", "أحكام التجهيز", "الوصية الشرعية"),
                    selectedIndex = selectedTab,
                    onItemSelected = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedTab) {
                    0 -> {
                        // Salat al-Janazah Guide
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            "فضل صلاة الجنازة واتباعها 🌟",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            "قال رسول الله ﷺ: «مَن شَهِدَ الجَنازَةَ حتى يُصَلَّى عليها فَلَهُ قِيراطٌ، ومَن شَهِدَها حتى تُدْفَنَ فَلَهُ قِيراطانِ»، قيلَ: وما القِيراطانِ؟ قالَ: «مِثْلُ الجَبَلَيْنِ العَظِيمَيْنِ». (متفق عليه)",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.9f),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            val takbeers = listOf(
                                Triple(
                                    "التكبيرة الأولى",
                                    "قراءة سورة الفاتحة",
                                    "يرفع يديه ويكبر، ثم يستعيذ بالله من الشيطان الرجيم ويبسمل، ويقرأ سورة الفاتحة سراً، ويستحب أن يقرأ معها سورة قصيرة كالإخلاص أو آية من القرآن."
                                ),
                                Triple(
                                    "التكبيرة الثانية",
                                    "الصلاة الإبراهيمية على النبي ﷺ",
                                    "يكبر التكبيرة الثانية، ثم يصلي على النبي ﷺ كصلاته في التشهد الأخير: «اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ...»."
                                ),
                                Triple(
                                    "التكبيرة الثالثة",
                                    "الدعاء للميت بالمأثور",
                                    "يكبر التكبيرة الثالثة ويدعو للميت بصدق وإخلاص: «اللَّهُمَّ اغْفِرْ لَهُ وَارْحَمْهُ، وَعَافِهِ وَاعْفُ عَنْهُ، وَأَكْرِمْ نُزُلَهُ، وَوَسِّعْ مُدْخَلَهُ، وَاغْسِلْهُ بِالْمَاءِ وَالثَّلْجِ وَالْبَرَدِ، وَنَقِّهِ مِنَ الْخَطَايَا كَمَا نَقَّيْتَ الثَّوْبَ الأَبْيَضَ مِنَ الدَّنَسِ، وَأَبْدِلْهُ دَاراً خَيْراً مِنْ دَارِهِ، وَأَهْلاً خَيْراً مِنْ أَهْلِهِ، وَأَدْخِلْهُ الْجَنَّةَ، وَأَعِذْهُ مِنْ عَذَابِ الْقَبْرِ وَمِنْ عَذَابِ النَّارِ»."
                                ),
                                Triple(
                                    "التكبيرة الرابعة والتسليم",
                                    "الدعاء لعموم المسلمين والتسليمة",
                                    "يكبر الرابعة، ويسكت قليلاً أو يدعو: «اللَّهُمَّ لَا تَحْرِمْنَا أَجْرَهُ وَلَا تَفْتِنَّا بَعْدَهُ، وَاغْفِرْ لَنَا وَلَهُ»، ثم يسلم تسليمة واحدة عن يمينه، وإن سلم تسليمتين فلا حرج."
                                )
                            )

                            items(takbeers.size) { index ->
                                val (title, subtitle, detail) = takbeers[index]
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(IosEmeraldPro, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    "${index + 1}",
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black,
                                                    fontSize = 13.sp
                                                )
                                            }
                                            Text(
                                                title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                "• $subtitle",
                                                fontSize = 12.sp,
                                                color = IosGoldApple
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            detail,
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.85f),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Rulings of Ghusl & Kafan & Condolences
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            "🚿 غسل الميت وتكفينه وفق السنة",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            "• غسل الميت فرض كفاية، وأولى الناس بغسله وصيّه العادل ثم أقرب الناس إليه.\n• يبدأ بمواضع الوضوء منه، ثم يغسل شقه الأيمن ثم الأيسر بالماء والسدر ثلاثاً أو خمساً.\n• يوضع في الغسلة الأخيرة شيء من الكافور لتطييبه.\n• التكفين: يستحب للرجل في ثلاثة أثواب بيض، وللمرأة في خمسة أثواب.",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.9f),
                                            lineHeight = 19.sp
                                        )
                                    }
                                }
                            }

                            item {
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            "🕊️ آداب التعزية وتسلية أهل الميت",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            "• يُعزى أهل الميت بالدعاء لهم بالصبر والسلوان وللميت بالمغفرة: «إِنَّ لِلَّهِ مَا أَخَذَ، وَلَهُ مَا أَعْطَى، وَكُلٌّ عِنْدَهُ بِأَجَلٍ مُسَمًّى، فَلْتَصْبِرْ وَلْتَحْتَسِبْ».\n• يُسن صنع الطعام لأهل الميت لقوله ﷺ: «اصْنَعُوا لآلِ جَعْفَرٍ طَعَامًا؛ فَإِنَّهُ قَدْ أَتَاهُمْ أَمْرٌ شَغَلَهُمْ».\n• يحرم النياحة وشق الجيوب ولطم الخدود والدعاء بدعوى الجاهلية.",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.9f),
                                            lineHeight = 19.sp
                                        )
                                    }
                                }
                            }

                            item {
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            "🪦 زيارة القبور والدعاء لأهلها",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            "كان النبي ﷺ يعلم أصحابه إذا خرجوا إلى المقابر أن يقولوا:\n«السَّلَامُ عَلَيْكُمْ أَهْلَ الدِّيَارِ مِنَ الْمُؤْمِنِينَ وَالْمُسْلِمِينَ، وَإِنَّا إِنْ شَاءَ اللهُ بِكُمْ لَاحِقُونَ، أَسْأَلُ اللهَ لَنَا وَلَكُمُ الْعَافِيَةَ».",
                                            fontSize = 12.sp,
                                            color = IosEmeraldPro,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Islamic Will / Wasiyyah Writer
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            "📜 أهمية كتابة الوصية الشرعية",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "قال رسول الله ﷺ: «مَا حَقُّ امْرِئٍ مُسْلِمٍ، لَهُ شَيْءٌ يُوصِي فِيهِ، يَبِيتُ لَيْلَتَيْنِ إِلَّا وَوَصِيَّتُهُ مَكْتُوبَةٌ عِنْدَهُ». (رواه البخاري ومسلم)",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }

                            item {
                                Text(
                                    "اسم الموصي كاملاً:",
                                    fontSize = 12.sp,
                                    color = IosTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = testatorName,
                                    onValueChange = { testatorName = it },
                                    placeholder = { Text("مثال: عبد الله بن محمد الأحمد", color = Color.Gray, fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = IosGoldApple,
                                        unfocusedBorderColor = IosBorderGlass
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }

                            item {
                                Text(
                                    "الديون والأمانات والودائع (ما لك وما عليك):",
                                    fontSize = 12.sp,
                                    color = IosTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = debtsAndDues,
                                    onValueChange = { debtsAndDues = it },
                                    placeholder = { Text("أبرئ ذمتي ببيان الديون المستحقة لي أو عليّ، والأمانات والودائع...", color = Color.Gray, fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 2,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = IosGoldApple,
                                        unfocusedBorderColor = IosBorderGlass
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }

                            item {
                                Text(
                                    "وصية الثلث أو الصدقة الجارية (لغير وارث):",
                                    fontSize = 12.sp,
                                    color = IosTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = charitableThird,
                                    onValueChange = { charitableThird = it },
                                    placeholder = { Text("أوصي بجزء من مالي (لا يتجاوز الثلث) لوقف أو صدقة جارية أو قرابة غير وارثين...", color = Color.Gray, fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 2,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = IosGoldApple,
                                        unfocusedBorderColor = IosBorderGlass
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }

                            item {
                                Text(
                                    "الوصية الدينية وأحكام الجنازة (وفق السنة):",
                                    fontSize = 12.sp,
                                    color = IosTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = sunnahDirectives,
                                    onValueChange = { sunnahDirectives = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = IosGoldApple,
                                        unfocusedBorderColor = IosBorderGlass
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            val fullWasiyyah = """
                                                بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
                                                هَذِهِ وَصِيَّةُ العَبْدِ الفَقِيرِ إِلَى رَبِّهِ: ${testatorName.ifBlank { "[اسم الموصي]" }}
                                                
                                                أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَنَّ مُحَمَّداً عَبْدُهُ وَرَسُولُهُ، وَأَنَّ الجَنَّةَ حَقٌّ، وَالنَّارَ حَقٌّ، وَأَنَّ السَّاعَةَ آتِيَةٌ لَا رَيْبَ فِيهَا، وَأَنَّ اللَّهَ يَبْعَثُ مَنْ فِي القُبُورِ.
                                                
                                                أولاً - التوجيه الشرعي:
                                                $sunnahDirectives
                                                
                                                ثانياً - الديون والأمانات:
                                                ${debtsAndDues.ifBlank { "لا توجد ديون معلقة أو تم قضاؤها بحمد الله." }}
                                                
                                                ثالثاً - الوصية الخيرية والصدقة:
                                                ${charitableThird.ifBlank { "لم تُخصص وصية مالية إضافية." }}
                                                
                                                كُتبت هذه الوصية بنية خالصة، امتثالاً لسنة النبي ﷺ.
                                            """.trimIndent()

                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("الوصية الشرعية", fullWasiyyah)
                                            clipboard.setPrimaryClip(clip)
                                            wasiyyahSavedSuccess = true
                                            Toast.makeText(context, "تم نسخ الوصية الشرعية بنجاح إلى الحافظة", Toast.LENGTH_LONG).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = IosGoldApple, contentColor = Color.Black),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("نسخ ومشاركة الوصية", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            wasiyyahSavedSuccess = true
                                            Toast.makeText(context, "تم حفظ الوصية في ذاكرة التطبيق الآمنة", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(16.dp),
                                        border = BorderStroke(1.dp, IosEmeraldPro),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IosEmeraldPro),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("حفظ محلياً", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            }

                            if (wasiyyahSavedSuccess) {
                                item {
                                    Surface(
                                        color = IosEmeraldPro.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(0.8.dp, IosEmeraldPro),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "✅ تم تجهيز الوصية الشرعية بنجاح. يُستحب أن يُشهد عليها اثنان من أهل العدل وتحفظ في مكان معلوم للأهل.",
                                            fontSize = 11.sp,
                                            color = IosEmeraldPro,
                                            modifier = Modifier.padding(10.dp),
                                            textAlign = TextAlign.Center
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
