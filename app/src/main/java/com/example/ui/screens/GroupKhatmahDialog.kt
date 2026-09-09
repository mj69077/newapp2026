package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

data class JuzAssignment(
    val juzNumber: Int,
    var readerName: String = "",
    var isCompleted: Boolean = false
)

object GroupKhatmahPrefs {
    private const val PREF_NAME = "group_khatmah_prefs"

    fun loadAssignments(context: Context): List<JuzAssignment> {
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return (1..30).map { juz ->
            val name = sp.getString("juz_${juz}_name", "") ?: ""
            val done = sp.getBoolean("juz_${juz}_done", false)
            JuzAssignment(juzNumber = juz, readerName = name, isCompleted = done)
        }
    }

    fun saveJuz(context: Context, juzNumber: Int, name: String, isCompleted: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString("juz_${juzNumber}_name", name)
            .putBoolean("juz_${juzNumber}_done", isCompleted)
            .apply()
    }
}

@Composable
fun GroupKhatmahDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var khatmahTitle by remember { mutableStateOf("ختمة الأهل والأحباب المباركة") }
    val assignments = remember { mutableStateListOf<JuzAssignment>().apply { addAll(GroupKhatmahPrefs.loadAssignments(context)) } }

    var selectedJuzToEdit by remember { mutableStateOf<JuzAssignment?>(null) }
    var editNameText by remember { mutableStateOf("") }

    val completedCount = assignments.count { it.isCompleted }
    val assignedCount = assignments.count { it.readerName.isNotBlank() }

    fun shareStatusOnWhatsApp() {
        val message = buildString {
            append("📖 *${khatmahTitle}*\n")
            append("نسبة الإنجاز: $completedCount من ٣٠ جزءاً (${(completedCount * 100 / 30)}%)\n\n")
            append("📋 *جدول الأجزاء:*\n")
            assignments.forEach { j ->
                val statusSymbol = if (j.isCompleted) "✅ تم الإنجاز" else if (j.readerName.isNotBlank()) "⏳ قيد القراءة (${j.readerName})" else "⚪ متاح للحجز"
                append("الجزء ${j.juzNumber}: $statusSymbol\n")
            }
            append("\n✨ تم التنسيق عبر تطبيق الورد اليومي — رفيق المسلم")
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(Intent.createChooser(shareIntent, "مشاركة جدول الختمة الجماعية"))
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
                            text = "🤝 الختمة الجماعية وتوزيع الأجزاء",
                            style = MaterialTheme.typography.titleMedium,
                            color = IslamicGoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تنظيم ختمة مشتركة للأهل والعائلة",
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
                        Icon(Icons.Default.Group, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Card
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
                            Column {
                                Text(
                                    text = khatmahTitle,
                                    color = IslamicGoldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "تم إنجاز $completedCount من ٣٠ جزءاً • محجوز: $assignedCount",
                                    color = IslamicMintLight,
                                    fontSize = 11.5.sp
                                )
                            }

                            val percentage = (completedCount * 100 / 30)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF133E2F),
                                border = BorderStroke(1.dp, IslamicGoldPrimary)
                            ) {
                                Text(
                                    text = "$percentage%",
                                    color = IslamicGoldPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { completedCount / 30f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = IslamicGoldPrimary,
                            trackColor = Color(0xFF0E2A20)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "الأجزاء الـ ٣٠ (اضغط على أي جزء لحجزه أو تسجيل إنجازه):",
                    color = IslamicGoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 30 Juz Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(assignments) { juz ->
                        val isAssigned = juz.readerName.isNotBlank()
                        val isDone = juz.isCompleted

                        val bgColor = when {
                            isDone -> Color(0xFF144525)
                            isAssigned -> Color(0xFF0E2C20)
                            else -> Color(0xFF071B14)
                        }

                        val borderColor = when {
                            isDone -> Color(0xFF4CAF50)
                            isAssigned -> IslamicGoldPrimary
                            else -> IslamicBorderGold
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = bgColor,
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier
                                .clickable {
                                    selectedJuzToEdit = juz
                                    editNameText = juz.readerName
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "جزء ${juz.juzNumber}",
                                        color = if (isDone) Color(0xFFA5D6A7) else IslamicGoldLight,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    if (isDone) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(14.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = if (isDone) "تم الإنجاز ✓"
                                    else if (isAssigned) juz.readerName
                                    else "متاح للحجز",
                                    color = if (isDone) Color(0xFFC8E6C9) else if (isAssigned) Color.White else Color.Gray,
                                    fontSize = 10.5.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // WhatsApp Sharing Button
                Button(
                    onClick = { shareStatusOnWhatsApp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF061A14), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "مشاركة جدول الختمة على الواتساب 📢",
                        color = Color(0xFF061A14),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }

    // Edit Juz Dialog
    if (selectedJuzToEdit != null) {
        val juz = selectedJuzToEdit!!
        AlertDialog(
            onDismissRequest = { selectedJuzToEdit = null },
            containerColor = Color(0xFF09241B),
            title = {
                Text("تعديل الجزء رقم ${juz.juzNumber}", color = IslamicGoldPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editNameText,
                        onValueChange = { editNameText = it },
                        label = { Text("اسم القارئ (مثال: أحمد، سارة)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IslamicGoldPrimary,
                            unfocusedBorderColor = IslamicBorderGold,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("تمت قراءة الجزء بالكامل:", color = IslamicGoldLight, fontSize = 13.sp)
                        Switch(
                            checked = juz.isCompleted,
                            onCheckedChange = { isDone ->
                                val index = assignments.indexOfFirst { it.juzNumber == juz.juzNumber }
                                if (index != -1) {
                                    val updated = juz.copy(isCompleted = isDone)
                                    assignments[index] = updated
                                    selectedJuzToEdit = updated
                                    GroupKhatmahPrefs.saveJuz(context, juz.juzNumber, editNameText, isDone)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = IslamicGoldPrimary,
                                checkedTrackColor = Color(0xFF0E3D2C)
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val index = assignments.indexOfFirst { it.juzNumber == juz.juzNumber }
                        if (index != -1) {
                            assignments[index] = juz.copy(readerName = editNameText.trim())
                            GroupKhatmahPrefs.saveJuz(context, juz.juzNumber, editNameText.trim(), juz.isCompleted)
                        }
                        selectedJuzToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGoldPrimary)
                ) {
                    Text("حفظ التغييرات", color = Color(0xFF061A14), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedJuzToEdit = null }) {
                    Text("إلغاء", color = IslamicTextSecondary)
                }
            }
        )
    }
}
