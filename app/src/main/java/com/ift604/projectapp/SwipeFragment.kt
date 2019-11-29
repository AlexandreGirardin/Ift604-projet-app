package com.ift604.projectapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import java.util.*
import kotlin.collections.ArrayList

class SwipeFragment : Fragment(), CardStackListener {
    private lateinit var bioScrollView: ScrollView
    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    private lateinit var rewindBtn: ImageButton
    private lateinit var skipBtn: ImageButton
    private lateinit var superBtn: ImageButton
    private lateinit var matchBtn: ImageButton
    private lateinit var boostBtn: ImageButton
    private lateinit var showCardBtn: ImageButton

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

        bioScrollView = view.findViewById(R.id.bioScrollView)
        bioScrollView.visibility = View.GONE

        showCardBtn = view.findViewById(R.id.showCardBtn)
        showCardBtn.setOnClickListener{v ->
            val rootView = v.rootView
            val bioScrollView = rootView.findViewById<ScrollView>(R.id.bioScrollView)
            val mainMenu = rootView.findViewById<LinearLayout>(R.id.mainMenu)
            val cardView = rootView.findViewById<CardStackView>(R.id.swipeCard)
            val swipeBackground = rootView.findViewById<LinearLayout>(R.id.swipeBackground)
            bioScrollView.visibility = View.GONE
            cardView.visibility = View.VISIBLE
            mainMenu.visibility = View.VISIBLE
            swipeBackground.visibility = View.VISIBLE
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
        val p1 = Profile(
            UUID.randomUUID(),
            "Tyler",
            "tyler@gmail.com",
            "12345",
            18,
            5,
            "Student",
            listOf("https://images.unsplash.com/photo-1473398643778-d68e48a374c1?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=600&h=800&fit=crop&ixid=eyJhcHBfaWQiOjF9")
        )
        ApiClient().doAsyncRegister(p1)


        val p2 = Profile(
            UUID.randomUUID(),
            "Alex",
            "alex@gmail.com",
            "12345",
            25,
            0,
            "Student",
            listOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800")
        )
        val p3 = Profile(
            UUID.randomUUID(),
            "Sam",
            "sam@gmail.com",
            "12345",
            50,
            2,
            "Student",
            listOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800")
        )
        val p4 = Profile(
            UUID.randomUUID(),
            "Charlie",
            "charlie@gmail.com",
            "12345",
            27,
            7,
            "Student",
            listOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800")
        )
        val p5 = Profile(
            UUID.randomUUID(),
            "Fred",
            "fred@gmail.com",
            "12345",
            30,
            20,
            "Student",
            listOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800")
        )
        val p6 = Profile(
            UUID.randomUUID(),
            "Dallas",
            "dallas@gmail.com",
            "12345",
            30,
            20,
            "Student",
            listOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800")
        )
        val p7 = Profile(
            UUID.randomUUID(),
            "Riley",
            "riley@gmail.com",
            "12345",
            30,
            20,
            "Student",
            listOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800")
        )
        return listOf(p1, p2, p3, p4, p5, p6, p7)
    }
}
