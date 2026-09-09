package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.data.ai.IslamwebAiService
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AiChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "الآن"
)

object IslamicAiKnowledge {
    fun getLocalSpiritualAnswer(query: String): String {
        val q = query.trim()
        return when {
            q.contains("فجر") || q.contains("الفجر") -> {
                "✨ **كيفية المحافظة على صلاة الفجر:**\n\n" +
                "١. **الصدق في النية:** اعقد العزم الصادق قبل النوم على الاستيقاظ.\n" +
                "٢. **النوم على طهارة وأذكار النوم:** قراءة آية الكرسي والمعوذات ودعاء النوم.\n" +
                "٣. **الأخذ بالأسباب المادية:** التبكير بالنوم، ضبط منبهين بعيداً عن متناول اليد، وتجنب السهر المضر.\n" +
                "٤. **استشعار الأجر العظيم:** قال النبي ﷺ: «من صلى الصبح فهو في ذمة الله» (رواه مسلم)، وركعتا الفجر خير من الدنيا وما فيها.\n" +
                "٥. **الدعاء:** الإلحاح على الله بأن يوفقك لشهود الفجر مع الجماعة."
            }
            q.contains("الكهف") || q.contains("كهف") -> {
                "📖 **فضائل ودروس سورة الكهف:**\n\n" +
                "• **العصمة من الفتن:** تعالج السورة أربع فتن كبرى (فتنة الدين في أصحاب الكهف، فتنة المال في صاحب الجنتين، فتنة العلم في موسى والخضر، وفتنة السلطة في ذي القرنين).\n" +
                "• **سفينة النجاة:** تقرأ كل جمعة لتضيء للمؤمن نوراً ما بين الجمعتين، وتعصم من فتنة المسيح الدجال.\n" +
                "• **العبرة الخالدة:** الثقة بحكمة الله وتدبيره الخفي في كل ما يقضيه."
            }
            q.contains("النيات") || q.contains("الأعمال بالنيات") -> {
                "📜 **شرح حديث (إنما الأعمال بالنيات):**\n\n" +
                "عن عمر بن الخطاب رضي الله عنه قال: سمعت رسول الله ﷺ يقول: «إنما الأعمال بالنيات، وإنما لكل امرئ ما نوى...» (متفق عليه).\n\n" +
                "• يُعد هذا الحديث ثلث الإسلام، وعليه مدار قبول الطاعات.\n" +
                "• النية تحوّل العادات (كالنوم والأكل والرياضة) إلى عبادات وقربات يؤجر عليها المسلم إذا نوى بها التقوي على طاعة الله."
            }
            q.contains("هم") || q.contains("دعاء") || q.contains("حزن") || q.contains("تفريج") -> {
                "🤲 **أدعية جامعة لتفريج الهم وتيسير الأمور:**\n\n" +
                "• دعاء ذي النون: «لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ».\n" +
                "• دعاء النبي ﷺ: «اللَّهُمَّ إِنِّي عَبْدُكَ، وَابْنُ عَبْدِكَ، وَابْنُ أَمَتِكَ، نَاصِيَتِي بِيَدِكَ، مَاضٍ فِيَّ حُكْمُكَ، عَدْلٌ فِيَّ قَضَاؤُكَ... أن تجعل القرآن العظيم ربيع قلبي ونور صدري وجلاء حزني وذهاب همي».\n" +
                "• «حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ» (يكفيك الله بها ما أهمّك)."
            }
            q.contains("الجمعة") || q.contains("جمعة") -> {
                "🕌 **سنن يوم الجمعة المبارك:**\n\n" +
                "١. الاغتسال والتطيب ولبس أحسن الثياب.\n" +
                "٢. التبكير إلى المسجد والمشي إليه بسكينة.\n" +
                "٣. قراءة سورة الكهف.\n" +
                "٤. الإكثار من الصلاة والسلام على النبي ﷺ.\n" +
                "٥. تحري ساعة الإجابة (آخر ساعة بعد صلاة العصر حتى غروب الشمس)."
            }
            else -> {
                "🌿 **إجابة وتأمل مبارك في سؤالك:**\n\n" +
                "الحمد لله، والصلاة والسلام على رسول الله.\n" +
                "بخصوص استفسارك الكريم: الإسلام دين اليسر والسماحة، وقد حثنا ديننا الحنيف على التمسك بكتاب الله وسنة رسوله ﷺ، واستفتاء أهل العلم الموثوقين في دقائق الفروع.\n\n" +
                "قال تعالى: ﴿فَاسْأَلُوا أَهْلَ الذِّكْرِ إِن كُنتُمْ لَا تَعْلَمُونَ﴾ [النحل: 43].\n" +
                "ويمكنك مراجعة قسم الفتاوى في التطبيق للبحث في موسوعة إسلام ويب الشاملة بأرقام الفتاوى المعتمدة."
            }
        }
    }
}

