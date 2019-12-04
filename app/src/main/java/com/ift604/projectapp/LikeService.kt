package com.ift604.projectapp

import android.app.*
import android.content.Context
import com.ift604.projectapp.R
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*
import org.json.JSONArray
import java.lang.Exception
import kotlin.random.Random


class LikeService : Service() {

    private val latestNumberOfLikes = 0
    private var notificationId = 0
    private var serviceRunning = true

    override fun onCreate() {
        var test: JSONArray
        ApiClient.postApiLogin("alex@gmail.com", "12345")
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                while (serviceRunning)
                {
                    test = ApiClient.getApiSwipe()
                    println(test)
                    if (test.getJSONObject(0).getInt("id") == 3)
                    {
                        showNotification();
                    }
                    delay(5000)
                }
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("SERVICE START!!!!!!!!!!!!!")

        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    override fun onDestroy() { // Cancel the persistent notification.
        serviceRunning = false
        Toast.makeText(this, "LikeService stopped", Toast.LENGTH_SHORT).show()
    }


    /**
     * Show a notification while this service is running.
     */
    private fun showNotification() { // In this sample, we'll use the same text for the ticker and the expanded notification
        val text = "Send UdeS"
        // The PendingIntent to launch our activity if the user selects this notification
        /*val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, LocalServiceActivities.Controller::class.java), 0
        )*/
        val builder = NotificationCompat.Builder(this, "test")
            .setSmallIcon(R.drawable.heart_active)
            .setContentTitle("You've got a new match!")
            .setContentText("Much longer text that cannot fit one line...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

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
