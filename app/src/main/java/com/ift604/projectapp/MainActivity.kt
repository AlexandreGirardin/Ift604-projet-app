package com.ift604.projectapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.viewpager.widget.ViewPager


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: CustomViewPager
    private lateinit var profilBtn: ImageButton
    private lateinit var swipeBtn: ImageButton
    private lateinit var likeBtn: ImageButton
    private lateinit var messageBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewpager)
        addTabs(viewPager)

        profilBtn = findViewById(R.id.profileBtn)
        profilBtn.setOnClickListener {
            toggleButtons(profilBtn, R.drawable.profile_active)
            viewPager.setCurrentItem(0, true)
        }

        swipeBtn = findViewById(R.id.swipeBtn)
        swipeBtn.setOnClickListener {
            toggleButtons(swipeBtn, R.drawable.flame_active)
            viewPager.setCurrentItem(1, true)
        }

        likeBtn = findViewById(R.id.likeBtn)
        likeBtn.setOnClickListener {
            toggleButtons(likeBtn, R.drawable.like_active)
            viewPager.setCurrentItem(2, true)
        }

        messageBtn = findViewById(R.id.messageBtn)
        messageBtn.setOnClickListener {
            toggleButtons(messageBtn, R.drawable.bubble_active)
            viewPager.setCurrentItem(3, true)
        }

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if(ViewPager.SCROLL_STATE_IDLE == state){
                    when(viewPager.currentItem) {
                        0 -> {
                            viewPager.setPagingEnabled(true)
                            toggleButtons(profilBtn, R.drawable.profile_active)
                        }
                        1 -> {
                            viewPager.setPagingEnabled(false)
                            toggleButtons(swipeBtn, R.drawable.flame_active)
                        }
                        2 -> {
                            viewPager.setPagingEnabled(true)
                            toggleButtons(likeBtn, R.drawable.like_active)
                        }
                        3 -> {
                            viewPager.setPagingEnabled(true)
                            toggleButtons(messageBtn, R.drawable.bubble_active)
                        }
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

        })

        /*val builder = NotificationCompat.Builder(this, "test")
            .setSmallIcon(R.drawable.heart_active)
            .setContentTitle("You've got a new match!")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
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
            notify(1, builder.build())
        }*/
    }

    private fun toggleButtons(btn: ImageButton, activeSrc: Int) {
        findViewById<ImageButton>(R.id.profileBtn).setImageResource(R.drawable.profile)
        findViewById<ImageButton>(R.id.swipeBtn).setImageResource(R.drawable.flame)
        findViewById<ImageButton>(R.id.likeBtn).setImageResource(R.drawable.like)
        findViewById<ImageButton>(R.id.messageBtn).setImageResource(R.drawable.bubble)
        btn.setImageResource(activeSrc)
    }

    private fun addTabs(viewPager: CustomViewPager) {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFrag(ProfileFragment.newInstance())
        adapter.addFrag(SwipeFragment.newInstance())
        adapter.addFrag(LikeFragment.newInstance())
        adapter.addFrag(MessageFragment.newInstance())
        viewPager.adapter = adapter
        viewPager.currentItem = 1
        viewPager.offscreenPageLimit = 3
        viewPager.setPagingEnabled(false)
    }
}
