package com.ift604.projectapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.GsonBuilder
import com.yuyakaido.android.cardstackview.*
import java.util.*


class SwipeFragment : Fragment(), CardStackListener {
    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    private lateinit var rewindBtn: ImageButton
    private lateinit var skipBtn: ImageButton
    private lateinit var superBtn: ImageButton
    private lateinit var matchBtn: ImageButton
    private lateinit var boostBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = CardStackLayoutManager(activity, this)
        adapter = CardStackAdapter(fetchProfilesData())
        setupManager()
    }

    private fun setupManager() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swipe, container, false)
        cardStackView = view.findViewById(R.id.swipeCard)
        setupCardStackView()

        rewindBtn = view.findViewById(R.id.rewindBtn)
        rewindBtn.setOnClickListener {
            cardStackView.rewind()
        }

        skipBtn = view.findViewById(R.id.skipBtn)
        skipBtn.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        superBtn = view.findViewById(R.id.superBtn)
        superBtn.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Top)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        matchBtn = view.findViewById(R.id.matchBtn)
        matchBtn.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        return view
    }

    companion object {
        fun newInstance() =
            SwipeFragment()
    }

    private fun setupCardStackView() {
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }

        if (manager.topPosition == adapter.itemCount)
        {
            skipBtn.setImageResource(R.drawable.cross)
            superBtn.setImageResource(R.drawable.star)
            matchBtn.setImageResource(R.drawable.heart)
        }

//      Handle swipe direction here
        when (direction) {
            Direction.Right -> Toast.makeText(this.context, "Match", Toast.LENGTH_SHORT).show()
            Direction.Top -> Toast.makeText(this.context, "Super", Toast.LENGTH_SHORT).show()
            Direction.Left -> Toast.makeText(this.context, "Skip", Toast.LENGTH_SHORT).show()
            Direction.Bottom -> Toast.makeText(this.context, "Cancel", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.name)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
        skipBtn.setImageResource(R.drawable.cross_active)
        superBtn.setImageResource(R.drawable.star_active)
        matchBtn.setImageResource(R.drawable.heart_active)
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun paginate() {
        val old = adapter.getProfiles()
        val new = old.plus(fetchProfilesData())
        val callback = ProfileDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setProfiles(new)
        result.dispatchUpdatesTo(adapter)
    }

    //Replace this with fetch profile
    private fun fetchProfilesData(): List<Profile> {
        //ApiClient().doAsyncRegister(p2)
        val apiClient = (activity as MainActivity).apiClient
        val jsonArrayOfSwipeables = apiClient.getApiSwipe()

        val gson = GsonBuilder().create()
        val lSwipeableUsers = gson.fromJson(jsonArrayOfSwipeables.toString() , Array<Profile>::class.java).toList()

        println(lSwipeableUsers)
        return lSwipeableUsers
    }
}
