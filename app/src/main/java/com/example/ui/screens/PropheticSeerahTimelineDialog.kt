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

data class SeerahEvent(
    val year: String,
    val title: String,
    val description: String,
    val category: String
)

object SeerahData {
    val timelineEvents = listOf(
        SeerahEvent("٥٧١ م (عام الفيل)", "المولد النبوي الشريف", "ولد النبي ﷺ يتيم الأب في مكة المكرمة يوم الإثنين ١٢ ربيع الأول، وأرضعته حليمة السعدية في ديار بني سعد.", "الميلاد والنشأة"),
        SeerahEvent("٥٧٧ م (٦ سنوات)", "وفاة الأم وكفالة الجد", "توفيت أمه آمنة بنت وهب بالأبواء بين مكة والمدينة، فكفله جده عبد المطلب وحاطه بعنايته ورعايته.", "الميلاد والنشأة"),
        SeerahEvent("٥٧٩ م (٨ سنوات)", "وفاة الجد وكفالة العم أبي طالب", "توفي عبد المطلب وكفله عمه أبو طالب وشاركه في رحلات التجارة إلى الشام والتقى الراهب بحيرا.", "الميلاد والنشأة"),
        SeerahEvent("٥٩٥ م (٢٥ سنة)", "الزواج من السيدة خديجة", "عُرف في مكة بالصادق الأمين، وخرج في تجارة لخديجة بنت خويلد رضي الله عنها، ثم تزوجها وكانت خير سند له.", "الشباب"),
        SeerahEvent("٦٠٥ م (٣٥ سنة)", "تجديد بناء الكعبة والحجر الأسود", "حكّمت قريش النبي ﷺ في وضع الحجر الأسود فبسط رداءه وأمر رؤساء القبائل بحمله معاً فدرأ الفتنة.", "الشباب"),
        SeerahEvent("٦١٠ م (٤٠ سنة)", "نزول الوحي في غار حراء", "نزل جبريل عليه السلام بقوله تعالى ﴿اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ﴾، وبدأت النبوة والرسالة الخاتمة.", "البعثة بمكة"),
        SeerahEvent("٦١٣ م (٣ نبوي)", "الجهر بالدعوة", "نزل قوله تعالى ﴿فَاصْدَعْ بِمَا تُؤْمَرُ﴾، فصعد الصفا ونادى قريشاً، وبدأ استضعاف المؤمنين وتعذيبهم.", "البعثة بمكة"),
        SeerahEvent("٦١٥ م (٥ نبوي)", "الهجرة الأولى إلى الحبشة", "أمر النبي ﷺ أصحابه بالهجرة إلى الحبشة لأن بها ملكاً عادلاً لا يُظلم عنده أحد (النجاشي).", "البعثة بمكة"),
        SeerahEvent("٦١٩ م (١٠ نبوي)", "عام الحزن ورحلة الطائف", "وفاة السيدة خديجة وعمه أبي طالب، وتوجهه إلى الطائف لدعوة ثقيف فقابلوه بالأذى فدعا دعاءه الخالد.", "البعثة بمكة"),
        SeerahEvent("٦٢١ م (١١ نبوي)", "الإسراء والمعراج", "أسرى الله به ليلاً من المسجد الحرام إلى المسجد الأقصى وعُرج به إلى السماوات العلى وفرضت الصلوات الخمس.", "البعثة بمكة"),
        SeerahEvent("٦٢٢ م (١ هـ)", "الهجرة النبوية الشريفة", "هجرته ﷺ مع أبي بكر إلى المدينة المنورة (يثرب)، وتأسيس المسجد النبوي الشريف والمؤاخاة بين المهاجرين والأنصار.", "العهد المدني"),
        SeerahEvent("٢ هـ (٦٢٤ م)", "غزوة بدر الكبرى (يوم الفرقان)", "أول مواجهة فاصلة بين الحق والباطل في ١٧ رمضان، ونصر الله المسلمين رغم قلة عددهم وعتادهم.", "الغزوات الكبرى"),
        SeerahEvent("٣ هـ (٦٢٥ م)", "غزوة أحد", "استشهد فيها حمزة بن عبد المطلب وسبعون من الصحابة بسبب نزول الرماة عن الجبل طلباً للغنائم.", "الغزوات الكبرى"),
        SeerahEvent("٥ هـ (٦٢٧ م)", "غزوة الأحزاب (الخندق)", "حفر المسلمين الخندق بإشارة سلمان الفارسي وحصار قريش والأحزاب للمدينة حتى رد الله كيدهم بالريح والملائكة.", "الغزوات الكبرى"),
        SeerahEvent("٦ هـ (٦٢٨ م)", "صلح الحديبية وبيعة الرضوان", "صلح بين المسلمين وقريش كان فتحاً مبيناً وهيأ الفرصة لمراسلة ملوك الأرض ودخول الناس في دين الله أفواجاً.", "العهد المدني"),
        SeerahEvent("٨ هـ (٦٣٠ م)", "فتح مكة العظيم", "دخل النبي ﷺ مكة فاتحاً مطأطئ الرأس حامداً الله، وحطم الأصنام وقال لأهل مكة: «اذهبوا فأنتم الطلقاء».", "الفتوحات"),
        SeerahEvent("١٠ هـ (٦٣٢ م)", "حجة الوداع", "حج النبي ﷺ بأكثر من مائة ألف مسلم، وخطب خطبته الجامعة الخالدة ونزل قوله تعالى: ﴿الْيَوْمَ أَكْمَلْتُ لَكُمْ دِينَكُمْ﴾.", "الختام المبارك"),
        SeerahEvent("١١ هـ (١٢ ربيع الأول)", "الوفاة والرفيق الأعلى", "وفاة خير خلق الله ﷺ في حجر عائشة رضي الله عنها بعد أن بلّغ الرسالة وأدى الأمانة ونصح الأمة.", "الختام المبارك")
    )

