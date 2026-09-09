package com.example.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class VoiceDhikrReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "VoiceDhikrReceiver"
        const val ACTION_VOICE_DHIKR = "com.example.action.VOICE_DHIKR"

        private val DHIKR_PHRASES = listOf(
            "اللَّهُمَّ صَلِّ وَسَلِّمْ وَبَارِكْ عَلَى نَبِيِّنَا مُحَمَّدٍ",
            "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            "لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
            "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            "سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلَا إِلٰهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ"
        )

        fun scheduleNext(context: Context, intervalMinutes: Int) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, VoiceDhikrReceiver::class.java).apply {
                action = ACTION_VOICE_DHIKR
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                10088,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )

            val triggerAt = System.currentTimeMillis() + (intervalMinutes * 60 * 1000L)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
                }
                Log.d(TAG, "Scheduled voice dhikr in $intervalMinutes minutes")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to schedule voice dhikr: ${e.message}")
            }
        }

        fun cancelSchedule(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, VoiceDhikrReceiver::class.java).apply {
                action = ACTION_VOICE_DHIKR
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                10088,
                intent,
                PendingIntent.FLAG_NO_CREATE or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
                Log.d(TAG, "Cancelled voice dhikr alarm")
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("voice_dhikr_prefs", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("enabled", false)
        if (!isEnabled) return

        val intervalMinutes = prefs.getInt("interval_minutes", 30)
        val selectedPhraseIndex = prefs.getInt("phrase_index", 0) // 0: random, 1..N specific

        // Check quiet hours
        val quietStartHour = prefs.getInt("quiet_start", 23)
        val quietEndHour = prefs.getInt("quiet_end", 6)
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val inQuietHours = if (quietStartHour > quietEndHour) {
            currentHour >= quietStartHour || currentHour < quietEndHour
        } else {
            currentHour in quietStartHour until quietEndHour
        }

        if (!inQuietHours) {
            val phrase = if (selectedPhraseIndex == 0) {
                DHIKR_PHRASES.random()
            } else {
                DHIKR_PHRASES.getOrElse(selectedPhraseIndex - 1) { DHIKR_PHRASES[0] }
            }

            speakDhikr(context, phrase)
        }

        // Reschedule next
        scheduleNext(context, intervalMinutes)
    }

    private fun speakDhikr(context: Context, text: String) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("ar")
                tts?.setSpeechRate(0.85f)
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "dhikr_utterance")
            }
        }
    }
}
