package com.ift604.projectapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder

class MessageFragment : Fragment() {
    private lateinit var newMatchRecyclerView: RecyclerView
    private lateinit var newMatchAdapter: NewMatchAdapter

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newMatchAdapter = NewMatchAdapter(fetchNewMatchesData())
        messageAdapter = MessageAdapter(fetchNewMatchesData())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        newMatchRecyclerView = view.findViewById(R.id.newMatchRecyclerView)
        newMatchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = newMatchAdapter
        }

        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }
        return view
    }

    private fun fetchNewMatchesData(): List<Profile> {
        val jsonArrayOfSwipeables = ApiClient.instance.getApiSwipe()

        val gson = GsonBuilder().create()
        val lSwipeableUsers = gson.fromJson(jsonArrayOfSwipeables.toString() , Array<Profile>::class.java).toList()

        return lSwipeableUsers
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MessageFragment()
    }
}
