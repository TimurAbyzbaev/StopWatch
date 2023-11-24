package com.example.stopwatch.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.stopwatch.R
import com.example.stopwatch.viewmodel.BaseViewModel
import com.example.stopwatch.viewmodel.TimerModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CurrentTimerNotification : BaseViewModel() {
    private lateinit var context: Context
    private lateinit var currentTimerModel: TimerModel
    private val notificationId = 0
    private val channelId = "stopwatch_channel"
    private val channelName = "Stopwatch Channel"
    var notificationActive = false

    lateinit var notificationBuilder: NotificationCompat.Builder

    fun setContext(context: Context) {
        this.context = context
    }

    fun setCurrentTimer(timerModel: TimerModel) {
        currentTimerModel = timerModel
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun createNotification() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(currentTimerModel.name)
            .setContentText("Текущее время: 00:00:000")
            .addAction(R.drawable.baseline_play_circle_outline_24, "Старт", null)
            .addAction(R.drawable.baseline_pause_circle_outline_24, "Пауза", null)
            .addAction(R.drawable.baseline_stop_circle_24, "Сброс", null)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationBuilder.setCategory(Notification.CATEGORY_STOPWATCH)
        }

        val notification = notificationBuilder.build()

        if (!hasNotificationPermission()) {
            requestNotificationPermission()
            return
        }

        NotificationManagerCompat.from(context).notify(notificationId, notification)
        notificationActive = true

        // Обновление уведомления при изменении значения времени
        viewModelCoroutineScope.launch {
            startUpdateValue()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun startUpdateValue() {
        while (notificationActive) {
            updateNotification()
            delay(1000)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasNotificationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            PERMISSION_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun updateNotification() {
        val currentValue = currentTimerModel.subscribeToValue().value
        notificationBuilder
            .setContentTitle(currentTimerModel.name)
            .setContentText("Текущее время: $currentValue")

        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(context)
                .notify(notificationId, notificationBuilder.build())
        }
    }

    fun cancelNotification() {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123 // Уникальный код запроса разрешения
    }
}