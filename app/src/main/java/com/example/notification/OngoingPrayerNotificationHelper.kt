package com.example.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R

object OngoingPrayerNotificationHelper {

    const val CHANNEL_ONGOING_PRAYER = "islamic_ongoing_prayer_channel"
    private const val NOTIF_ID_ONGOING = 1009
    private const val PREFS_NAME = "ongoing_prayer_prefs"
    private const val KEY_ENABLED = "ongoing_prayer_enabled"

    fun isOngoingEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, false)
    }

    fun setOngoingEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, enabled)
            .apply()

        if (!enabled) {
            cancelOngoingNotification(context)
        }
    }

    fun createOngoingChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                ?: return

            val ongoingChannel = NotificationChannel(
                CHANNEL_ONGOING_PRAYER,
                "إشعار الصلاة الحي المستمر",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "شريط تنبيه مستمر وهادئ يوضح الصلاة القادمة والوقت المتبقي"
                setShowBadge(false)
                enableVibration(false)
                enableLights(false)
            }
            notificationManager.createNotificationChannel(ongoingChannel)
        }
    }

    fun showOrUpdateOngoingNotification(
        context: Context,
        nextPrayerName: String = "العصر",
        nextPrayerTime: String = "03:30 م",
        remainingText: String = "متبقي حوالي ساعتين"
    ) {
        if (!isOngoingEnabled(context)) return

        createOngoingChannel(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            if (permission != PackageManager.PERMISSION_GRANTED) return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("INITIAL_TAB", "PRAYER")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIF_ID_ONGOING,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ONGOING_PRAYER)
            .setSmallIcon(R.drawable.ic_holy_kaaba)
            .setContentTitle("🕌 الصلاة القادمة: صلاة $nextPrayerName ($nextPrayerTime)")
            .setContentText("$remainingText • اضغط لفتح بوصلة القبلة والمواقيت")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🕌 الصلاة القادمة: صلاة $nextPrayerName في تمام $nextPrayerTime\n⏳ $remainingText\n✨ حافظ على صلاتك وتهيأ للوضوء قبل دخول الوقت.")
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setSilent(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID_ONGOING, notification)
        } catch (_: SecurityException) {}
    }

    fun cancelOngoingNotification(context: Context) {
        try {
            NotificationManagerCompat.from(context).cancel(NOTIF_ID_ONGOING)
        } catch (_: SecurityException) {}
    }
}
