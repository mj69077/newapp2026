package com.example.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.provider.Settings

object PrayerDndManager {
    private const val PREF_NAME = "prayer_dnd_prefs"
    private const val KEY_ENABLED = "dnd_enabled"
    private const val KEY_DURATION = "dnd_duration_min"
    private const val KEY_PRAYERS = "dnd_active_prayers"

    fun isAutoMuteEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, true)
    }

    fun setAutoMuteEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, enabled)
            .apply()
    }

    fun getMuteDurationMinutes(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_DURATION, 20)
    }

    fun setMuteDurationMinutes(context: Context, minutes: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_DURATION, minutes)
            .apply()
    }

    fun hasNotificationPolicyAccess(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            return nm?.isNotificationPolicyAccessGranted == true
        }
        return true
    }

    fun requestNotificationPolicyAccess(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    fun applyMute(context: Context) {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
            if (hasNotificationPolicyAccess(context) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                nm?.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
            } else {
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            }
        } catch (_: Exception) {}
    }

    fun restoreSound(context: Context) {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
            if (hasNotificationPolicyAccess(context) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                nm?.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        } catch (_: Exception) {}
    }
}
