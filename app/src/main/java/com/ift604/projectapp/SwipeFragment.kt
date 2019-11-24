package com.ift604.projectapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.yuyakaido.android.cardstackview.*
import java.util.*
import kotlin.collections.ArrayList

class SwipeFragment : Fragment(), CardStackListener {
    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swipe, container, false)
        cardStackView = view.findViewById(R.id.swipeCard)
        manager = CardStackLayoutManager(activity, this)
        adapter = CardStackAdapter(createProfils())
        setupCardStackView()
        return view
    }

    companion object {
        fun newInstance() =
            SwipeFragment()
    }

    private fun setupCardStackView() {
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
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun paginate() {
        val old = adapter.getProfils()
        val new = old.plus(createProfils())
        val callback = ProfilDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setProfils(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createProfils(): ArrayList<Profil> {
        val p1 = Profil(
            UUID.randomUUID(),
            "Bob",
            18,
            5,
            "Student",
            arrayListOf("https://images.unsplash.com/photo-1473398643778-d68e48a374c1?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=600&h=800&fit=crop&ixid=eyJhcHBfaWQiOjF9")
        )
        val p2 = Profil(
            UUID.randomUUID(),
            "Alex",
            25,
            0,
            "Student",
            arrayListOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800")
        )
        return arrayListOf(p1, p2)
    }
}
