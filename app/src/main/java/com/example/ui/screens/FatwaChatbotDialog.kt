package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.ai.ArabicFatwaChatbotEngine
import com.example.ui.components.GlassCard
import com.example.ui.components.IosGrabberHandle
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val sender: MessageSender,
    val text: String,
    val fatwaNumber: String? = null,
    val source: String? = null,
    val matchedKeywords: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageSender {
    USER,
    BOT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FatwaChatbotDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    var inputQuery by remember { mutableStateOf("") }
    var isBotTyping by remember { mutableStateOf(false) }

    val initialGreeting = ChatMessage(
        sender = MessageSender.BOT,
        text = "السلام عليكم ورحمة الله وبركاته، أنا مستشارك الشرعي الذكي (Arabic Fatwa ChatBot).\n\nاطرح أي سؤال فقهي أو شرعي وسأجيبك فوراً بالأدلة المعتمدة وفتاوى إسلام ويب الموثقة.",
        source = "مركز الفتوى - إسلام ويب (Arabic-Fatwa-ChatBot)"
    )

    val messages = remember {
        mutableStateListOf(initialGreeting)
    }

    // Initialize the NLP engine on launch
    LaunchedEffect(Unit) {
        ArabicFatwaChatbotEngine.initialize(context)
    }

    fun sendMessage(queryText: String) {
        val trimmed = queryText.trim()
        if (trimmed.isBlank() || isBotTyping) return

        messages.add(ChatMessage(sender = MessageSender.USER, text = trimmed))
        inputQuery = ""
        keyboardController?.hide()

        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
            isBotTyping = true
            delay(400) // Realistic typing feel

            val match = ArabicFatwaChatbotEngine.findBestAnswer(trimmed)
            isBotTyping = false

            if (match != null) {
                // Extract optional fatwa number
                var fatwaNum: String? = null
                val numMatch = Regex("(?:الفتوى|فتوى)\\s*(?:رقم\\s*)?(\\d{3,6})").find(match.entry.answer)
                if (numMatch != null) {
                    fatwaNum = numMatch.groupValues[1]
                }

                messages.add(
                    ChatMessage(
                        sender = MessageSender.BOT,
                        text = match.entry.answer,
                        fatwaNumber = fatwaNum,
                        source = match.entry.source,
                        matchedKeywords = match.matchedKeywords
                    )
                )
            } else {
                messages.add(
                    ChatMessage(
                        sender = MessageSender.BOT,
                        text = "الحمد لله والصلاة والسلام على رسول الله؛\n\nلم أجد فتوى مباشرة مطابقة لسؤالك بدقة في الذاكرة الفورية، يُرجى تجربة إعادة صياغة السؤال بكلمات فقهية شائعة (مثل: صلاة الاستخارة، المسح على الجوارب، حساب الادخار)، أو الرجوع إلى قسم البحث المباشر في فتاوى إسلام ويب.",
                        source = "إسلام ويب"
                    )
                )
            }
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = IosBgBlack
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IosBgBlack)
            ) {
                // Top Grabber & Navigation Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(IosDockBg)
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IosGrabberHandle()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x1FFFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = Color.White
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = IosGoldApple,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "المفتي الذكي (AI ChatBot)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IosTextPrimary
                                )
                            }
                            Text(
                                text = "موسوعة إسلام ويب • Arabic-Fatwa-ChatBot",
                                fontSize = 11.sp,
                                color = IosEmeraldPro
                            )
                        }

                        IconButton(
                            onClick = {
                                messages.clear()
                                messages.add(initialGreeting)
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x1FFFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "بدء محادثة جديدة",
                                tint = IosGoldApple
                            )
                        }
                    }
                }

                // Quick Suggestion Chips
                val suggestions = ArabicFatwaChatbotEngine.getFeaturedQuestions()
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestions) { prompt ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x22F5D372),
                            border = BorderStroke(1.dp, Color(0x44F5D372)),
                            modifier = Modifier.clickable { sendMessage(prompt) }
                        ) {
                            Text(
                                text = prompt,
                                color = IosGoldBright,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Divider(color = Color(0x1AFFFFFF), thickness = 0.5.dp)

                // Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    items(messages) { msg ->
                        ChatBubbleItem(message = msg, context = context)
                    }

                    if (isBotTyping) {
                        item {
                            TypingIndicatorBubble()
                        }
                    }
                }

                // Bottom Input Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding(),
                    color = Color(0xEB0A1813),
                    border = BorderStroke(1.dp, Color(0x28FFFFFF))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = inputQuery,
                            onValueChange = { inputQuery = it },
                            placeholder = {
                                Text(
                                    text = "اكتب سؤالك الشرعي هنا...",
                                    color = IosTextSecondary,
                                    fontSize = 13.5.sp
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(22.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0x33000000),
                                unfocusedContainerColor = Color(0x22000000),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = { sendMessage(inputQuery) })
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { sendMessage(inputQuery) },
                            enabled = inputQuery.isNotBlank() && !isBotTyping,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (inputQuery.isNotBlank()) IosGoldApple else Color(0x26FFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "إرسال",
                                tint = if (inputQuery.isNotBlank()) Color(0xFF040A07) else Color(0x66FFFFFF),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(message: ChatMessage, context: Context) {
    val isUser = message.sender == MessageSender.USER

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            color = if (isUser) Color(0xFF1E3A2F) else Color(0xFF10241B),
            border = BorderStroke(
                1.dp,
                if (isUser) Color(0x552DD4BF) else Color(0x44F5D372)
            ),
            modifier = Modifier.widthIn(max = 340.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = null,
                            tint = IosGoldApple,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "المفتي الشرعي (إسلام ويب)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                    }
                }

                Text(
                    text = message.text,
                    fontSize = if (isUser) 14.sp else 13.5.sp,
                    lineHeight = 22.sp,
                    color = Color.White
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0x18FFFFFF), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message.source ?: "مركز الفتوى",
                            fontSize = 10.sp,
                            color = IosTextSecondary
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Copy Action
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Fatwa", message.text))
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = "نسخ",
                                    tint = IosGoldBright,
                                    modifier = Modifier.size(15.dp)
                                )
                            }

                            // Share Action
                            IconButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "🕌 فتوى شرعية من تطبيق الورد اليومي:\n\n${message.text}")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "مشاركة الفتوى"))
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "مشاركة",
                                    tint = IosGoldBright,
                                    modifier = Modifier.size(15.dp)
                                )
                            }

                            // Open in Islamweb if fatwa number exists
                            if (message.fatwaNumber != null) {
                                IconButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.islamweb.net/ar/fatwa/${message.fatwaNumber}"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.OpenInNew,
                                        contentDescription = "رابط الفتوى",
                                        tint = IosEmeraldPro,
                                        modifier = Modifier.size(15.dp)
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

@Composable
private fun TypingIndicatorBubble() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF10241B),
        border = BorderStroke(1.dp, Color(0x33F5D372)),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = IosGoldApple,
                strokeWidth = 2.dp
            )
            Text(
                text = "جاري استحضار الفتوى الشرعية والدليل...",
                fontSize = 11.5.sp,
                color = IosGoldBright
            )
        }
    }
}
