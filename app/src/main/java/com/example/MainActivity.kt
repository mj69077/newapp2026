package com.example

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.*
import com.example.notification.PushNotificationHelper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AudioPlayerBar
import com.example.ui.screens.*
import com.example.ui.theme.DailyWirdTheme
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGoldPrimary
import com.example.ui.theme.IslamicTextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        PushNotificationHelper.createNotificationChannels(this)
        com.example.notification.OngoingPrayerNotificationHelper.createOngoingChannel(this)
        handleIntent(intent)
        setContent {
            DailyWirdTheme {
                MainAppRoot(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val tabExtra = intent?.getStringExtra("INITIAL_TAB")
        val action = intent?.action
        when {
            tabExtra == "ATHKAR" || action == "ACTION_ATHKAR_ALERT" -> {
                viewModel.setTab(AppTab.ATHKAR)
            }
            tabExtra == "PRAYER" || action == "ACTION_PRAYER_ALERT" -> {
                viewModel.setTab(AppTab.PRAYER)
            }
            tabExtra == "QURAN" || action == "ACTION_QURAN_WIRD" -> {
                viewModel.setTab(AppTab.QURAN)
            }
            tabExtra == "RADIO" -> {
                viewModel.setTab(AppTab.RADIO)
            }
            action == "ACTION_SUNNAH_ALERT" -> {
                viewModel.setTab(AppTab.DAILY_TASKS)
            }
        }
    }
}

@Composable
fun MainAppRoot(viewModel: MainViewModel) {
    // Request notification permission automatically on Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* Handled */ }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    val currentTab by viewModel.currentTab.collectAsState()
    val notification by viewModel.uiNotification.collectAsState()
    val audioState by viewModel.audioPlayer.playbackState.collectAsState()
    val availableReciters by viewModel.availableReciters.collectAsState()
    val isQuranFullscreen by viewModel.isQuranFullscreen.collectAsState()
    val selectedSurah by viewModel.selectedSurah.collectAsState()
    val isFullscreenQuran = isQuranFullscreen && currentTab == AppTab.QURAN && selectedSurah != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isFullscreenQuran) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    // Floating Audio Player if active
                    if (audioState.currentSurah != null || audioState.currentRadio != null) {
                        AudioPlayerBar(
                            state = audioState,
                            availableReciters = availableReciters,
                            onTogglePlayPause = { viewModel.audioPlayer.togglePlayPause() },
                            onStop = { viewModel.audioPlayer.stop() },
                            onSelectReciter = { viewModel.audioPlayer.setReciter(it) }
                        )
                    }

                    // Apple iOS Floating Frosted Dock
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(32.dp)),
                            color = Color(0xEB0A1813),
                            border = BorderStroke(
                                1.dp,
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0x38FFFFFF),
                                        Color(0x0FFFFFFF),
                                        Color(0x28F5D372)
                                    )
                                )
                            ),
                            shadowElevation = 8.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val tabs = listOf(
                                    Triple(AppTab.PRAYER, Icons.Default.AccessTime, "الصلاة"),
                                    Triple(AppTab.RADIO, Icons.Default.Radio, "الإذاعة"),
                                    Triple(AppTab.FATWAS, Icons.Default.QuestionAnswer, "الفتاوى"),
                                    Triple(AppTab.ATHKAR, Icons.Default.TouchApp, "الأذكار"),
                                    Triple(AppTab.DUAS, Icons.Default.VolunteerActivism, "الأدعية"),
                                    Triple(AppTab.QURAN, Icons.Default.MenuBook, "المصحف"),
                                    Triple(AppTab.DAILY_TASKS, Icons.Default.CheckCircle, "الورد")
                                )

                                tabs.forEach { (tab, icon, label) ->
                                    val isSelected = currentTab == tab
                                    val animColor by animateColorAsState(
                                        targetValue = if (isSelected) IslamicGoldPrimary else IslamicTextSecondary,
                                        label = "dock_item_color"
                                    )
                                    val animBg by animateColorAsState(
                                        targetValue = if (isSelected) Color(0x2EE2B84D) else Color.Transparent,
                                        label = "dock_item_bg"
                                    )

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(animBg)
                                            .clickable { viewModel.setTab(tab) }
                                            .padding(vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = label,
                                            tint = animColor,
                                            modifier = Modifier.size(if (isSelected) 22.dp else 20.dp)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = label,
                                            fontSize = 8.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = animColor
                                        )
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Screen switching with AnimatedContent
            AnimatedContent(
                targetState = currentTab,
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    AppTab.DAILY_TASKS -> DailyDashboardScreen(viewModel = viewModel)
                    AppTab.QURAN -> QuranReaderScreen(viewModel = viewModel)
                    AppTab.RADIO -> RadioScreen(viewModel = viewModel)
                    AppTab.DUAS -> DuasScreen(viewModel = viewModel)
                    AppTab.ATHKAR -> AthkarTasbihScreen(viewModel = viewModel)
                    AppTab.FATWAS -> FatwasScreen(viewModel = viewModel)
                    AppTab.PRAYER -> PrayerQiblaScreen(viewModel = viewModel)
                }
            }

            // In-app Notification Banner (Toast)
            AnimatedVisibility(
                visible = notification != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                notification?.let { notif ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF0F382B),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IslamicGoldPrimary),
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = IslamicGoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = notif.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = IslamicGoldLight,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = notif.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
