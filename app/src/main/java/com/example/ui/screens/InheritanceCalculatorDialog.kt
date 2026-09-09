package com.example.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.components.IosGrabberHandle
import com.example.ui.components.IosSegmentedControl
import com.example.ui.theme.*

data class HeirShareResult(
    val heirTitle: String,
    val shareFractionText: String,
    val shareAmount: Double,
    val note: String
)

@Composable
fun InheritanceCalculatorDialog(
    onDismiss: () -> Unit
) {
    var deceasedGender by remember { mutableIntStateOf(0) } // 0: المتوفى رجل, 1: المتوفاة امرأة
    var estateAmountText by remember { mutableStateOf("100000") }

    var hasSpouse by remember { mutableStateOf(true) } // زوجة لو رجل، أو زوج لو امرأة
    var sonsCount by remember { mutableIntStateOf(2) }
    var daughtersCount by remember { mutableIntStateOf(2) }
    var hasFather by remember { mutableStateOf(true) }
    var hasMother by remember { mutableStateOf(true) }

    var calculationResults by remember { mutableStateOf<List<HeirShareResult>?>(null) }

    fun calculateShares() {
        val totalEstate = estateAmountText.toDoubleOrNull() ?: 0.0
        if (totalEstate <= 0.0) return

        val results = mutableListOf<HeirShareResult>()
        val hasChildren = (sonsCount + daughtersCount) > 0
        var remainingEstate = totalEstate

        // 1. Mother share
        if (hasMother) {
            val motherFraction = if (hasChildren) (1.0 / 6.0) else (1.0 / 3.0)
            val motherShare = totalEstate * motherFraction
            results.add(HeirShareResult("الأم", if (hasChildren) "السدس (١/٦)" else "الثلث (١/٣)", motherShare, "لوجود فرع وارث (أولاد)" ))
            remainingEstate -= motherShare
        }

        // 2. Father share
        if (hasFather) {
            val fatherFraction = if (hasChildren) (1.0 / 6.0) else 0.0
            val fatherShare = if (hasChildren) totalEstate * fatherFraction else 0.0
            if (hasChildren) {
                results.add(HeirShareResult("الأب", "السدس فرضاً (١/٦)", fatherShare, "لوجود فرع وارث ذكر أو أنثى"))
                remainingEstate -= fatherShare
            }
        }

        // 3. Spouse share
        if (hasSpouse) {
            if (deceasedGender == 0) {
                // Deceased is male -> Wife inherits
                val wifeFraction = if (hasChildren) (1.0 / 8.0) else (1.0 / 4.0)
                val wifeShare = totalEstate * wifeFraction
                results.add(HeirShareResult("الزوجة", if (hasChildren) "الثمن (١/٨)" else "الربع (١/٤)", wifeShare, "فرضه المقدر في كتاب الله"))
                remainingEstate -= wifeShare
            } else {
                // Deceased is female -> Husband inherits
                val husbandFraction = if (hasChildren) (1.0 / 4.0) else (1.0 / 2.0)
                val husbandShare = totalEstate * husbandFraction
                results.add(HeirShareResult("الزوج", if (hasChildren) "الربع (١/٤)" else "النصف (١/٢)", husbandShare, "فرضه المقدر في كتاب الله"))
                remainingEstate -= husbandShare
            }
        }

        // 4. Children (Asabah - للذكر مثل حظ الأنثيين)
        if (hasChildren) {
            val totalParts = (sonsCount * 2) + daughtersCount
            if (totalParts > 0 && remainingEstate > 0) {
                val onePartAmount = remainingEstate / totalParts
                if (sonsCount > 0) {
                    val eachSonAmount = onePartAmount * 2
                    val allSonsAmount = eachSonAmount * sonsCount
                    results.add(HeirShareResult("الأبناء الذكور ($sonsCount)", "عصبة بالغير (سهمان لكل ابن)", allSonsAmount, "نصيب كل ابن: ${String.format("%,.0f", eachSonAmount)}"))
                }
                if (daughtersCount > 0) {
                    val eachDaughterAmount = onePartAmount
                    val allDaughtersAmount = eachDaughterAmount * daughtersCount
                    results.add(HeirShareResult("البنات ($daughtersCount)", "عصبة بالغير (سهم لكل بنت)", allDaughtersAmount, "نصيب كل بنت: ${String.format("%,.0f", eachDaughterAmount)}"))
                }
                remainingEstate = 0.0
            }
        } else {
            // No children -> Father takes remainder as Asabah
            if (hasFather && remainingEstate > 0) {
                results.add(HeirShareResult("الأب", "الباقي تعصيباً", remainingEstate, "يأخذ باقي التركة تعصيباً لعدم وجود فرع وارث"))
                remainingEstate = 0.0
            }
        }

        calculationResults = results
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
                            text = "⚖️ حاسبة المواريث والتركات الشرعية",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "توزيع الفروض والمقادير وفق سورة النساء",
                            fontSize = 11.sp,
                            color = IosTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x2EE2B84D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("المتوفى: رجل 👨", "المتوفاة: امرأة 👩"),
                    selectedIndex = deceasedGender,
                    onSelectIndex = {
                        deceasedGender = it
                        calculationResults = null
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Estate Amount Input Card
                    item {
                        GlassCard(backgroundColor = IosCardGlass, modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Text("إجمالي قيمة التركة النقدية:", color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = estateAmountText,
                                    onValueChange = { estateAmountText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = IosGoldApple,
                                        unfocusedBorderColor = IosBorderGlass,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    // Heirs Config Card
                    item {
                        GlassCard(backgroundColor = IosCardGlass, modifier = Modifier.fillMaxWidth()) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("تحديد الورثة الأحياء:", color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                                // Spouse toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(if (deceasedGender == 0) "الزوجة على قيد الحياة" else "الزوج على قيد الحياة", color = Color.White, fontSize = 12.5.sp)
                                    Switch(
                                        checked = hasSpouse,
                                        onCheckedChange = { hasSpouse = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = IosGoldApple, checkedTrackColor = Color(0xFF133E2F))
                                    )
                                }

                                // Father toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("الأب على قيد الحياة", color = Color.White, fontSize = 12.5.sp)
                                    Switch(
                                        checked = hasFather,
                                        onCheckedChange = { hasFather = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = IosGoldApple, checkedTrackColor = Color(0xFF133E2F))
                                    )
                                }

                                // Mother toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("الأم على قيد الحياة", color = Color.White, fontSize = 12.5.sp)
                                    Switch(
                                        checked = hasMother,
                                        onCheckedChange = { hasMother = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = IosGoldApple, checkedTrackColor = Color(0xFF133E2F))
                                    )
                                }

                                HorizontalDivider(color = IosSeparator, thickness = 0.5.dp)

                                // Sons Counter
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("عدد الأبناء (الذكور): $sonsCount", color = Color.White, fontSize = 12.5.sp)
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        IconButton(onClick = { if (sonsCount > 0) sonsCount -= 1 }, modifier = Modifier.size(30.dp).background(Color(0x33FFFFFF), CircleShape)) {
                                            Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(onClick = { sonsCount += 1 }, modifier = Modifier.size(30.dp).background(Color(0xFF133E2F), CircleShape)) {
                                            Icon(Icons.Default.Add, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }

                                // Daughters Counter
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("عدد البنات (الإناث): $daughtersCount", color = Color.White, fontSize = 12.5.sp)
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        IconButton(onClick = { if (daughtersCount > 0) daughtersCount -= 1 }, modifier = Modifier.size(30.dp).background(Color(0x33FFFFFF), CircleShape)) {
                                            Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(onClick = { daughtersCount += 1 }, modifier = Modifier.size(30.dp).background(Color(0xFF133E2F), CircleShape)) {
                                            Icon(Icons.Default.Add, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Calculation Results
                    if (calculationResults != null) {
                        item {
                            Text("نتيجة توزيع التركة الشرعية:", color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        items(calculationResults!!) { res ->
                            GlassCard(backgroundColor = Color(0xFF0D241B), modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(res.heirTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                        Text(res.shareFractionText, color = IosEmeraldPro, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                                        Text(res.note, color = IosTextSecondary, fontSize = 10.sp)
                                    }
                                    Text(
                                        text = "${String.format("%,.0f", res.shareAmount)}",
                                        color = IosGoldApple,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { calculateShares() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IosGoldApple)
                ) {
                    Text("حساب وتوزيع التركة الآن ⚡", color = Color(0xFF040A07), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
