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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.AudioCacheManager
import com.example.data.local.OfflineData
import com.example.data.model.Reciter
import com.example.data.model.Surah
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun AudioDownloadManagerDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val availableReciters by viewModel.availableReciters.collectAsState()
    val allSurahs by viewModel.surahList.collectAsState()

    var selectedReciter by remember {
        mutableStateOf(availableReciters.firstOrNull() ?: Reciter("alafasy", "مشاري راشد العفاسي", "ar.alafasy", "https://server8.mp3quran.net/afs/"))
    }

    var downloadingSurahId by remember { mutableStateOf<Int?>(null) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var cacheSizeBytes by remember { mutableLongStateOf(0L) }
    var downloadedCount by remember { mutableIntStateOf(0) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    // Calculate Cache size and downloaded files
    LaunchedEffect(refreshTrigger, selectedReciter) {
        withContext(Dispatchers.IO) {
            val cacheDir = File(context.cacheDir, "quran_audio")
            if (cacheDir.exists()) {
                val files = cacheDir.listFiles() ?: emptyArray()
                cacheSizeBytes = files.sumOf { it.length() }
                downloadedCount = files.size
            } else {
                cacheSizeBytes = 0L
                downloadedCount = 0
            }
        }
    }

    fun isSurahDownloaded(surahId: Int): Boolean {
        val formattedId = String.format("%03d", surahId)
        val url = "${selectedReciter.serverUrl}$formattedId.mp3"
        return AudioCacheManager.getCachedFile(context, url) != null
    }

    fun startDownload(surah: Surah) {
        if (downloadingSurahId != null) return
        downloadingSurahId = surah.id
        downloadProgress = 0.1f

        coroutineScope.launch {
            val formattedId = String.format("%03d", surah.id)
            val url = "${selectedReciter.serverUrl}$formattedId.mp3"
            downloadProgress = 0.4f
            val file = AudioCacheManager.downloadOrGet(context, url)
            downloadProgress = 1.0f
            downloadingSurahId = null
            refreshTrigger += 1
            if (file != null) {
                viewModel.showNotification("تم التنزيل بنجاح", "تم حفظ تلاوة سورة ${surah.nameArabic} للاستماع أوفلاين.")
            } else {
                viewModel.showNotification("تنبيه التنزيل", "تعذر تنزيل السورة، سيتم تشغيلها عبر البث المباشر.")
            }
        }
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
                            .background(Color(0xFF0F3326), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = IslamicGoldPrimary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📥 مدير التنزيلات الصوتية للأوفلاين",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تنزيل سور القرآن للاستماع بدون إنترنت",
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
                        Icon(Icons.Default.CloudDownload, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Cache Storage Metric Card
                GlassCard(
                    backgroundColor = Color(0xFF09241B),
                    borderColor = IslamicBorderGold
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            val mbSize = String.format("%.1f", cacheSizeBytes / (1024f * 1024f))
                            Text(
                                text = "مساحة التخزين المستهلكة: $mbSize ميجابايت",
                                color = IslamicGoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "عدد الملفات المحفوظة محلياً: $downloadedCount سورة",
                                color = IslamicMintLight,
                                fontSize = 11.5.sp
                            )
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    withContext(Dispatchers.IO) {
                                        AudioCacheManager.clearCache(context)
                                    }
                                    refreshTrigger += 1
                                    viewModel.showNotification("تنظيف الذاكرة", "تم تفريغ الملفات الصوتية المؤقتة بنجاح.")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF421515)),
                            border = BorderStroke(0.8.dp, Color(0xFFE53935)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = Color(0xFFFF8A80), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("مسح", color = Color(0xFFFF8A80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Reciters Selection Bar
                Text(
                    text = "اختر القارئ المفضل للتحميل:",
                    color = IslamicGoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(availableReciters) { reciter ->
                        val isSelected = reciter.id == selectedReciter.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) IslamicGoldPrimary else Color(0xFF0F3226),
                            border = BorderStroke(1.dp, if (isSelected) IslamicGoldPrimary else IslamicBorderGold),
                            modifier = Modifier.clickable { selectedReciter = reciter }
                        ) {
                            Text(
                                text = reciter.nameArabic,
                                color = if (isSelected) Color(0xFF061A14) else IslamicTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Batch Download Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF103A2B),
                    border = BorderStroke(1.dp, IslamicBorderGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val essentialIds = listOf(1, 18, 36, 55, 67, 112, 113, 114)
                            val surahsToDownload = allSurahs.filter { it.id in essentialIds }
                            coroutineScope.launch {
                                surahsToDownload.forEach { s ->
                                    val formattedId = String.format("%03d", s.id)
                                    val url = "${selectedReciter.serverUrl}$formattedId.mp3"
                                    AudioCacheManager.downloadOrGet(context, url)
                                }
                                refreshTrigger += 1
                                viewModel.showNotification("اكتمل التحميل الشامل", "تم تحميل السور الأساسية (الفاتحة، الكهف، يس، الملك، المعوذات) للاستماع أوفلاين!")
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DownloadForOffline, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "⚡ تحميل السور الأساسية دفعة واحدة",
                                    color = IslamicGoldLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "الفاتحة، الكهف، يس، الرحمن، الملك، وقصار السور",
                                    color = IslamicTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(13.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Surahs Download List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(allSurahs) { surah ->
                        val isDownloaded = isSurahDownloaded(surah.id)
                        val isCurrentlyDownloading = downloadingSurahId == surah.id

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF0A261D),
                            border = BorderStroke(0.8.dp, if (isDownloaded) Color(0x664CAF50) else IslamicBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(if (isDownloaded) Color(0xFF1B5E20) else Color(0xFF133627)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${surah.id}",
                                            color = if (isDownloaded) Color(0xFFA5D6A7) else IslamicGoldLight,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = "سورة ${surah.nameArabic}",
                                            color = IslamicTextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp
                                        )
                                        Text(
                                            text = "${surah.versesCount} آية • ${if (surah.revelationPlace == "makkah") "مكية" else "مدنية"}",
                                            color = IslamicTextSecondary,
                                            fontSize = 10.5.sp
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isCurrentlyDownloading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = IslamicGoldPrimary,
                                            strokeWidth = 2.5.dp
                                        )
                                    } else if (isDownloaded) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0x334CAF50),
                                            border = BorderStroke(0.5.dp, Color(0xFF4CAF50))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("جاهزة أوفلاين", color = Color(0xFFE8F5E9), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    } else {
                                        IconButton(
                                            onClick = { startDownload(surah) },
                                            modifier = Modifier
                                                .size(34.dp)
                                                .background(Color(0xFF133E2F), CircleShape)
                                        ) {
                                            Icon(
                                                Icons.Default.ArrowDownward,
                                                contentDescription = "تحميل",
                                                tint = IslamicGoldPrimary,
                                                modifier = Modifier.size(18.dp)
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
