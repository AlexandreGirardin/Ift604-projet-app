package com.ift604.projectapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject


class MatchService : Service() {

    private var latestNumberOfLikes = 0
    private var notificationId = 0
    var serviceRunning = true

    override fun onCreate() {
        var matches: JSONArray
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                latestNumberOfLikes = ApiClient.getApiMatches().length()
                while (serviceRunning)
                {
                    matches = ApiClient.getApiMatches()
                    if (latestNumberOfLikes < matches.length())
                    {
                        showNotification(matches.getJSONObject(matches.length() - 1))
                        latestNumberOfLikes = matches.length()
                    }
                    delay(1000)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("SERVICE START!!!!!!!!!!!!!")
        return START_STICKY
    }

    override fun onDestroy() { // Cancel the persistent notification.
        serviceRunning = false
        Toast.makeText(this, "LikeService stopped", Toast.LENGTH_SHORT).show()
    }


    /**
     * Show a notification while this service is running.
     */
    private fun showNotification(jsonObject: JSONObject) {
        // The PendingIntent to launch our activity if the user selects this notification
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), 0
        )
        val builder = NotificationCompat.Builder(this, "test")
            .setSmallIcon(R.drawable.heart_active)
            .setContentTitle("You matched with ${jsonObject.getString("name")}")
            .setContentText("Go see their profile!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("test", "channel name", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "channel description"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
            notificationId += 1
        }
    }
}
