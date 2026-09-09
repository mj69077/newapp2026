package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.ui.text.font.FontFamily
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

data class LapDua(
    val lapNumber: Int,
    val title: String,
    val duaText: String,
    val tip: String
)

object HajjUmrahData {
    val tawafDuas = listOf(
        LapDua(1, "الشوط الأول", "بِسْمِ اللَّهِ وَاللَّهُ أَكْبَرُ، اللَّهُمَّ إِيمَانًا بِكَ وَتَصْدِيقًا بِكِتَابِكَ وَوَفَاءً بِعَهْدِكَ وَاتِّبَاعًا لِسُنَّةِ نَبِيِّكَ مُحَمَّدٍ ﷺ.", "ابدأ بمحاذاة الحجر الأسود رافعاً يدك اليمنى بالتكبير."),
        LapDua(2, "الشوط الثاني", "اللَّهُمَّ إِنَّ هَذَا الْبَيْتَ بَيْتُكَ، وَالْحَرَمَ حَرَمُكَ، وَالأَمْنَ أَمْنُكَ، اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ.", "تستحب الرمل (الإسراع في المشي) للرجال في الأشواط الثلاثة الأولى."),
        LapDua(3, "الشوط الثالث", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الشَّكِّ وَالشِّرْكِ وَالشِّقَاقِ.", "أكثر من الدعاء بين الركن اليماني والحجر الأسود."),
        LapDua(4, "الشوط الرابع", "اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا، وَذَنْبًا مَغْفُورًا، وَسَعْيًا مَشْكُورًا، وَتِجَارَةً لَنْ تَبُورَ يَا عَزِيزُ يَا غَفُورُ.", "سل الله من خيري الدنيا والآخرة لنفسك ولوالديك."),
        LapDua(5, "الشوط الخامس", "اللَّهُمَّ أَظِلَّنِي تَحْتَ ظِلِّ عَرْشِكَ يَوْمَ لَا ظِلَّ إِلَّا ظِلُّكَ، وَاسْقِنِي مِنْ حَوْضِ نَبِيِّكَ مُحَمَّدٍ ﷺ شَرْبَةً هَنِيئَةً لَا أَظْمَأُ بَعْدَهَا أَبَدًا.", "المشي بسكينة ووقار دون مزاحمة الطائفين."),
        LapDua(6, "الشوط السادس", "اللَّهُمَّ إِنَّ لَكَ عَلَيَّ حُقُوقًا كَثِيرَةً فِيمَا بَيْنِي وَبَيْنَكَ، وَحُقُوقًا فِيمَا بَيْنِي وَبَيْنَ خَلْقِكَ، فَاغْفِرْ لِي مَا كَانَ لَكَ، وَتَحَمَّلْ عَنِّي مَا كَانَ لِخَلْقِكَ.", "الاستغفار الصادق والعزم على التوبة."),
        LapDua(7, "الشوط السابع", "اللَّهُمَّ إِنِّي أَسْأَلُكَ إِيمَانًا دَائِمًا، وَقَلْبًا خَاشِعًا، وَعِلْمًا نَافِعًا، وَيَقِينًا صَادِقًا، وَدِينًا قَيِّمًا، وَأَسْأَلُكَ الْعَافِيَةَ مِنْ كُلِّ بَلِيَّةٍ.", "عند ختام الشوط السابع، توجه لصلاة ركعتين خلف مقام إبراهيم إن تيسر.")
    )

    val saiDuas = listOf(
        LapDua(1, "الشوط الأول (من الصفا إلى المروة)", "﴿إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ﴾، أَبْدَأُ بِمَا بَدَأَ اللَّهُ بِهِ. لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.", "استقبل الكعبة عند الصفا وكبّر ثلاثاً وادع بما شئت."),
        LapDua(2, "الشوط الثاني (من المروة إلى الصفا)", "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ، أَنْجَزَ وَعْدَهُ، وَنَصَرَ عَبْدَهُ، وَهَزَمَ الْأَحْزَابَ وَحْدَهُ. رَبِّ اغْفِرْ وَارْحَمْ وَأَنْتَ الْأَعَزُّ الْأَكْرَمُ.", "يُسن للرجال الهرولة بين العلمين الأخضرين."),
        LapDua(3, "الشوط الثالث (من الصفا إلى المروة)", "اللَّهُمَّ إِنِّي أَسْأَلُكَ مُوجِبَاتِ رَحْمَتِكَ، وَعَزَائِمَ مَغْفِرَتِكَ، وَالسَّلَامَةَ مِنْ كُلِّ إِثْمٍ، وَالْغَنِيمَةَ مِنْ كُلِّ بِرٍّ، وَالْفَوْزَ بِالْجَنَّةِ، وَالنَّجَاةَ مِنَ النَّارِ.", "الإكثار من الدعاء والذكر طوال المسار."),
        LapDua(4, "الشوط الرابع (من المروة إلى الصفا)", "رَبَّنَا تَقَبَّلْ مِنَّا إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ، وَتُبْ عَلَيْنَا إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ. رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ.", "تذكر سعي أمنا هاجر وتوكلها العظيم على الله."),
        LapDua(5, "الشوط الخامس (من الصفا إلى المروة)", "اللَّهُمَّ حَبِّبْ إِلَيْنَا الْإِيمَانَ وَزَيِّنْهُ فِي قُلُوبِنَا، وَكَرِّهْ إِلَيْنَا الْكُفْرَ وَالْفُسُوقَ وَالْعِصْيَانَ، وَاجْعَلْنَا مِنَ الرَّاشِدِينَ.", "الدعاء لصلاح الأهل والذرية والمسلمين."),
        LapDua(6, "الشوط السادس (من المروة إلى الصفا)", "اللَّهُمَّ إِنِّي أَسْأَلُكَ عَيْشَةً نَقِيَّةً، وَمِيتَةً سَوِيَّةً، وَمَرَدًّا غَيْرَ مُخْزٍ وَلَا فَاضِحٍ. اللَّهُمَّ ارْزُقْنَا حُسْنَ الْخَاتِمَةِ.", "الخشوع والسكينة أثناء المسير."),
        LapDua(7, "الشوط السابع (من الصفا إلى المروة)", "اللَّهُمَّ اخْتِمْ لَنَا بِخَيْرٍ، وَاجْعَلْ عَاقِبَةَ أَمْرِنَا إِلَى رُشْدٍ، وَتَقَبَّلْ عُمْرَتَنَا وَسَعْيَنَا، وَاغْفِرْ لَنَا وَلِوَالِدَيْنَا وَلِجَمِيعِ الْمُسْلِمِينَ.", "ينتهي السعي عند المروة، وبعده الحلق أو التقصير للتحلل.")
    )
}

@Composable
fun UmrahHajjGuideDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableIntStateOf(0) } // 0: عداد الطواف, 1: عداد السعي, 2: مناسك العمرة, 3: مناسك الحج

    var tawafCurrentLap by remember { mutableIntStateOf(1) }
    var saiCurrentLap by remember { mutableIntStateOf(1) }

    fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(50)
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

                // iOS Header
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
                            text = "🕋 رفيق الحج والعمرة التفاعلي",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "عداد الأشواط الذكي والأدعية المأثورة",
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
                        Icon(Icons.Default.Mosque, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Apple Segmented Control
                IosSegmentedControl(
                    items = listOf("أشواط الطواف", "أشواط السعي", "دليل العمرة", "مناسك الحج"),
                    selectedIndex = activeTab,
                    onSelectIndex = {
                        activeTab = it
                        vibrate()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (activeTab) {
                    0 -> {
                        // Tawaf Counter
                        val currentDua = HajjUmrahData.tawafDuas.getOrElse(tawafCurrentLap - 1) { HajjUmrahData.tawafDuas.last() }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Circular Lap Progress Ring
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x1A2DD4BF))
                                    .clickable {
                                        if (tawafCurrentLap < 7) {
                                            tawafCurrentLap += 1
                                        } else {
                                            tawafCurrentLap = 1
                                        }
                                        vibrate()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$tawafCurrentLap",
                                        fontSize = 52.sp,
                                        fontWeight = FontWeight.Black,
                                        color = IosGoldApple
                                    )
                                    Text(
                                        text = "من ٧ أشواط طواف",
                                        fontSize = 12.sp,
                                        color = IosEmeraldPro,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Current Dua Glass Card
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
                                            text = currentDua.title,
                                            color = IosGoldApple,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0x332DD4BF)
                                        ) {
                                            Text(
                                                text = "مستحب مأثور",
                                                color = IosEmeraldPro,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = currentDua.duaText,
                                        fontSize = 15.5.sp,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 25.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0x33000000), RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Info, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = currentDua.tip,
                                            color = IosTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            // Bottom Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        tawafCurrentLap = 1
                                        vibrate()
                                    },
                                    modifier = Modifier.weight(0.8f).height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF261818))
                                ) {
                                    Text("تصفير ↺", color = Color(0xFFFFCDD2), fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (tawafCurrentLap < 7) {
                                            tawafCurrentLap += 1
                                        } else {
                                            tawafCurrentLap = 1
                                        }
                                        vibrate()
                                    },
                                    modifier = Modifier.weight(1.4f).height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IosGoldApple)
                                ) {
                                    Text(
                                        text = if (tawafCurrentLap < 7) "الشوط التالي (${tawafCurrentLap + 1}) ⬅" else "اكتمل الطواف مبارك! 🎉",
                                        color = Color(0xFF040A07),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        // Sa'i Counter
                        val currentDua = HajjUmrahData.saiDuas.getOrElse(saiCurrentLap - 1) { HajjUmrahData.saiDuas.last() }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x1AF5D372))
                                    .clickable {
                                        if (saiCurrentLap < 7) saiCurrentLap += 1 else saiCurrentLap = 1
                                        vibrate()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$saiCurrentLap",
                                        fontSize = 52.sp,
                                        fontWeight = FontWeight.Black,
                                        color = IosGoldApple
                                    )
                                    Text(
                                        text = "من ٧ أشواط سعي",
                                        fontSize = 12.sp,
                                        color = IosGoldLight,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            GlassCard(
                                backgroundColor = IosCardGlass,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = currentDua.title,
                                        color = IosGoldApple,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = currentDua.duaText,
                                        fontSize = 15.5.sp,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 25.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0x33000000), RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Info, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = currentDua.tip,
                                            color = IosTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        saiCurrentLap = 1
                                        vibrate()
                                    },
                                    modifier = Modifier.weight(0.8f).height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF261818))
                                ) {
                                    Text("تصفير ↺", color = Color(0xFFFFCDD2), fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (saiCurrentLap < 7) saiCurrentLap += 1 else saiCurrentLap = 1
                                        vibrate()
                                    },
                                    modifier = Modifier.weight(1.4f).height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IosGoldApple)
                                ) {
                                    Text(
                                        text = if (saiCurrentLap < 7) "الشوط التالي (${saiCurrentLap + 1}) ⬅" else "اكتمل السعي بحمد الله! 🎉",
                                        color = Color(0xFF040A07),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        // Umrah Steps Guide
                        val umrahSteps = listOf(
                            "١. الإحرام من الميقات" to "الاغتسال والتطيب ولبس إزار ورداء أبيضين، والتلبية: «لَبَّيْكَ اللَّهُمَّ عُمْرَةً».",
                            "٢. طواف القدوم ٧ أشواط" to "البدء من محاذاة الحجر الأسود جاعلاً الكعبة عن يسارك، والرمل في أول ٣ أشواط للرجال.",
                            "٣. صلاة ركعتين خلف المقام" to "قراءة سورة الكافرون في الركعة الأولى وسورة الإخلاص في الركعة الثانية والشرب من زمزم.",
                            "٤. السعي بين الصفا والمروة ٧ أشواط" to "البدء بالصفا والختام بالمروة، مع الهرولة بين الميلين الأخضرين للرجال.",
                            "٥. الحلق أو التقصير" to "الحلق أفضل للرجال، أو التقصير من جميع جهات الرأس، وتقصير قدر أنملة للنساء."
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(umrahSteps.size) { idx ->
                                val (title, desc) = umrahSteps[idx]
                                GlassCard(
                                    backgroundColor = IosCardGlass,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(title, color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(desc, color = IosTextPrimary, fontSize = 12.5.sp, lineHeight = 18.sp)
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        // Hajj Days Guide
                        val hajjDays = listOf(
                            "يوم التروية (٨ ذو الحجة)" to "الإحرام للحج من مكة والتوجه إلى منى للمبيت بها وصلاة الصلوات قصراً بلا جمع.",
                            "يوم عرفة (٩ ذو الحجة)" to "ركن الحج الأعظم، الوقوف بعرفة بعد الزوال إلى الغروب، والإكثار من الدعاء والذكر.",
                            "المزدلفة (ليلة ١٠ ذو الحجة)" to "النفرة بعد الغروب إلى مزدلفة والمبيت بها وصلاة المغرب والعشاء جمع تأخير ولقط الحصى.",
                            "يوم النحر (١٠ ذو الحجة - العيد)" to "رمي جمرة العقبة الكبرى بـ ٧ حصيات، ثم ذبح الهدي، ثم الحلق والتحلل، ثم طواف الإفاضة.",
                            "أيام التشريق (١١، ١٢، ١٣ ذو الحجة)" to "المبيت بمنى ورمي الجمرات الثلاث (الصغرى، الوسطى، والكبرى)، ثم طواف الوداع."
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(hajjDays.size) { idx ->
                                val (title, desc) = hajjDays[idx]
                                GlassCard(
                                    backgroundColor = IosCardGlass,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(title, color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(desc, color = IosTextPrimary, fontSize = 12.5.sp, lineHeight = 18.sp)
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