    val mothersOfBelievers = listOf(
        "خديجة بنت خويلد" to "أولى زوجاته وسنده العظيم، بشرها جبريل ببيت في الجنة من قصب لا صخب فيه ولا نصب.",
        "سودة بنت زمعة" to "تزوجها بعد خديجة لسترها ورعاية بيته، عُرفت بالجود وإيثار عائشة بيومها.",
        "عائشة بنت أبي بكر" to "حبيبة رسول الله والفقيهة العالمة التي روت أكثر من ألفي حديث عن النبي ﷺ.",
        "حفصة بنت عمر" to "الصوامة القوامة، ابنة الفاروق عمر، احتفظت بأول مصحف جُمع في عهد أبي بكر.",
        "زينب بنت خزيمة" to "أم المساكين لكثرة عطفها وإطعامها الفقراء، توفيت بعد زواجها بأشهر معدودة.",
        "أم سلمة (هند بنت أبي أمية)" to "صاحبة الرأي الصائب في صلح الحديبية والحكمة العظيمة.",
        "زينب بنت جحش" to "زوجها الله من فوق سبع سماوات بنص القرآن ﴿فَلَمَّا قَضَىٰ زَيْدٌ مِّنْهَا وَطَرًا زَوَّجْنَاكَهَا﴾.",
        "جويرية بنت الحارث" to "كانت أعظم امرأة بركة على قومها، أعتق المسلمون مائة بيت من قومها إكراماً لمصاهرتها.",
        "أم حبيبة (رملة بنت أبي سفيان)" to "هاجرت للحبشة وثبتت على دينها، وعقد عليها النبي ﷺ وهي بأرض الحبشة.",
        "صفية بنت حيي" to "من نسل هارون عليه السلام، جمعت بين الجمال والورع والتقى.",
        "ميمونة بنت الحارث" to "آخر زوجاته ﷺ، نزل فيها الوحي وشهدت لها عائشة بالتقوى وصلة الرحم."
    )

    val tenPromised = listOf(
        "أبو بكر الصديق" to "أفضل الأمة بعد نبيها، خليفة رسول الله، ورفيق الغار وصاحب الهجرة.",
        "عمر بن الخطاب" to "الفاروق، مفرق الحق من الباطل، وافقه القرآن في مواضع وفتحت في عهده بلاد فارس والشام ومصر.",
        "عثمان بن عفان" to "ذو النورين لتزوجه ابنتي النبي ﷺ (رقية ثم أم كلثوم)، جامع القرآن الكريم على رسم واحد.",
        "علي بن أبي طالب" to "ابن عم النبي ﷺ وزوج فاطمة الزهراء، أبو السبطين الحسن والحسين، وباب مدينة العلم.",
        "طلحة بن عبيد الله" to "طلحة الخير، وقى النبي ﷺ يوم أحد بيده حتى شُلت يده وسماه النبي ﷺ شهيداً يمشي على الأرض.",
        "الزبير بن العوام" to "حواريّ رسول الله ﷺ وابن عمته صفية، أول من سل سيفاً في سبيل الله.",
        "عبد الرحمن بن عوف" to "التاجر الصدوق، هاجر بلا مال فبورك له في تجارته وأنفق أموالاً طائلة في سبيل الله.",
        "سعد بن أبي وقاص" to "خال النبي ﷺ، أول من رمى بسهم في سبيل الله، قائد معركة القادسية وهازم الفرس.",
        "سعيد بن زيد" to "من السابقين الأولين في الإسلام، ابن عم عمر بن الخطاب وزوج أخته فاطمة.",
        "أبو عبيدة عامر بن الجراح" to "أمين هذه الأمة، قائد الفتوحات الإسلامية في الشام وصاحب الزهد العظيم."
    )
}

