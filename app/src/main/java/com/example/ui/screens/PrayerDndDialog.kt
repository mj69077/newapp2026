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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.notification.PrayerDndManager
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

@Composable
fun PrayerDndDialog(
    onDismiss: () -> Unit,
    onShowNotification: (String, String) -> Unit
) {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(PrayerDndManager.isAutoMuteEnabled(context)) }
    var durationMinutes by remember { mutableIntStateOf(PrayerDndManager.getMuteDurationMinutes(context)) }
    var hasPermission by remember { mutableStateOf(PrayerDndManager.hasNotificationPolicyAccess(context)) }

    val durations = listOf(15, 20, 25, 30, 45)

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
                            text = "🔕 الوضع الصامت الذكي أثناء الصلاة",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "كتم الهاتف آلياً في المسجد لضمان الخشوع",
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
                        Icon(Icons.Default.VolumeOff, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Master Switch Card
                    item {
                        GlassCard(
                            backgroundColor = Color(0xFF09241B),
                            borderColor = IslamicBorderGold
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(if (isEnabled) Color(0xFF134231) else Color(0xFF261818)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            if (isEnabled) Icons.Default.NotificationsOff else Icons.Default.Notifications,
                                            contentDescription = null,
                                            tint = if (isEnabled) IslamicGoldPrimary else Color.Gray,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "تفعيل الكتم التلقائي مع الأذان",
                                            color = IslamicTextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "يعيد الصوت لطبيعته فور انتهاء مدة الصلاة المحددة",
                                            color = IslamicTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Switch(
                                    checked = isEnabled,
                                    onCheckedChange = { checked ->
                                        isEnabled = checked
                                        PrayerDndManager.setAutoMuteEnabled(context, checked)
                                        onShowNotification(
                                            "الوضع الصامت للصلاة",
                                            if (checked) "تم تفعيل كتم الصوت التلقائي عند الأذان" else "تم إيقاف كتم الصوت التلقائي"
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = IslamicGoldPrimary,
                                        checkedTrackColor = Color(0xFF0E3D2C),
                                        uncheckedThumbColor = Color.Gray,
                                        uncheckedTrackColor = Color(0xFF192520)
                                    )
                                )
                            }
                        }
                    }

                    // Permission Notice if needed
                    if (!hasPermission) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF332008),
                                border = BorderStroke(1.dp, Color(0xFFFFB300)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "مطلوب إذن الوصول لوضع عدم الإزعاج (DND)",
                                                color = Color(0xFFFFE082),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = "لكتم صوت الرنين والوسائط تلقائياً على نظام أندرويد الحديث",
                                                color = Color(0xFFFFECB3),
                                                fontSize = 10.5.sp
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            PrayerDndManager.requestNotificationPolicyAccess(context)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("منح الإذن", color = Color(0xFF261800), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Duration Selection
                    item {
                        GlassCard(
                            backgroundColor = Color(0xFF09241B),
                            borderColor = IslamicBorderGold
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "مدة كتم الصوت بعد دخول وقت الصلاة:",
                                    color = IslamicGoldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "المدة المقترحة: ٢٠ دقيقة لتشمل الركعات وسنة الراتبة",
                                    color = IslamicTextSecondary,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    durations.forEach { min ->
                                        val isSelected = min == durationMinutes
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) IslamicGoldPrimary else Color(0xFF0F3226),
                                            border = BorderStroke(1.dp, if (isSelected) IslamicGoldPrimary else IslamicBorderGold),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    durationMinutes = min
                                                    PrayerDndManager.setMuteDurationMinutes(context, min)
                                                }
                                        ) {
                                            Text(
                                                text = "$min د",
                                                color = if (isSelected) Color(0xFF061A14) else IslamicTextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(vertical = 10.dp),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // How it works info card
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF082017),
                            border = BorderStroke(1.dp, IslamicBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("آلية العمل الذكية:", color = IslamicGoldPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "١. عند حلول موعد الأذان، ينطلق صوت الأذان أو التنبيه.\n" +
                                            "٢. بعد انتهاء الأذان، يُفعّل وضع عدم الإزعاج الصامت فوراً.\n" +
                                            "٣. بعد انقضاء $durationMinutes دقيقة، يستعيد الهاتف رنينه التلقائي دون أي تدخل منك.",
                                    color = IslamicTextSecondary,
                                    fontSize = 11.5.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                ) {
                    Text("حفظ وإغلاق", color = Color(0xFF061A14), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
