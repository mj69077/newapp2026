package com.example.ui.screens

import android.content.Context
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
import com.example.data.model.DailyTask
import com.example.data.model.TaskCategory
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

object QadaaPrefs {
    private const val PREF_NAME = "qadaa_tracker_prefs"

    fun getInt(context: Context, key: String, default: Int = 0): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(key, default)
    }

    fun setInt(context: Context, key: String, value: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putInt(key, value.coerceAtLeast(0)).apply()
    }
}

@Composable
fun QadaaTrackerDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableIntStateOf(0) } // 0: صلوات, 1: صيام

    // Prayer counts
    var fajrCount by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_fajr", 15)) }
    var dhuhrCount by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_dhuhr", 12)) }
    var asrCount by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_asr", 12)) }
    var maghribCount by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_maghrib", 10)) }
    var ishaCount by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_isha", 14)) }
    var witrCount by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_witr", 8)) }

    // Fasting counts
    var ramadanDays by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_ramadan", 5)) }
    var kaffarahDays by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_kaffarah", 0)) }
    var nadhrDays by remember { mutableIntStateOf(QadaaPrefs.getInt(context, "qadaa_nadhr", 0)) }

    val totalPrayers = fajrCount + dhuhrCount + asrCount + maghribCount + ishaCount + witrCount
    val totalFasting = ramadanDays + kaffarahDays + nadhrDays

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
                            text = "⚖️ سجل قضاء الصلوات والصيام",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تتبع وإبراء الذمة من العبادات الفائتة",
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
                        Icon(Icons.Default.FactCheck, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Summary Metric
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
                            Text(
                                text = if (activeTab == 0) "إجمالي الصلوات المتبقية: $totalPrayers صلاة"
                                else "إجمالي أيام الصيام المتبقية: $totalFasting يوماً",
                                color = IslamicGoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            val estimateText = if (activeTab == 0) {
                                if (totalPrayers > 0) {
                                    val days = (totalPrayers / 5).coerceAtLeast(1)
                                    "بمعدل قضاء صلاة مع كل فريضة: تنتهي خلال $days يوماً بإذن الله"
                                } else "ما شاء الله! ذمتك بريئة من قضاء الصلوات"
                            } else {
                                if (totalFasting > 0) "يمكنك صيامهما بالاثنين والخميس أو الأيام البيض"
                                else "ما شاء الله! لا توجد أيام قضاء عليك"
                            }
                            Text(
                                text = estimateText,
                                color = IslamicMintLight,
                                fontSize = 11.sp
                            )
                        }

                        Icon(
                            if (activeTab == 0) Icons.Default.AccessTime else Icons.Default.WbTwilight,
                            contentDescription = null,
                            tint = IslamicGoldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D281E))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { activeTab = 0 },
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeTab == 0) IslamicGoldPrimary else Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "صلوات القضاء ($totalPrayers)",
                            color = if (activeTab == 0) Color(0xFF061A14) else IslamicTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { activeTab = 1 },
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeTab == 1) IslamicGoldPrimary else Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "صيام القضاء ($totalFasting)",
                            color = if (activeTab == 1) Color(0xFF061A14) else IslamicTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // List of items
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (activeTab == 0) {
                        item {
                            QadaaCounterRow(
                                title = "صلاة الفجر",
                                count = fajrCount,
                                onIncrement = {
                                    fajrCount += 1
                                    QadaaPrefs.setInt(context, "qadaa_fajr", fajrCount)
                                },
                                onDecrement = {
                                    if (fajrCount > 0) fajrCount -= 1
                                    QadaaPrefs.setInt(context, "qadaa_fajr", fajrCount)
                                },
                                onAddFive = {
                                    fajrCount += 5
                                    QadaaPrefs.setInt(context, "qadaa_fajr", fajrCount)
                                }
                            )
                        }

                        item {
                            QadaaCounterRow(
                                title = "صلاة الظهر",
                                count = dhuhrCount,
                                onIncrement = {
                                    dhuhrCount += 1
                                    QadaaPrefs.setInt(context, "qadaa_dhuhr", dhuhrCount)
                                },
                                onDecrement = {
                                    if (dhuhrCount > 0) dhuhrCount -= 1
                                    QadaaPrefs.setInt(context, "qadaa_dhuhr", dhuhrCount)
                                },
                                onAddFive = {
                                    dhuhrCount += 5
                                    QadaaPrefs.setInt(context, "qadaa_dhuhr", dhuhrCount)
                                }
                            )
                        }

                        item {
                            QadaaCounterRow(
                                title = "صلاة العصر",
                                count = asrCount,
                                onIncrement = {
                                    asrCount += 1
                                    QadaaPrefs.setInt(context, "qadaa_asr", asrCount)
                                },
                                onDecrement = {
                                    if (asrCount > 0) asrCount -= 1
                                    QadaaPrefs.setInt(context, "qadaa_asr", asrCount)
                                },
                                onAddFive = {
                                    asrCount += 5
                                    QadaaPrefs.setInt(context, "qadaa_asr", asrCount)
                                }
                            )
                        }

                        item {
                            QadaaCounterRow(
                                title = "صلاة المغرب",
                                count = maghribCount,
                                onIncrement = {
                                    maghribCount += 1
                                    QadaaPrefs.setInt(context, "qadaa_maghrib", maghribCount)
                                },
                                onDecrement = {
                                    if (maghribCount > 0) maghribCount -= 1
                                    QadaaPrefs.setInt(context, "qadaa_maghrib", maghribCount)
                                },
                                onAddFive = {
                                    maghribCount += 5
                                    QadaaPrefs.setInt(context, "qadaa_maghrib", maghribCount)
                                }
                            )
                        }

                        item {
                            QadaaCounterRow(
                                title = "صلاة العشاء",
                                count = ishaCount,
                                onIncrement = {
                                    ishaCount += 1
                                    QadaaPrefs.setInt(context, "qadaa_isha", ishaCount)
                                },
                                onDecrement = {
                                    if (ishaCount > 0) ishaCount -= 1
                                    QadaaPrefs.setInt(context, "qadaa_isha", ishaCount)
                                },
                                onAddFive = {
                                    ishaCount += 5
                                    QadaaPrefs.setInt(context, "qadaa_isha", ishaCount)
                                }
                            )
                        }

                        item {
                            QadaaCounterRow(
                                title = "صلاة الوتر",
                                count = witrCount,
                                onIncrement = {
                                    witrCount += 1
                                    QadaaPrefs.setInt(context, "qadaa_witr", witrCount)
                                },
                                onDecrement = {
                                    if (witrCount > 0) witrCount -= 1
                                    QadaaPrefs.setInt(context, "qadaa_witr", witrCount)
                                },
                                onAddFive = {
                                    witrCount += 5
                                    QadaaPrefs.setInt(context, "qadaa_witr", witrCount)
                                }
                            )
                        }
                    } else {
                        item {
                            QadaaCounterRow(
                                title = "قضاء أيام من رمضان",
                                count = ramadanDays,
                                onIncrement = {
                                    ramadanDays += 1
                                    QadaaPrefs.setInt(context, "qadaa_ramadan", ramadanDays)
                                },
                                onDecrement = {
                                    if (ramadanDays > 0) ramadanDays -= 1
                                    QadaaPrefs.setInt(context, "qadaa_ramadan", ramadanDays)
                                },
                                onAddFive = {
                                    ramadanDays += 5
                                    QadaaPrefs.setInt(context, "qadaa_ramadan", ramadanDays)
                                }
                            )
                        }

                        item {
                            QadaaCounterRow(
                                title = "كفارات الصيام",
                                count = kaffarahDays,
                                onIncrement = {
                                    kaffarahDays += 1
                                    QadaaPrefs.setInt(context, "qadaa_kaffarah", kaffarahDays)
                                },
                                onDecrement = {
                                    if (kaffarahDays > 0) kaffarahDays -= 1
                                    QadaaPrefs.setInt(context, "qadaa_kaffarah", kaffarahDays)
                                },
                                onAddFive = {
                                    kaffarahDays += 5
                                    QadaaPrefs.setInt(context, "qadaa_kaffarah", kaffarahDays)
                                }
                            )
                        }

                        item {
                            QadaaCounterRow(
                                title = "صيام النذور والعهود",
                                count = nadhrDays,
                                onIncrement = {
                                    nadhrDays += 1
                                    QadaaPrefs.setInt(context, "qadaa_nadhr", nadhrDays)
                                },
                                onDecrement = {
                                    if (nadhrDays > 0) nadhrDays -= 1
                                    QadaaPrefs.setInt(context, "qadaa_nadhr", nadhrDays)
                                },
                                onAddFive = {
                                    nadhrDays += 5
                                    QadaaPrefs.setInt(context, "qadaa_nadhr", nadhrDays)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Add to Today Tasks Button
                Button(
                    onClick = {
                        val taskName = if (activeTab == 0) "قضاء صلوات اليوم الفائتة" else "صيام يوم قضاء"
                        val taskDesc = if (activeTab == 0) "صلاة ركعتي قضاء الفجر وصلوات اليوم" else "نية صيام القضاء وإبراء الذمة"
                        viewModel.addNewTask(
                            title = taskName,
                            category = TaskCategory.PRAYER,
                            target = 1,
                            description = taskDesc
                        )
                        viewModel.showNotification("تمت الإضافة للورد اليومي", "تمت إضافة مهمة '$taskName' إلى قائمة مهامك اليومية.")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                ) {
                    Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = Color(0xFF061A14), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "إضافة خطة القضاء إلى مهام الورد اليوم",
                        color = Color(0xFF061A14),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QadaaCounterRow(
    title: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onAddFive: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0A241B),
        border = BorderStroke(1.dp, IslamicBorderGold),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    color = IslamicTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "المتبقي: $count",
                    color = if (count > 0) IslamicGoldLight else Color(0xFF81C784),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // -1 Button (قضاء صلاة)
                IconButton(
                    onClick = onDecrement,
                    enabled = count > 0,
                    modifier = Modifier
                        .size(34.dp)
                        .background(if (count > 0) Color(0xFF1B5E20) else Color(0xFF132A21), CircleShape)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "إنقاص", tint = if (count > 0) Color(0xFFA5D6A7) else Color.Gray, modifier = Modifier.size(18.dp))
                }

                // Count Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF061812),
                    border = BorderStroke(1.dp, IslamicBorderGold)
                ) {
                    Text(
                        text = "$count",
                        color = IslamicGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .widthIn(min = 36.dp)
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }

                // +1 Button
                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color(0xFF133E2F), CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "زيادة", tint = IslamicGoldPrimary, modifier = Modifier.size(18.dp))
                }

                // +5 Quick Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF164836),
                    modifier = Modifier.clickable { onAddFive() }
                ) {
                    Text(
                        text = "+٥",
                        color = IslamicGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
