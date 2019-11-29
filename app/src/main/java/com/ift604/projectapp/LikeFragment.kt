package com.ift604.projectapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder

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
        val jsonArrayOfSwipeables = ApiClient.instance.getApiSwipe()

        val gson = GsonBuilder().create()
        val lSwipeableUsers = gson.fromJson(jsonArrayOfSwipeables.toString() , Array<Profile>::class.java).toList()

        return lSwipeableUsers
    }

    companion object {
        @JvmStatic
        fun newInstance() = LikeFragment()
    }
}
