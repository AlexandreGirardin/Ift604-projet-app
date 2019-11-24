package com.ift604.projectapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
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
    }

    private fun toggleButtons(btn: ImageButton, activeSrc: Int) {
        findViewById<ImageButton>(R.id.profileBtn).setImageResource(R.drawable.profile)
        findViewById<ImageButton>(R.id.swipeBtn).setImageResource(R.drawable.flame)
        findViewById<ImageButton>(R.id.likeBtn).setImageResource(R.drawable.like)
        findViewById<ImageButton>(R.id.messageBtn).setImageResource(R.drawable.bubble)
        btn.setImageResource(activeSrc)
    }

    private fun addTabs(viewPager: ViewPager) {
        val adapter = CustomPagerAdapter(supportFragmentManager)
        adapter.addFrag(ProfileFragment.newInstance())
        adapter.addFrag(SwipeFragment.newInstance())
        adapter.addFrag(LikeFragment.newInstance())
        adapter.addFrag(MessageFragment.newInstance())
        viewPager.adapter = adapter
        viewPager.currentItem = 1
    }
}
