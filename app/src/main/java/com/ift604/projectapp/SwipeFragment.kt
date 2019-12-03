package com.ift604.projectapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.yuyakaido.android.cardstackview.*


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
        adapter = CardStackAdapter(fetchSwipeableUsers())
        setupManager()
    }

    private fun setupManager() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.1f)
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

            Direction.Right -> {
                val topCardId = manager.topPosition - 1
                val userId = adapter.getProfiles()[topCardId].id
                if (userId != null)
                {
                    ApiClient.postApiLike(userId)
                    Toast.makeText(this.context, "Like", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this.context, "No userId provided (LIKE)", Toast.LENGTH_SHORT).show()
                }
            }
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

        val rootView = view.rootView
        val bioName = rootView.findViewById<TextView>(R.id.bioName)
        val bioAge = rootView.findViewById<TextView>(R.id.bioAge)
        val bioText = rootView.findViewById<TextView>(R.id.bioText)
        val bioPicture = rootView.findViewById<ImageView>(R.id.bioPicture)
        val profile = adapter.getProfiles()[position]

        bioName.text = profile.name
        bioAge.text = profile.age.toString()
        bioText.text = profile.bio
        Picasso.get()
            .load(ApiClient.getUrl() + profile.photo)
            .placeholder(R.drawable.profile_large)
            .error(R.drawable.profile_large)
            .into(bioPicture)

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
        val new = old.plus(fetchSwipeableUsers())
        val callback = ProfileDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setProfiles(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun fetchSwipeableUsers(): List<Profile> {
        val jsonArrayOfSwipeables = ApiClient.getApiSwipe()

        val gson = GsonBuilder().create()

        return gson.fromJson(jsonArrayOfSwipeables.toString() , Array<Profile>::class.java).toList()
    }
}
