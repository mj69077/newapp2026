package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.GlassCard
import com.example.ui.components.IosGrabberHandle
import com.example.ui.components.IosSegmentedControl
import com.example.ui.theme.*

data class KidsStep(val stepNumber: Int, val title: String, val instruction: String, val iconName: String)
data class KidsStory(val prophetName: String, val headline: String, val shortStory: String, val lesson: String)

@Composable
fun KidsIslamicCornerDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableIntStateOf(0) } // 0: الصلاة والوضوء, 1: قصص الصغار, 2: مسبحة البطل, 3: جدول النجوم

    var kidStars by remember { mutableIntStateOf(15) }
    var kidTasbihCount by remember { mutableIntStateOf(0) }
    var encouragementMessage by remember { mutableStateOf("ما شاء الله! أنت بطل رائع 🌟") }

    fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(40)
        }
    }

    val wuduSteps = listOf(
        KidsStep(1, "النية والبسملة وغسل الكفين", "أقول 'بسم الله' في قلبي وأغسل كفيّ ثلاث مرات بنظافة ونشاط.", "CleanHands"),
        KidsStep(2, "المضمضة والاستنشاق", "أتمضمض بالماء ثلاث مرات، وأستنشق الماء بأنفي برفق وأستنثر ثلاث مرات.", "Face"),
        KidsStep(3, "غسل الوجه كاملاً", "أغسل وجهي الجميل ثلاث مرات من منبت الشعر إلى أسفل الذقن.", "Face"),
        KidsStep(4, "غسل اليدين إلى المرفقين", "أبدأ بيدي اليمنى إلى الكوع ثلاث مرات، ثم يدي اليسرى ثلاث مرات.", "Hands"),
        KidsStep(5, "مسح الرأس والأذنين", "أمسح شعري برفق بيدي المبللتين مرة واحدة، ثم أمسح أذنيّ من الداخل والخارج.", "Head"),
        KidsStep(6, "غسل الرجلين إلى الكعبين", "أغسل رجلي اليمنى إلى الكعبين جيداً ثلاث مرات، ثم رجلي اليسرى ثلاث مرات.", "Feet")
    )

    val stories = listOf(
        KidsStory("سيدنا نوح والسفينة الكبيرة", "بناء السفينة في الصحراء", "أمر الله سيدنا نوح أن يبني سفينة كبيرة في أرض لا بحر فيها، وكان الناس يتعجبون منه، لكن سيدنا نوح كان يطيع الله بثقة. وعندما جاء الطوفان العظيم، ركب نوح والمؤمنون والحيوانات في السفينة ونجوا بسلام!", "طاعة الله دائماً تنجينا وتجلب لنا الخير."),
        KidsStory("سيدنا إبراهيم والنار الباردة", "الشجاعة والتوكل على الله", "كان قوم سيدنا إبراهيم يعبدون الأصنام، فحطمها إبراهيم ليفهموا أنها لا تنفع، فغضب الأشرار ووضعوه في نار ضخمة، فقال الله للنار: 'كوني برداً وسلاماً على إبراهيم'، فتحولت النار إلى روضة جميلة ولم تمسه بسوء!", "الله يحفظ عباده المؤمنين والشجعان دائماً."),
        KidsStory("سيدنا يونس في بطن الحوت", "الدعاء والاستغفار في الظلمات", "ابتلع الحوت الضخم سيدنا يونس في البحر، فجلس في بطن الحوت في ثلاث ظلمات، وظل يدعو: 'لا إله إلا أنت سبحانك إني كنت من الظالمين'، فاستجاب الله لدعائه وأمر الحوت أن يخرجه إلى الشاطئ بأمان!", "الدعاء والاستغفار يفرجان أصعب المواقف.")
    )

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
                            text = "👶 واحة براعم الإيمان للأبطال الصغار",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD54F)
                        )
                        Text(
                            text = "تعلم الصلاة والوضوء والقصص الرائعة",
                            fontSize = 11.5.sp,
                            color = IosTextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1B382B),
                        border = BorderStroke(1.dp, Color(0xFFFFD54F))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⭐ $kidStars", color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("الصلاة والوضوء", "قصص الأنبياء", "مسبحة البطل", "جدول النجوم"),
                    selectedIndex = activeTab,
                    onSelectIndex = {
                        activeTab = it
                        vibrate()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (activeTab) {
                    0 -> {
                        // Wudu and Salah Steps
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF0C2B20),
                                    border = BorderStroke(1.dp, Color(0x665DD9A9)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.WaterDrop, contentDescription = null, tint = IosEmeraldPro, modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "خطوات الوضوء السهلة لتكون نظيفاً ومحبوباً عند الله 🧼",
                                            color = Color.White,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            items(wuduSteps) { step ->
                                GlassCard(
                                    backgroundColor = IosCardGlass,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF133E2F)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${step.stepNumber}",
                                                color = IosGoldApple,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = step.title,
                                                color = IosGoldApple,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = step.instruction,
                                                color = IosTextPrimary,
                                                fontSize = 11.5.sp,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Illustrated Stories
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(stories) { story ->
                                GlassCard(
                                    backgroundColor = IosCardGlass,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = story.prophetName,
                                                color = Color(0xFFFFD54F),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.5.sp
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(0xFF1B382B)
                                            ) {
                                                Text(
                                                    text = story.headline,
                                                    color = IosEmeraldPro,
                                                    fontSize = 10.sp,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = story.shortStory,
                                            color = IosTextPrimary,
                                            fontSize = 12.5.sp,
                                            lineHeight = 19.sp
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF04140F)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("💡 ماذا نتعلم؟: ", color = IosGoldApple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                Text(story.lesson, color = Color(0xFFE0E0E0), fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Kids Fun Tasbih
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF143024),
                                border = BorderStroke(1.dp, Color(0xFFFFD54F))
                            ) {
                                Text(
                                    text = encouragementMessage,
                                    color = Color(0xFFFFE082),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }

                            // Giant Kids Button
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF124E38),
                                border = BorderStroke(5.dp, Color(0xFFFFD54F)),
                                modifier = Modifier
                                    .size(220.dp)
                                    .clickable {
                                        kidTasbihCount += 1
                                        vibrate()
                                        if (kidTasbihCount % 10 == 0) {
                                            kidStars += 1
                                            encouragementMessage = "رائع جداً! ربحت نجمة جديدة ⭐ ($kidStars)"
                                        } else if (kidTasbihCount % 5 == 0) {
                                            encouragementMessage = "واصل يا بطل، الله يحبك! 💖"
                                        }
                                    }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "$kidTasbihCount",
                                        fontSize = 62.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFFD54F)
                                    )
                                    Text(
                                        text = "سُبْحَانَ اللَّهِ 🎈",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    kidTasbihCount = 0
                                    encouragementMessage = "هيا نبدأ جولة تسبيح جديدة ممتعة!"
                                    vibrate()
                                },
                                modifier = Modifier.height(44.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF261818)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("تصفير ↺", color = Color(0xFFFFCDD2), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    3 -> {
                        // Star Reward Board
                        val tasks = listOf(
                            "صلاة الفجر في وقتها" to "نجمتان ⭐⭐",
                            "الوضوء بنشاط وإتقان" to "نجمة واحدة ⭐",
                            "مساعدة أمي وأبي في البيت" to "نجمتان ⭐⭐",
                            "الابتسامة والقول الطيب" to "نجمة واحدة ⭐",
                            "قراءة سورة الفاتحة والإخلاص" to "نجمة واحدة ⭐"
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF1E382B),
                                    border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("رصيدك الحالي من النجوم:", color = IosTextSecondary, fontSize = 12.sp)
                                        Text("$kidStars نجمة ذهبية ⭐", color = Color(0xFFFFD54F), fontSize = 24.sp, fontWeight = FontWeight.Black)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("أنت في مرتبة: البطل المؤمن الصغير 🏆", color = IosEmeraldPro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            items(tasks) { (taskName, starsEarned) ->
                                GlassCard(
                                    backgroundColor = IosCardGlass,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(taskName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                        Text(starsEarned, color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
