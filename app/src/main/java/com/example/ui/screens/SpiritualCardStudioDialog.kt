package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.viewmodel.MainViewModel

data class CardTheme(
    val id: String,
    val name: String,
    val bgBrush: Brush,
    val borderColor: Color,
    val textColor: Color,
    val accentColor: Color
)

object SpiritualCardThemes {
    val themes = listOf(
        CardTheme(
            id = "royal_emerald",
            name = "زمردي ملكي",
            bgBrush = Brush.verticalGradient(listOf(Color(0xFF0D2E22), Color(0xFF04130E))),
            borderColor = IslamicGoldPrimary,
            textColor = IslamicTextPrimary,
            accentColor = IslamicGoldPrimary
        ),
        CardTheme(
            id = "holy_kaaba",
            name = "سواد الكعبة",
            bgBrush = Brush.verticalGradient(listOf(Color(0xFF1E1E1E), Color(0xFF080808))),
            borderColor = Color(0xFFFFD54F),
            textColor = Color(0xFFFFF9C4),
            accentColor = Color(0xFFFFD54F)
        ),
        CardTheme(
            id = "dawn_glow",
            name = "أفق الفجر",
            bgBrush = Brush.verticalGradient(listOf(Color(0xFF183D4A), Color(0xFF081C24))),
            borderColor = Color(0xFF80DEEA),
            textColor = Color(0xFFE0F7FA),
            accentColor = Color(0xFF4DD0E1)
        ),
        CardTheme(
            id = "midnight_stars",
            name = "سماء الليل",
            bgBrush = Brush.verticalGradient(listOf(Color(0xFF1A1A3A), Color(0xFF0B0B1C))),
            borderColor = Color(0xFFB39DDB),
            textColor = Color(0xFFEDE7F6),
            accentColor = Color(0xFFCE93D8)
        )
    )
}

@Composable
fun SpiritualCardStudioDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val todayHadith by viewModel.todayHadith.collectAsState()

    var cardContentSource by remember { mutableIntStateOf(0) } // 0: آية, 1: حديث, 2: ذكر, 3: مخصص
    var selectedTheme by remember { mutableStateOf(SpiritualCardThemes.themes[0]) }
    var customText by remember { mutableStateOf("") }
    var showWatermark by remember { mutableStateOf(true) }

    val activeText = when (cardContentSource) {
        0 -> "﴿ وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ ﴾\n[سورة البقرة: 186]"
        1 -> "قال رسول الله ﷺ:\n«أَحَبُّ الْأَعْمَالِ إِلَى اللَّهِ أَدْوَمُهَا وَإِنْ قَلَّ»\n(صحيح البخاري)"
        2 -> "«سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ»\nكلمتان خفيفتان على اللسان، ثقيلتان في الميزان، حبيبتان إلى الرحمن."
        else -> if (customText.isNotBlank()) customText else "اكتب نصك المبارك ليظهر في البطاقة المصممة..."
    }

    fun shareCard() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            val textToSend = buildString {
                append(activeText)
                if (showWatermark) {
                    append("\n\n✨ بطاقة من تطبيق الورد اليومي — رفيق المسلم")
                }
            }
            putExtra(Intent.EXTRA_TEXT, textToSend)
        }
        context.startActivity(Intent.createChooser(shareIntent, "مشاركة البطاقة الإيمانية"))
    }

    fun copyCardText() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Spiritual Card", activeText)
        clipboard.setPrimaryClip(clip)
        viewModel.showNotification("تم النسخ", "تم نسخ نص البطاقة بنجاح.")
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
                            text = "🎨 استوديو صانع بطاقات الآيات والأحاديث",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تصاميم إسلامية راقية جاهزة للنشر والمشاركة",
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
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Content Source Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0D281E))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val sources = listOf("آية كريمة", "حديث شريف", "ذكر مأثور", "نص مخصص")
                        sources.forEachIndexed { index, title ->
                            Button(
                                onClick = { cardContentSource = index },
                                modifier = Modifier.weight(1f).height(36.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (cardContentSource == index) IslamicGoldPrimary else Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = title,
                                    color = if (cardContentSource == index) Color(0xFF061A14) else IslamicTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    if (cardContentSource == 3) {
                        OutlinedTextField(
                            value = customText,
                            onValueChange = { customText = it },
                            placeholder = { Text("اكتب نصاً إسلامياً أو حكمة لتصميم بطاقتها...", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IslamicGoldPrimary,
                                unfocusedBorderColor = IslamicBorderGold,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            maxLines = 3
                        )
                    }

                    // Theme selector chips
                    Column {
                        Text(
                            text = "اختر السمة الجمالية للبطاقة:",
                            color = IslamicGoldLight,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(SpiritualCardThemes.themes) { theme ->
                                val isSelected = theme.id == selectedTheme.id
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) theme.accentColor else Color(0xFF0F3226),
                                    border = BorderStroke(1.2.dp, theme.accentColor),
                                    modifier = Modifier.clickable { selectedTheme = theme }
                                ) {
                                    Text(
                                        text = theme.name,
                                        color = if (isSelected) Color(0xFF061A14) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // The Visual Card Preview
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)),
                        border = BorderStroke(1.8.dp, selectedTheme.borderColor)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(selectedTheme.bgBrush)
                                .padding(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Ornamental Top Header
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text("❖", color = selectedTheme.accentColor, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 13.sp,
                                        color = selectedTheme.accentColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("❖", color = selectedTheme.accentColor, fontSize = 14.sp)
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                // Main Text Body
                                Text(
                                    text = activeText,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Serif,
                                    lineHeight = 27.sp,
                                    color = selectedTheme.textColor,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Bottom Footer
                                if (showWatermark) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = selectedTheme.accentColor.copy(alpha = 0.8f),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "الورد اليومي | رفيق المسلم",
                                            fontSize = 10.sp,
                                            color = selectedTheme.accentColor.copy(alpha = 0.8f),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Watermark toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "إظهار شعار التطبيق أسفل البطاقة",
                            color = IslamicTextSecondary,
                            fontSize = 12.sp
                        )
                        Switch(
                            checked = showWatermark,
                            onCheckedChange = { showWatermark = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = IslamicGoldPrimary,
                                checkedTrackColor = Color(0xFF0E3D2C)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { copyCardText() },
                        modifier = Modifier.weight(1f).height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF133E2F)),
                        border = BorderStroke(1.dp, IslamicGoldPrimary)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("نسخ النص", color = IslamicGoldLight, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { shareCard() },
                        modifier = Modifier.weight(1.3f).height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF061A14), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("مشاركة البطاقة", color = Color(0xFF061A14), fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                }
            }
        }
    }
}
