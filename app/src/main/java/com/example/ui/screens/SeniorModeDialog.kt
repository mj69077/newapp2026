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
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SeniorModeDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var seniorTab by remember { mutableIntStateOf(0) } // 0: مسبحة عملاقة, 1: أذكار بخط كبير, 2: مواقيت الصلاة
    var tasbihCount by remember { mutableIntStateOf(0) }
    var selectedZikr by remember { mutableStateOf("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ") }

    val prayerData by viewModel.prayerTimes.collectAsState()

    fun performHaptic() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(45)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(26.dp)),
            color = Color(0xFF000000), // Pure OLED Black for maximum contrast
            border = BorderStroke(2.dp, Color(0xFFFFD54F))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Exit Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF261818)),
                        border = BorderStroke(1.dp, Color(0xFFE57373)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFFFCDD2), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("خروج من الوضع", color = Color(0xFFFFCDD2), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E281F),
                        border = BorderStroke(1.dp, Color(0xFFFFD54F))
                    ) {
                        Text(
                            text = "👓 وضع كبار السن وضعاف البصر",
                            color = Color(0xFFFFD54F),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Big Mode Navigation Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF141F1A))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf("المسبحة الكبرى", "أذكار واضحة", "مواقيت اليوم")
                    tabs.forEachIndexed { idx, title ->
                        Button(
                            onClick = {
                                seniorTab = idx
                                performHaptic()
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (seniorTab == idx) Color(0xFFFFD54F) else Color.Transparent
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = title,
                                color = if (seniorTab == idx) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (seniorTab) {
                    0 -> {
                        // Giant Tasbih Screen
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Zikr Selector
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF12241C),
                                border = BorderStroke(1.5.dp, Color(0xFFFFD54F)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = selectedZikr,
                                    fontSize = 22.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFF9C4),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            // Giant Tap Button
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0F3A2A),
                                border = BorderStroke(4.dp, Color(0xFFFFD54F)),
                                modifier = Modifier
                                    .size(240.dp)
                                    .clickable {
                                        tasbihCount += 1
                                        performHaptic()
                                    }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "$tasbihCount",
                                        fontSize = 58.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFFD54F)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "المس هنا للتسبيح",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Quick Reset
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        selectedZikr = when (selectedZikr) {
                                            "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ" -> "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
                                            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ" -> "لَا إِلَٰهَ إِلَّا اللَّهُ"
                                            "لَا إِلَٰهَ إِلَّا اللَّهُ" -> "اللَّهُ أَكْبَرُ كَبِيرًا"
                                            "اللَّهُ أَكْبَرُ كَبِيرًا" -> "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ"
                                            else -> "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ"
                                        }
                                        performHaptic()
                                    },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B382B)),
                                    border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("تغيير الذكر 🔁", color = Color(0xFFFFD54F), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        tasbihCount = 0
                                        performHaptic()
                                    },
                                    modifier = Modifier.weight(0.7f).height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF331616)),
                                    border = BorderStroke(1.dp, Color(0xFFEF5350)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("تصفير ↺", color = Color(0xFFFFCDD2), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    1 -> {
                        // Large Text Athkar
                        val bigAthkar = listOf(
                            "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
                            "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ. (٣ مرات)",
                            "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا.",
                            "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ.",
                            "قُلْ هُوَ اللَّهُ أَحَدٌ ﴿١﴾ اللَّهُ الصَّمَدُ ﴿٢﴾ لَمْ يَلِدْ وَلَمْ يُولَدْ ﴿٣﴾ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ ﴿٤﴾"
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(bigAthkar) { zikr ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF0D1F17),
                                    border = BorderStroke(1.5.dp, Color(0xFFFFD54F)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = zikr,
                                        fontSize = 21.sp,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 34.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        // High Visibility Prayer Times
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF142C21),
                                    border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "الصلاة القادمة: صلاة ${prayerData.nextPrayer}",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD54F)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = prayerData.timeRemaining,
                                            fontSize = 18.sp,
                                            color = Color(0xFFE0F2F1)
                                        )
                                    }
                                }
                            }

                            val prayers = listOf(
                                "الفجر" to prayerData.fajr,
                                "الشروق" to prayerData.sunrise,
                                "الظهر" to prayerData.dhuhr,
                                "العصر" to prayerData.asr,
                                "المغرب" to prayerData.maghrib,
                                "العشاء" to prayerData.isha
                            )

                            items(prayers) { (name, time) ->
                                val isNext = prayerData.nextPrayer.contains(name)
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isNext) Color(0xFF1F4432) else Color(0xFF0E1C15),
                                    border = BorderStroke(if (isNext) 2.dp else 1.dp, if (isNext) Color(0xFFFFD54F) else Color(0x66FFD54F)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = name,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isNext) Color(0xFFFFD54F) else Color.White
                                        )
                                        Text(
                                            text = time,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isNext) Color(0xFFFFD54F) else Color(0xFFC8E6C9)
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
