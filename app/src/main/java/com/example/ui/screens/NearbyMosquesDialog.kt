package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

data class NearbyMosque(
    val id: String,
    val name: String,
    val distance: String,
    val walkTime: String,
    val address: String,
    val iqamahNotice: String,
    val hasJummah: Boolean,
    val hasWomenPrayerArea: Boolean,
    val latitude: Double,
    val longitude: Double
)

object NearbyMosquesData {
    fun getMosquesForCity(cityName: String): List<NearbyMosque> {
        return listOf(
            NearbyMosque(
                id = "m1",
                name = "جامع الهدى الكبير",
                distance = "٣٥٠ متر",
                walkTime = "٤ دقائق مشياً",
                address = "حي النخيل - الشارع العام",
                iqamahNotice = "الإقامة: الفجر ٢٠د، باقي الصلوات ١٥د",
                hasJummah = true,
                hasWomenPrayerArea = true,
                latitude = 24.7136,
                longitude = 46.6753
            ),
            NearbyMosque(
                id = "m2",
                name = "مسجد الإمام البخاري",
                distance = "٦٥٠ متر",
                walkTime = "٨ دقائق مشياً",
                address = "شارع الأمير سلطان - بجوار الحديقة",
                iqamahNotice = "الإقامة: الفجر ٢٥د، العشاء ١٥د",
                hasJummah = false,
                hasWomenPrayerArea = true,
                latitude = 24.7180,
                longitude = 46.6780
            ),
            NearbyMosque(
                id = "m3",
                name = "جامع التوحيد وسنة المصطفى",
                distance = "١.١ كم",
                walkTime = "٣ دقائق بالسيارة",
                address = "طريق الملك فهد الفرعي",
                iqamahNotice = "الإقامة: الفجر ٢٠د، المغرب ١٠د",
                hasJummah = true,
                hasWomenPrayerArea = true,
                latitude = 24.7220,
                longitude = 46.6710
            ),
            NearbyMosque(
                id = "m4",
                name = "مسجد التقوى والنور",
                distance = "١.٤ كم",
                walkTime = "٤ دقائق بالسيارة",
                address = "الحي الشرقي - ساحة الأندلس",
                iqamahNotice = "الإقامة بعد الأذان بـ ١٥ دقيقة لجميع الفروض",
                hasJummah = false,
                hasWomenPrayerArea = false,
                latitude = 24.7080,
                longitude = 46.6830
            ),
            NearbyMosque(
                id = "m5",
                name = "جامع الفرقان والمصلى الجامع",
                distance = "٢.٠ كم",
                walkTime = "٦ دقائق بالسيارة",
                address = "ميدان القدس - تقاطع الشفاء",
                iqamahNotice = "الإقامة موحدة حسب توقيت وزارة الأوقاف",
                hasJummah = true,
                hasWomenPrayerArea = true,
                latitude = 24.7010,
                longitude = 46.6690
            )
        )
    }
}

@Composable
fun NearbyMosquesDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val currentCity by viewModel.currentCity.collectAsState()
    val mosques = remember(currentCity) { NearbyMosquesData.getMosquesForCity(currentCity) }

    fun openMapNavigation(query: String) {
        try {
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode("مسجد $query $currentCity"))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                val genericIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode("مسجد $currentCity")))
                context.startActivity(genericIntent)
            }
        } catch (e: Exception) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode("مساجد $currentCity")))
            context.startActivity(browserIntent)
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
                            .background(Color(0xFF0F3226), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = IslamicGoldPrimary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🕌 المساجد والمصليات القريبة",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "المساجد المحيطة بـ $currentCity ومواقيت الإقامة",
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
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // GPS Search Button Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF0B2E21),
                    border = BorderStroke(1.dp, IslamicBorderGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            openMapNavigation("مساجد قريبة مني")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Explore, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "بحث مباشر عن أقرب مسجد على الخريطة",
                                    color = IslamicGoldLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "فتح الملاحة والتوجيه خطوة بخطوة بالـ GPS",
                                    color = IslamicTextSecondary,
                                    fontSize = 10.5.sp
                                )
                            }
                        }

                        Icon(Icons.Default.OpenInNew, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "أقرب المساجد والمصليات المسجلة:",
                    color = IslamicGoldLight,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(mosques) { mosque ->
                        GlassCard(
                            backgroundColor = Color(0xFF09241B),
                            borderColor = IslamicBorderGold
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF123C2D)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Mosque, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = mosque.name,
                                                color = IslamicTextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = mosque.address,
                                                color = IslamicTextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF134231)
                                    ) {
                                        Text(
                                            text = mosque.distance,
                                            color = IslamicGoldLight,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0x335DD9A9)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = IslamicMintLight, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(mosque.walkTime, color = IslamicMintLight, fontSize = 10.sp)
                                        }
                                    }

                                    if (mosque.hasJummah) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0x33E2B84D)
                                        ) {
                                            Text(
                                                text = "صلاة الجمعة ✓",
                                                color = IslamicGoldPrimary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    if (mosque.hasWomenPrayerArea) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0x22FFFFFF)
                                        ) {
                                            Text(
                                                text = "مصلى نساء ✓",
                                                color = Color(0xFFE0E0E0),
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF041812)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Schedule, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = mosque.iqamahNotice,
                                            color = IslamicGoldLight,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = { openMapNavigation(mosque.name) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF123B2D)),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(0.8.dp, IslamicGoldPrimary),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.Directions, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("الاتجاهات عبر الخريطة", color = IslamicGoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
