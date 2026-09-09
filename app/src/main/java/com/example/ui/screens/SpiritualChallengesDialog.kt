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

data class SpiritualChallenge(
    val id: String,
    val title: String,
    val description: String,
    val hadithProof: String,
    val totalDays: Int,
    var currentDay: Int,
    val icon: String,
    val category: String, // قيام، صيام، ذكر، قرآن، صلة
    var isCompleted: Boolean = false
)

data class SpiritualBadge(
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val dateUnlocked: String
)

@Composable
fun SpiritualChallengesDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: التحديات الجارية, 1: سلاسل الالتزام, 2: شهادات وأوسمة

    var challenges by remember {
        mutableStateOf(
            listOf(
                SpiritualChallenge(
                    id = "fajr_7",
                    title = "تحدي فرسان الفجر 🌅",
                    description = "أداء صلاة الفجر في وقتها وحضور تكبيرة الإحرام لمدة 7 أيام متتالية.",
                    hadithProof = "«مَن صَلَّى الصُّبْحَ فَهُوَ فِي ذِمَّةِ اللهِ» (رواه مسلم)",
                    totalDays = 7,
                    currentDay = 4,
                    icon = "🕌",
                    category = "صلاة"
                ),
                SpiritualChallenge(
                    id = "qiyam_10",
                    title = "تحدي قيام الليل والمستغفرين 🌙",
                    description = "ركعتان خفيفتان قبل الفجر مع ركعة الوتر والاستغفار في وقت السحر.",
                    hadithProof = "«أَفْضَلُ الصَّلَاةِ بَعْدَ الصَّلَاةِ الْمَكْتُوبَةِ: الصَّلَاةُ فِي جَوْفِ اللَّيْلِ»",
                    totalDays = 10,
                    currentDay = 6,
                    icon = "✨",
                    category = "قيام"
                ),
                SpiritualChallenge(
                    id = "dhikr_1000",
                    title = "تحدي ألف تسبيحة واستغفار 📿",
                    description = "المحافظة على 1000 ذكر يومياً (سبحان الله وبحمده، أستغفر الله، الصلاة على النبي).",
                    hadithProof = "«أَيَعْجِزُ أَحَدُكُمْ أَنْ يَكْسِبَ كُلَّ يَوْمٍ أَلْفَ حَسَنَةٍ؟»",
                    totalDays = 14,
                    currentDay = 11,
                    icon = "💎",
                    category = "ذكر"
                ),
                SpiritualChallenge(
                    id = "quran_tadabbur",
                    title = "تحدي الختمة التدبرية الشهرية 📖",
                    description = "قراءة جزء يومياً مع تدوين فائدة تدبرية واحدة والعمل بآية.",
                    hadithProof = "«أَفَلَا يَتَدَبَّرُونَ الْقُرْآنَ أَمْ عَلَىٰ قُلُوبٍ أَقْفَالُهَا»",
                    totalDays = 30,
                    currentDay = 18,
                    icon = "🌱",
                    category = "قرآن"
                ),
                SpiritualChallenge(
                    id = "sunnah_fasting",
                    title = "تحدي صيام الإثنين والخميس 🍃",
                    description = "صيام يومين كل أسبوع إحياءً لسنة الحبيب المصطفى ﷺ.",
                    hadithProof = "«تُعْرَضُ الْأَعْمَالُ يَوْمَ الِاثْنَيْنِ وَالْخَمِيسِ، فَأُحِبُّ أَنْ يُعْرَضَ عَمَلِي وَأَنَا صَائِمٌ»",
                    totalDays = 8,
                    currentDay = 5,
                    icon = "🌴",
                    category = "صيام"
                ),
                SpiritualChallenge(
                    id = "silat_rahim",
                    title = "تحدي صلة الرحم وإدخال السرور 🤝",
                    description = "الاتصال بأحد الأقارب أو زيارة مريض أو تفريج كربة مسلم يومياً لمدة أسبوع.",
                    hadithProof = "«مَنْ سَرَّهُ أَنْ يُبْسَطَ لَهُ فِي رِزْقِهِ وَيُنْسَأَ لَهُ فِي أَثَرِهِ فَلْيَصِلْ رَحِمَهُ»",
                    totalDays = 7,
                    currentDay = 7,
                    icon = "❤️",
                    category = "إحسان",
                    isCompleted = true
                )
            )
        )
    }

    val badges = remember {
        listOf(
            SpiritualBadge("فارس الفجر الأسبوعي", "أكملت 7 أيام من صلاة الفجر في وقتها", "🌅", true, "15 محرم"),
            SpiritualBadge("المستغفر بالأسحار", "قمت الليل 10 ليالٍ متتالية", "🌙", true, "22 صفر"),
            SpiritualBadge("سفير الباقيات الصالحات", "أتممت 14,000 تسبيحة في التحدي", "📿", true, "5 ربيع الأول"),
            SpiritualBadge("الصائم القانت", "أتممت صيام 8 أيام تطوع في شهر", "🍃", false, "قريباً"),
            SpiritualBadge("صاحب القرآن", "أتممت الختمة التدبرية الكاملة", "📖", false, "قريباً")
        )
    }

    var showCompletionCelebration by remember { mutableStateOf<SpiritualChallenge?>(null) }

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
                            text = "🔥 التحديات الإيمانية وبناء العادات",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "سلاسل الالتزام ومحاسبة النفس على الطاعات",
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
                        Text("🏆", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("التحديات الجارية", "سلاسل الالتزام", "الأوسمة والشهادات"),
                    selectedIndex = selectedTab,
                    onItemSelected = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedTab) {
                    0 -> {
                        // Challenges List
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(challenges) { ch ->
                                GlassCard {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(ch.icon, fontSize = 24.sp)
                                                Column {
                                                    Text(
                                                        text = ch.title,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp,
                                                        color = Color.White
                                                    )
                                                    Text(
                                                        text = "الفئة: ${ch.category} • الهدف: ${ch.totalDays} يوماً",
                                                        fontSize = 11.sp,
                                                        color = IosTextSecondary
                                                    )
                                                }
                                            }

                                            if (ch.currentDay >= ch.totalDays) {
                                                Surface(
                                                    color = IosEmeraldPro.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(12.dp),
                                                    border = BorderStroke(1.dp, IosEmeraldPro)
                                                ) {
                                                    Text(
                                                        "مكتمل ⭐️",
                                                        color = IosEmeraldPro,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            } else {
                                                Surface(
                                                    color = IosGoldApple.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(12.dp),
                                                    border = BorderStroke(0.8.dp, IosGoldApple)
                                                ) {
                                                    Text(
                                                        "اليوم ${ch.currentDay} من ${ch.totalDays}",
                                                        color = IosGoldApple,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = ch.description,
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.85f),
                                            lineHeight = 17.sp
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Surface(
                                            color = Color(0x1AFFFFFF),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = ch.hadithProof,
                                                fontSize = 11.sp,
                                                color = IosGoldApple.copy(alpha = 0.9f),
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Day Circles Progress
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            for (d in 1..ch.totalDays.coerceAtMost(10)) {
                                                val isPassed = d <= ch.currentDay
                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (isPassed) IosEmeraldPro else Color(0x33FFFFFF)
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (isPassed) {
                                                        Icon(
                                                            Icons.Default.Check,
                                                            contentDescription = null,
                                                            tint = Color.Black,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    } else {
                                                        Text(
                                                            "$d",
                                                            fontSize = 10.sp,
                                                            color = Color.White.copy(alpha = 0.6f)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Check-in button
                                        Button(
                                            onClick = {
                                                if (ch.currentDay < ch.totalDays) {
                                                    val updated = challenges.map { item ->
                                                        if (item.id == ch.id) {
                                                            val newDay = item.currentDay + 1
                                                            val done = newDay >= item.totalDays
                                                            val res = item.copy(currentDay = newDay, isCompleted = done)
                                                            if (done) showCompletionCelebration = res
                                                            res
                                                        } else item
                                                    }
                                                    challenges = updated
                                                }
                                            },
                                            enabled = ch.currentDay < ch.totalDays,
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (ch.currentDay < ch.totalDays) IosEmeraldPro else Color.Gray,
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Icon(
                                                if (ch.currentDay < ch.totalDays) Icons.Default.CheckCircle else Icons.Default.Star,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                if (ch.currentDay < ch.totalDays) "تسجيل إنجاز اليوم (${ch.currentDay + 1})" else "تم إتمام التحدي بنجاح 🏅",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Streaks Tab
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            item {
                                GlassCard {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("🔥", fontSize = 48.sp)
                                        Text(
                                            "سلسلة الالتزام الحالية",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            "21 يوماً متواصلاً في طاعة الله",
                                            fontSize = 14.sp,
                                            color = IosGoldApple,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            "«أَحَبُّ الأَعْمَالِ إِلَى اللهِ أَدْوَمُهَا وَإِنْ قَلَّ»",
                                            fontSize = 12.sp,
                                            color = IosTextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            item {
                                Text(
                                    "إحصائيات العادات الإيمانية:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IosGoldApple
                                )
                            }

                            val streaks = listOf(
                                Triple("صلاة الفجر حاضراً", 21, "🌅"),
                                Triple("أذكار الصباح والمساء", 35, "📿"),
                                Triple("ورد تلاوة القرآن", 18, "📖"),
                                Triple("صلاة الوتر", 14, "✨"),
                                Triple("ركعتا الضحى", 9, "☀️")
                            )

                            items(streaks) { (title, count, icon) ->
                                GlassCard {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(icon, fontSize = 24.sp)
                                            Column {
                                                Text(
                                                    title,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    "لم تنقطع السلسلة بفضل الله",
                                                    fontSize = 11.sp,
                                                    color = IosTextSecondary
                                                )
                                            }
                                        }

                                        Surface(
                                            color = Color(0x33F5D372),
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(1.dp, IosGoldApple)
                                        ) {
                                            Text(
                                                "$count يوم 🔥",
                                                color = IosGoldApple,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Badges & Certificates
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(badges) { b ->
                                GlassCard {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .background(
                                                        if (b.isUnlocked) IosGoldApple.copy(alpha = 0.2f) else Color(0x22FFFFFF),
                                                        CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(b.icon, fontSize = 22.sp)
                                            }

                                            Column {
                                                Text(
                                                    b.title,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (b.isUnlocked) Color.White else Color.Gray,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    b.description,
                                                    fontSize = 11.sp,
                                                    color = IosTextSecondary
                                                )
                                            }
                                        }

                                        if (b.isUnlocked) {
                                            Surface(
                                                color = IosEmeraldPro.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(10.dp),
                                                border = BorderStroke(0.8.dp, IosEmeraldPro)
                                            ) {
                                                Text(
                                                    "نيل في ${b.dateUnlocked} ✨",
                                                    color = IosEmeraldPro,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        } else {
                                            Text(
                                                "🔒 قيد الإنجاز",
                                                color = Color.Gray,
                                                fontSize = 11.sp
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

    // Celebration modal when completing a challenge
    showCompletionCelebration?.let { completedChallenge ->
        Dialog(
            onDismissRequest = { showCompletionCelebration = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(28.dp)),
                color = IosBgBlack,
                border = BorderStroke(1.5.dp, IosGoldApple)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉 ⭐️ 🏆", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "هنيئاً لك إتمام التحدي المبارك!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = IosGoldApple,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "أتممت بنجاح: ${completedChallenge.title}",
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0x22F5D372),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "نسأل الله تعالى أن يتقبل منك وأن يثبتك على طاعته ويجعل هذا العمل في موازين حسناتك.",
                            fontSize = 12.sp,
                            color = IosGoldApple,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showCompletionCelebration = null },
                        colors = ButtonDefaults.buttonColors(containerColor = IosGoldApple, contentColor = Color.Black),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("الحمد لله وشكراً", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
