package com.ift604.projectapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewpager)
        addTabs(viewPager)
    }

    private fun addTabs(viewPager: ViewPager) {
        val adapter = CustomPagerAdapter(supportFragmentManager)
        adapter.addFrag(ProfilFragment.newInstance())
        adapter.addFrag(SwipeFragment.newInstance())
        adapter.addFrag(LikeFragment.newInstance())
        adapter.addFrag(MessageFragment.newInstance())
        viewPager.adapter = adapter
        viewPager.currentItem = 1
    }
}