@Composable
fun PropheticSeerahTimelineDialog(
    onDismiss: () -> Unit
) {
    var activeTab by remember { mutableIntStateOf(0) } // 0: الخط الزمني, 1: شجرة النسب, 2: أمهات المؤمنين, 3: العشرة المبشرون

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
                            text = "📜 الخط الزمني للسيرة والشجرة النبوية",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IosGoldApple
                        )
                        Text(
                            text = "سيرة الحبيب المصطفى ﷺ من المولد إلى الرفيق الأعلى",
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
                        Icon(Icons.Default.AutoStories, contentDescription = null, tint = IosGoldApple, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                IosSegmentedControl(
                    items = listOf("الخط الزمني", "شجرة النسب", "أمهات المؤمنين", "العشرة المبشرون"),
                    selectedIndex = activeTab,
                    onSelectIndex = { activeTab = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (activeTab) {
                    0 -> {
                        // Chronological Timeline
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(SeerahData.timelineEvents) { event ->
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
                                                text = event.title,
                                                color = IosGoldApple,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0x332DD4BF)
                                            ) {
                                                Text(
                                                    text = event.year,
                                                    color = IosEmeraldPro,
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = event.description,
                                            color = IosTextPrimary,
                                            fontSize = 12.5.sp,
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "• مرحلة: ${event.category}",
                                            color = IosTextSecondary,
                                            fontSize = 10.5.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Prophetic Family Tree
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF0E2A1E),
                                    border = BorderStroke(1.dp, IosBorderHighlight),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("النَسَب الشريف لسيد المرسلين ﷺ", color = IosGoldApple, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "محمد بن عبد الله بن عبد المطلب بن هاشم بن عبد مناف بن قصي بن كلاب بن مرة بن كعب بن لؤي بن غالب بن فهر (قريش) بن مالك بن النضر بن كنانة بن خزيمة بن مدركة بن إلياس بن مضر بن نزار بن معد بن عدنان (من نسل إسماعيل بن إبراهيم عليهما السلام).",
                                            color = Color.White,
                                            fontSize = 12.5.sp,
                                            lineHeight = 20.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            item {
                                GlassCard(backgroundColor = IosCardGlass, modifier = Modifier.fillMaxWidth()) {
                                    Column {
                                        Text("أبناء النبي ﷺ:", color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("١. القاسم (وبه كان يُكنى أبا القاسم)\n٢. عبد الله (المُلقب بالطيب والطاهر)\n٣. إبراهيم (من السيدة مارية القبطية)", color = IosTextPrimary, fontSize = 12.sp, lineHeight = 18.sp)
                                    }
                                }
                            }

                            item {
                                GlassCard(backgroundColor = IosCardGlass, modifier = Modifier.fillMaxWidth()) {
                                    Column {
                                        Text("بنات النبي ﷺ:", color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("١. زينب (كبرى بناته، زوجة أبي العاص بن الربيع)\n٢. رقية (ذات الهجرتين، زوجة عثمان بن عفان)\n٣. أم كلثوم (تزوجها عثمان بعد وفاة رقية)\n٤. فاطمة الزهراء (سيدة نساء العالمين، زوجة علي بن أبي طالب وأم الحسن والحسين)", color = IosTextPrimary, fontSize = 12.sp, lineHeight = 18.sp)
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Mothers of the Believers
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(SeerahData.mothersOfBelievers) { (name, desc) ->
                                GlassCard(
                                    backgroundColor = IosCardGlass,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text("أم المؤمنين: $name رضي الله عنها", color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(desc, color = IosTextPrimary, fontSize = 12.sp, lineHeight = 18.sp)
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        // The Ten Promised Paradise
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(SeerahData.tenPromised) { (name, desc) ->
                                GlassCard(
                                    backgroundColor = IosCardGlass,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text("الصحابي الجليل: $name رضي الله عنه", color = IosGoldApple, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(desc, color = IosTextPrimary, fontSize = 12.sp, lineHeight = 18.sp)
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