@Composable
fun IslamicAiCompanionDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            AiChatMessage(
                id = "welcome",
                text = "السلام عليكم ورحمة الله وبركاته يا أخي المبارك. 🌿\nأنا رفيقك الإسلامي الذكي، يمكنك سؤالي عن تدبر الآيات، وشرح الأحاديث الشريفة، وسير الصالحين، وأدعية تفريج الكرب.",
                isUser = false
            )
        )
    }

    val suggestedQuestions = listOf(
        "كيف أحافظ على صلاة الفجر؟",
        "فضائل ودروس سورة الكهف",
        "شرح حديث إنما الأعمال بالنيات",
        "أدعية جامعة لتفريج الهم والكرب",
        "سنن وآداب يوم الجمعة"
    )

    fun sendMessage(query: String) {
        val q = query.trim()
        if (q.isBlank() || isLoading) return

        messages.add(AiChatMessage(id = System.currentTimeMillis().toString(), text = q, isUser = true))
        inputText = ""
        isLoading = true

        coroutineScope.launch {
            delay(600) // Realistic conversational pause
            val answer = IslamicAiKnowledge.getLocalSpiritualAnswer(q)
            messages.add(
                AiChatMessage(
                    id = (System.currentTimeMillis() + 1).toString(),
                    text = answer,
                    isUser = false
                )
            )
            isLoading = false
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Islamic Reflection", text)
        clipboard.setPrimaryClip(clip)
        viewModel.showNotification("تم النسخ", "تم نسخ الإجابة بنجاح إلى الحافظة.")
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "المساعد الإسلامي الذكي",
                                style = MaterialTheme.typography.titleMedium,
                                color = IslamicGoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "إجابات موثقة وتدبرات إيمانية مستنيرة",
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
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = IslamicGoldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Suggested Prompt Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(suggestedQuestions) { prompt ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF0C2B20),
                            border = BorderStroke(0.8.dp, IslamicBorderGold),
                            modifier = Modifier.clickable { sendMessage(prompt) }
                        ) {
                            Text(
                                text = prompt,
                                color = IslamicGoldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Chat Messages List
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(messages) { msg ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                        ) {
                            if (!msg.isUser) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(IslamicGoldPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF061A14), modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (msg.isUser) 16.dp else 4.dp,
                                    bottomEnd = if (msg.isUser) 4.dp else 16.dp
                                ),
                                color = if (msg.isUser) Color(0xFF133E2F) else Color(0xFF0B241C),
                                border = BorderStroke(1.dp, if (msg.isUser) IslamicGoldPrimary else IslamicBorderGold),
                                modifier = Modifier.widthIn(max = 290.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = msg.text,
                                        color = if (msg.isUser) Color.White else IslamicTextPrimary,
                                        fontSize = 13.sp,
                                        lineHeight = 19.sp
                                    )

                                    if (!msg.isUser) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            IconButton(
                                                onClick = { copyToClipboard(msg.text) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = IslamicGoldPrimary, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isLoading) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = IslamicGoldPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "جاري استحضار الإجابة الإيمانية الموثقة...",
                                    color = IslamicTextSecondary,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Input Field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("اكتب سؤالك أو استفسارك هنا...", color = Color.Gray, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IslamicGoldPrimary,
                            unfocusedBorderColor = IslamicBorderGold,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF092219),
                            unfocusedContainerColor = Color(0xFF092219)
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { sendMessage(inputText) },
                        modifier = Modifier
                            .size(46.dp)
                            .background(IslamicGoldPrimary, CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "إرسال", tint = Color(0xFF061A14), modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
    }
}
