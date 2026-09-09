package com.example.ui.screens

import android.content.Context
import android.speech.tts.TextToSpeech
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
import com.example.notification.VoiceDhikrReceiver
import com.example.ui.components.GlassCard
import com.example.ui.components.IosGrabberHandle
import com.example.ui.components.IosSegmentedControl
import com.example.ui.theme.*
import java.util.*

@Composable
fun VoiceDhikrDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("voice_dhikr_prefs", Context.MODE_PRIVATE) }

    var isEnabled by remember { mutableStateOf(prefs.getBoolean("enabled", false)) }
    var selectedIntervalMinutes by remember { mutableIntStateOf(prefs.getInt("interval_minutes", 30)) }
    var selectedPhraseIndex by remember { mutableIntStateOf(prefs.getInt("phrase_index", 0)) }
    var quietHoursEnabled by remember { mutableStateOf(prefs.getBoolean("quiet_enabled", true)) }

    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        ttsEngine = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsEngine?.language = Locale("ar")
                ttsEngine?.setSpeechRate(0.85f)
                isTtsReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsEngine?.stop()
            ttsEngine?.shutdown()
        }
    }

    val phrases = listOf(
        "🔀 تناوب ذكي (جميع الأذكار بالترتيب)",
        "🌸 «اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ»",
        "💎 «سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ»",
        "🌟 «لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ»",
        "🤲 «أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ»",
        "🛡️ «لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ»"
    )

    fun speakSample(text: String) {
        val cleanText = text.substringAfter("«").substringBefore("»").ifBlank { "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ" }
        ttsEngine?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "sample_utterance")
    }

    fun saveSettings(enabled: Boolean, interval: Int, phraseIdx: Int, quiet: Boolean) {
        prefs.edit()
            .putBoolean("enabled", enabled)
            .putInt("interval_minutes", interval)
            .putInt("phrase_index", phraseIdx)
            .putBoolean("quiet_enabled", quiet)
            .apply()

        if (enabled) {
            VoiceDhikrReceiver.scheduleNext(context, interval)
            Toast.makeText(context, "تم تفعيل المنبه الصوتي للأذكار كل $interval دقيقة", Toast.LENGTH_SHORT).show()
        } else {
            VoiceDhikrReceiver.cancelSchedule(context)
            Toast.makeText(context, "تم إيقاف المنبه الصوتي للأذكار", Toast.LENGTH_SHORT).show()
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
                            text = "🗣️ المنبه الصوتي للأذكار النبوية",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "تذكير صوتي ناطق يصدح بالذكر والصلاة على النبي ﷺ",
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
                        Text("📢", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Master Switch Card
                    item {
                        GlassCard {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "تفعيل التذكير الصوتي الناطق",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        "ينطق الهاتف بالذكر المختار تلقائياً في الخلفية",
                                        fontSize = 11.sp,
                                        color = IosTextSecondary
                                    )
                                }

                                Switch(
                                    checked = isEnabled,
                                    onCheckedChange = {
                                        isEnabled = it
                                        saveSettings(isEnabled, selectedIntervalMinutes, selectedPhraseIndex, quietHoursEnabled)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = IosEmeraldPro
                                    )
                                )
                            }
                        }
                    }

                    // Interval Selector
                    item {
                        Text(
                            "معدل التكرار الزمني:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val intervals = listOf(
                                Pair(15, "15 دقيقة"),
                                Pair(30, "30 دقيقة"),
                                Pair(60, "ساعة"),
                                Pair(120, "ساعتين")
                            )

                            intervals.forEach { (mins, label) ->
                                val selected = selectedIntervalMinutes == mins
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (selected) IosGoldApple else Color(0x22FFFFFF))
                                        .clickable {
                                            selectedIntervalMinutes = mins
                                            saveSettings(isEnabled, mins, selectedPhraseIndex, quietHoursEnabled)
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        label,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selected) Color.Black else Color.White
                                    )
                                }
                            }
                        }
                    }

                    // Dhikr Phrase Selector
                    item {
                        Text(
                            "الصيغة المسموعة المفضلة:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                    }

                    items(phrases.size) { index ->
                        val p = phrases[index]
                        val isSelected = selectedPhraseIndex == index
                        GlassCard {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPhraseIndex = index
                                        saveSettings(isEnabled, selectedIntervalMinutes, index, quietHoursEnabled)
                                    }
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            selectedPhraseIndex = index
                                            saveSettings(isEnabled, selectedIntervalMinutes, index, quietHoursEnabled)
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = IosGoldApple,
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                    Text(
                                        p,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) IosGoldApple else Color.White
                                    )
                                }

                                if (index > 0) {
                                    IconButton(
                                        onClick = { speakSample(p) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.VolumeUp,
                                            contentDescription = "استماع",
                                            tint = IosEmeraldPro,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Quiet Hours Setting
                    item {
                        GlassCard {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "وضع الصمت الليلي (11 م - 6 ص)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        "إيقاف الصوت تلقائياً أثناء ساعات النوم حرصاً على راحتك",
                                        fontSize = 11.sp,
                                        color = IosTextSecondary
                                    )
                                }

                                Switch(
                                    checked = quietHoursEnabled,
                                    onCheckedChange = {
                                        quietHoursEnabled = it
                                        saveSettings(isEnabled, selectedIntervalMinutes, selectedPhraseIndex, it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = IosEmeraldPro
                                    )
                                )
                            }
                        }
                    }

                    // Live Test Button
                    item {
                        Button(
                            onClick = {
                                val textToSpeak = if (selectedPhraseIndex == 0) {
                                    "اللَّهُمَّ صَلِّ وَسَلِّمْ وَبَارِكْ عَلَى نَبِيِّنَا مُحَمَّدٍ"
                                } else {
                                    phrases[selectedPhraseIndex].substringAfter("«").substringBefore("»")
                                }
                                speakSample(textToSpeak)
                                Toast.makeText(context, "جاري تشغيل العينة الصوتية الآن 🔊", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IosEmeraldPro, contentColor = Color.Black),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("تجربة الصوت الآن (سماع نموذج)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
