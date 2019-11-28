package com.ift604.projectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class LikeFragment : Fragment() {
    private lateinit var likeRecyclerView: RecyclerView
    private lateinit var likeAdapter: LikeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        likeAdapter = LikeAdapter(fetchLikeProfile())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_like, container, false)
        likeRecyclerView = view.findViewById(R.id.likeRecyclerView)
        likeRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
            adapter = likeAdapter
        }
        return view
    }

    private fun fetchLikeProfile(): List<Profile> {
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

    companion object {
        @JvmStatic
        fun newInstance() = LikeFragment()
    }
}
