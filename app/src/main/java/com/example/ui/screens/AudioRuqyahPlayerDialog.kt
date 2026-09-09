package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.components.IosGrabberHandle
import com.example.ui.components.IosSegmentedControl
import com.example.ui.theme.*

data class RuqyahTrack(
    val id: Int,
    val title: String,
    val reciter: String,
    val durationText: String,
    val durationSeconds: Int,
    val audioUrl: String,
    val description: String,
    val icon: String
)

@Composable
fun AudioRuqyahPlayerDialog(
    onDismiss: () -> Unit
) {
    val tracks = remember {
        listOf(
            RuqyahTrack(
                id = 1,
                title = "الرقية الشرعية الشاملة المطولة",
                reciter = "الشيخ مشاري راشد العفاسي",
                durationText = "48:15",
                durationSeconds = 2895,
                audioUrl = "https://server8.mp3quran.net/afs/001.mp3",
                description = "رقية جامعة من الكتاب والسنة لعلاج المس والسحر والحسد وطمأنينة الصدر.",
                icon = "🛡️"
            ),
            RuqyahTrack(
                id = 2,
                title = "رقية السكينة وطرد الحزن والوسواس",
                reciter = "الشيخ ماهر المعيقلي",
                durationText = "35:20",
                durationSeconds = 2120,
                audioUrl = "https://server12.mp3quran.net/maher/002.mp3",
                description = "آيات السكينة والانشراح لتهدئة النفس وإزالة القلق والاكتئاب.",
                icon = "🌿"
            ),
            RuqyahTrack(
                id = 3,
                title = "رقية إبطال العين والحسد الحارق",
                reciter = "الشيخ سعد الغامدي",
                durationText = "29:40",
                durationSeconds = 1780,
                audioUrl = "https://server7.mp3quran.net/s_gmd/001.mp3",
                description = "آيات الشفاء ودعاء جبريل عليه السلام وتكرار المعوذات للتحصين التام.",
                icon = "✨"
            ),
            RuqyahTrack(
                id = 4,
                title = "رقية تحصين البيت والأطفال والنوم",
                reciter = "الشيخ أحمد بن علي العجمي",
                durationText = "42:10",
                durationSeconds = 2530,
                audioUrl = "https://server10.mp3quran.net/ajm/002.mp3",
                description = "أدعية الاستعاذة النبوية وتحصين البيوت من الشياطين وجلب البركة.",
                icon = "🏡"
            ),
            RuqyahTrack(
                id = 5,
                title = "سورة البقرة كاملة (بركة وطاردة للشياطين)",
                reciter = "الشيخ عبد الباسط عبد الصمد",
                durationText = "1:58:30",
                durationSeconds = 7110,
                audioUrl = "https://server7.mp3quran.net/basit/002.mp3",
                description = "«اقْرَءُوا سُورَةَ الْبَقَرَةِ، فَإِنَّ أَخْذَهَا بَرَكَةٌ، وَتَرْكَهَا حَسْرَةٌ، وَلَا تَسْتَطِيعُهَا الْبَطَلَةُ».",
                icon = "📖"
            )
        )
    }

    var currentTrackIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }
    var isLooping by remember { mutableStateOf(false) }
    var sleepTimerMinutes by remember { mutableIntStateOf(0) } // 0: Disabled, 15, 30, 45, 60
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var currentProgressSeconds by remember { mutableIntStateOf(145) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: المشغل الرئيسي, 1: قائمة المقاطع

    val currentTrack = tracks[currentTrackIndex]

    // Visualizer wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 38f,
        animationSpec = infiniteRepeatable(tween(400, easing = LinearEasing), RepeatMode.Reverse),
        label = "w1"
    )
    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 28f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(550, easing = LinearEasing), RepeatMode.Reverse),
        label = "w2"
    )
    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 42f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "w3"
    )
    val waveHeight4 by infiniteTransition.animateFloat(
        initialValue = 32f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(tween(480, easing = LinearEasing), RepeatMode.Reverse),
        label = "w4"
    )

    fun formatSeconds(sec: Int): String {
        val m = sec / 60
        val s = sec % 60
        return "%02d:%02d".format(m, s)
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

                // Top Bar
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
                            text = "🎧 مشغل الرقية الشرعية المطولة",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "استماع متواصل وخلفية هادئة مع مؤقت النوم",
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
                        Text("🔊", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("المشغل التفاعلي", "مكتبة الرقى الصوتية"),
                    selectedIndex = selectedTab,
                    onItemSelected = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedTab) {
                    0 -> {
                        // Main Apple-styled Now Playing View
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Album Artwork / Calligraphy Card
                            Box(
                                modifier = Modifier
                                    .size(190.dp)
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF0F382A),
                                                Color(0xFF051C14),
                                                Color(0xFF1E281F)
                                            )
                                        )
                                    )
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(currentTrack.icon, fontSize = 48.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "الرقية الشرعية",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = IosGoldApple
                                    )
                                    Text(
                                        "شفاء وطمأنينة",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            // Title and Reciter
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = currentTrack.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentTrack.reciter,
                                    fontSize = 13.sp,
                                    color = IosGoldApple
                                )
                            }

                            // Dynamic Visualizer Bars (when playing)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val heights = if (isPlaying) listOf(waveHeight1, waveHeight2, waveHeight3, waveHeight4, waveHeight2, waveHeight1)
                                else listOf(6f, 6f, 6f, 6f, 6f, 6f)

                                heights.forEach { h ->
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .width(5.dp)
                                            .height(h.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(IosEmeraldPro)
                                    )
                                }
                            }

                            // Scrubber Slider
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
                                Slider(
                                    value = currentProgressSeconds.toFloat(),
                                    onValueChange = { currentProgressSeconds = it.toInt() },
                                    valueRange = 0f..currentTrack.durationSeconds.toFloat(),
                                    colors = SliderDefaults.colors(
                                        thumbColor = IosGoldApple,
                                        activeTrackColor = IosGoldApple,
                                        inactiveTrackColor = Color(0x33FFFFFF)
                                    )
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        formatSeconds(currentProgressSeconds),
                                        fontSize = 11.sp,
                                        color = IosTextSecondary
                                    )
                                    Text(
                                        currentTrack.durationText,
                                        fontSize = 11.sp,
                                        color = IosTextSecondary
                                    )
                                }
                            }

                            // Controls Row (Rewind, Play/Pause, Fast Forward, Loop)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Loop Toggle
                                IconButton(
                                    onClick = { isLooping = !isLooping },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Repeat,
                                        contentDescription = "تكرار",
                                        tint = if (isLooping) IosEmeraldPro else Color.Gray,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                // Previous Track
                                IconButton(
                                    onClick = {
                                        if (currentTrackIndex > 0) {
                                            currentTrackIndex--
                                            currentProgressSeconds = 0
                                        }
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(Icons.Default.SkipPrevious, contentDescription = "السابق", tint = Color.White, modifier = Modifier.size(28.dp))
                                }

                                // Play / Pause Big Button
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(IosGoldApple)
                                        .clickable { isPlaying = !isPlaying },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "تشغيل/إيقاف",
                                        tint = Color.Black,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }

                                // Next Track
                                IconButton(
                                    onClick = {
                                        if (currentTrackIndex < tracks.size - 1) {
                                            currentTrackIndex++
                                            currentProgressSeconds = 0
                                        }
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(Icons.Default.SkipNext, contentDescription = "التالي", tint = Color.White, modifier = Modifier.size(28.dp))
                                }

                                // Speed Toggle
                                Surface(
                                    color = Color(0x22FFFFFF),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable {
                                        playbackSpeed = when (playbackSpeed) {
                                            1.0f -> 1.25f
                                            1.25f -> 1.5f
                                            1.5f -> 0.75f
                                            else -> 1.0f
                                        }
                                    }
                                ) {
                                    Text(
                                        "${playbackSpeed}x",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            // Sleep Timer Selector Card
                            GlassCard {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.Bedtime, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(18.dp))
                                        Text("مؤقت إيقاف النوم:", fontSize = 12.sp, color = Color.White)
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        listOf(0, 15, 30, 45, 60).forEach { mins ->
                                            val isSelected = sleepTimerMinutes == mins
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (isSelected) IosEmeraldPro else Color(0x22FFFFFF))
                                                    .clickable { sleepTimerMinutes = mins }
                                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    if (mins == 0) "إيقاف" else "$mins د",
                                                    fontSize = 10.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) Color.Black else Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Tracks Playlist
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(tracks) { index, track ->
                                val isSelected = index == currentTrackIndex
                                GlassCard {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                currentTrackIndex = index
                                                isPlaying = true
                                                currentProgressSeconds = 0
                                                selectedTab = 0
                                            }
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(42.dp)
                                                    .background(
                                                        if (isSelected) IosGoldApple.copy(alpha = 0.25f) else Color(0x22FFFFFF),
                                                        CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isSelected && isPlaying) {
                                                    Icon(Icons.Default.GraphicEq, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(20.dp))
                                                } else {
                                                    Text(track.icon, fontSize = 20.sp)
                                                }
                                            }

                                            Column {
                                                Text(
                                                    track.title,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = if (isSelected) IosGoldApple else Color.White,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    track.reciter,
                                                    fontSize = 12.sp,
                                                    color = IosTextSecondary
                                                )
                                            }
                                        }

                                        Surface(
                                            color = Color(0x22FFFFFF),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                track.durationText,
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
