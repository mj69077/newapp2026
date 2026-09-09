package com.example.ui.screens

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
fun TravelerCompanionDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: رخص وأحكام السفر, 1: حاسبة مسافة القصر, 2: أدعية السفر المأثورة

    // Travel Distance Calculator State
    var destinationName by remember { mutableStateOf("") }
    var distanceKmText by remember { mutableStateOf("120") }
    var stayDurationDaysText by remember { mutableStateOf("3") }

    val distanceKm = distanceKmText.toDoubleOrNull() ?: 0.0
    val stayDays = stayDurationDaysText.toIntOrNull() ?: 0
    val isEligibleForQasr = distanceKm >= 80.0 && stayDays <= 4

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
                            text = "✈️ رفيق المسافر وأحكام الرخص الشرعية",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "حاسبة مسافة القصر والجمع وأدعية السفر الصحيحة",
                            fontSize = 11.sp,
                            color = IosTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(IosEmeraldPro.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧭", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("رخص السفر", "حاسبة القصر", "أدعية السفر"),
                    selectedIndex = selectedTab,
                    onItemSelected = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedTab) {
                    0 -> {
                        // Travel Fiqh Concessions
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            "رحمة الله بالمسافر وتيسير العبادات 🌿",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            "قال رسول الله ﷺ: «إِنَّ اللَّهَ يُحِبُّ أَنْ تُؤْتَى رُخَصُهُ، كَمَا يَكْرَهُ أَنْ تُؤْتَى مَعْصِيَتُهُ». السفر قطعة من العذاب، ولذلك شرع الله تعالى فيه رخصاً تخفيفاً ورفقاً بالمؤمن.",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.9f),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            val concessions = listOf(
                                Triple(
                                    "قصر الصلاة الرباعية",
                                    "الظهر، العصر، العشاء تصلى ركعتين",
                                    "السنة المؤكدة في حق المسافر أن يصلي الصلوات الرباعية ركعتين ما دام في سفره، أما صلاة الصبح والمغرب فلا قصر فيهما إجماعاً."
                                ),
                                Triple(
                                    "الجمع بين الصلاتين",
                                    "جمع تقديم أو تأخير بعذر السفر",
                                    "يجوز للمسافر أن يجمع بين الظهر والعصر، وبين المغرب والعشاء، في وقت إحداهما تقديماً أو تأخيراً بحسب الأيسر له ولسيره."
                                ),
                                Triple(
                                    "المسح على الخفين والجوربين",
                                    "ثلاثة أيام ولياليها (72 ساعة)",
                                    "يمسح المسافر على خفيه أو جوربيه الطاهرين ثلاثة أيام بلياليهن تبدأ من أول مسح بعد الحدث، بخلاف المقيم الذي يمسح يوماً وليلة."
                                ),
                                Triple(
                                    "سقوط السنن الرواتب",
                                    "مع بقاء سنة الفجر وصلاة الوتر",
                                    "كان من هدي النبي ﷺ في السفر ترك السنن الرواتب، والمحافظة فقط على ركعتي سنة الفجر القبلية وعلى صلاة الوتر وقيام الليل."
                                ),
                                Triple(
                                    "رخصة الفطر في رمضان",
                                    "الإفطار للمسافر وقضاء الأيام بعد ذلك",
                                    "لقوله تعالى: {فَمَن كَانَ مِنكُم مَّرِيضًا أَوْ عَلَىٰ سَفَرٍ فَعِدَّةٌ مِّنْ أَيَّامٍ أُخَرَ}. ويجوز الصيام إن لم يجد مشقة."
                                ),
                                Triple(
                                    "الصلاة في الطائرة والقطار",
                                    "استقبال القبلة وأداء الأركان قدر الاستطاعة",
                                    "إذا خشي خروج الوقت صلى على حاله جالساً إن عجز عن القيام مستقبلاً القبلة جهده، لقوله تعالى: {فَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ}."
                                )
                            )

                            items(concessions.size) { i ->
                                val (title, subtitle, detail) = concessions[i]
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(26.dp)
                                                    .background(IosEmeraldPro, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("${i + 1}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            }
                                            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                                            Text("• $subtitle", fontSize = 11.sp, color = IosGoldApple)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(detail, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 18.sp)
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Distance & Duration Calculator
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            item {
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            "📏 ضابط مسافة السفر والمدة المعتبرة",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "حدد جمهور الفقهاء مسافة القصر بأربعة بُرُد (ما يقارب 80 - 85 كيلومتراً) بمفارقة بنيان وعمران بلد الإقامة، وتستمر الرخص إذا كانت نية الإقامة في الوجهة 4 أيام أو أقل.",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.85f),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            item {
                                Text("اسم الوجهة أو مدينة السفر:", fontSize = 12.sp, color = IosTextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = destinationName,
                                    onValueChange = { destinationName = it },
                                    placeholder = { Text("مثال: من الرياض إلى الدمام", color = Color.Gray, fontSize = 12.sp) },
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
                                Text("المسافة التقديرية بين المدينتين (كم):", fontSize = 12.sp, color = IosTextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = distanceKmText,
                                    onValueChange = { distanceKmText = it },
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
                                Text("عدد أيام الإقامة المتوقعة في الوجهة:", fontSize = 12.sp, color = IosTextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = stayDurationDaysText,
                                    onValueChange = { stayDurationDaysText = it },
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
                                GlassCard {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (isEligibleForQasr) {
                                            Text("✅ تنطبق عليك رخص السفر الشرعية", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = IosEmeraldPro)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                "بما أن المسافة (${distanceKm.toInt()} كم) تتجاوز مسافة القصر المعتبرة (80 كم) والمدة المنوية ($stayDays أيام) لا تتجاوز 4 أيام، يُشرع لك القصر والجمع والمسح 3 أيام.",
                                                fontSize = 12.sp,
                                                color = Color.White.copy(alpha = 0.9f),
                                                textAlign = TextAlign.Center,
                                                lineHeight = 18.sp
                                            )
                                        } else {
                                            Text("ℹ️ حكم الإقامة والإتمام", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = IosGoldApple)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            val reason = if (distanceKm < 80.0) {
                                                "المسافة أقل من مسافة السفر المعتبرة شرعاً (80 كم)، لذا تأخذ حكم الحاضر وتتم الصلاة بلا قصر."
                                            } else {
                                                "المسافة تبلغ مسافة السفر، لكن نيتك الإقامة لأكثر من 4 أيام ($stayDays أيام) تجعلك تأخذ حكم المقيم عند وصولك عند جمهور الفقهاء فتتم الصلاة."
                                            }
                                            Text(
                                                reason,
                                                fontSize = 12.sp,
                                                color = Color.White.copy(alpha = 0.9f),
                                                textAlign = TextAlign.Center,
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Travel Supplications (Ad'iyah)
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val travelDuas = listOf(
                                Pair(
                                    "دعاء ركوب الدابة أو وسيلة السفر",
                                    "«سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ * وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ»"
                                ),
                                Pair(
                                    "دعاء السفر النبوي الجامع",
                                    "«اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى، وَمِنَ الْعَمَلِ مَا تَرْضَى، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا، وَاطْوِ عَنَّا بُعْدَهُ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ، وَالْخَلِيفَةُ فِي الأَهْلِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ وَعْثَاءِ السَّفَرِ، وَكَآبَةِ الْمَنْظَرِ، وَسُوءِ الْمُنْقَلَبِ فِي الْمَالِ وَالأَهْلِ»."
                                ),
                                Pair(
                                    "دعاء العودة من السفر",
                                    "«آيِبُونَ، تَائِبُونَ، عَابِدُونَ، لِرَبِّنَا حَامِدُونَ»"
                                ),
                                Pair(
                                    "دعاء نزول المنزل أو الفندق",
                                    "«أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ» (من قالها لم يضره شيء حتى يرتحل من منزله ذلك)."
                                ),
                                Pair(
                                    "توديع المسافر والمقيم",
                                    "المقيم يقول للمسافر: «أَسْتَوْدِعُ اللَّهَ دِينَكَ، وَأَمَانَتَكَ، وَخَوَاتِيمَ عَمَلِكَ».\nوالمسافر يقول له: «أَسْتَوْدِعُكُمُ اللَّهَ الَّذِي لَا تَضِيعُ وَدَائِعُهُ»."
                                )
                            )

                            items(travelDuas.size) { index ->
                                val (title, dua) = travelDuas[index]
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = IosGoldApple
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Surface(
                                            color = Color(0x1AFFFFFF),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = dua,
                                                fontSize = 13.sp,
                                                color = Color.White,
                                                lineHeight = 20.sp,
                                                modifier = Modifier.padding(10.dp)
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
