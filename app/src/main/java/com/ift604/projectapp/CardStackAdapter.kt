package com.ift604.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CardStackAdapter(
    private var profiles: List<Profile> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    private lateinit var viewParent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewParent = parent
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.card_profile, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profil = profiles[position]
        holder.name.text = profil.name
        holder.age.text = profil.age.toString()
        holder.work.text = profil.email         // TODO: Change field for email.
        holder.distance.text = "${profil.distance} km away"
        Picasso.get()
            .load(ApiClient.instance.getUrl() + profil.photo)
            .placeholder(R.drawable.profile_large)
            .error(R.drawable.profile_large)
            .into(holder.image)


//      handle profile press here
        holder.previous.setOnClickListener { v ->
            Toast.makeText(v.context, "Previous", Toast.LENGTH_SHORT).show()
        }

        holder.next.setOnClickListener { v ->
            Toast.makeText(v.context, "Next", Toast.LENGTH_SHORT).show()
        }

        holder.bio.setOnClickListener {v ->
            val rootView = v.rootView
            val bioScrollView = rootView.findViewById<ScrollView>(R.id.bioScrollView)
            val swipeBackground = rootView.findViewById<LinearLayout>(R.id.swipeBackground)
            val mainMenu = rootView.findViewById<LinearLayout>(R.id.mainMenu)
            swipeBackground.visibility = View.GONE
            bioScrollView.visibility = View.VISIBLE
            viewParent.visibility = View.GONE
            mainMenu.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    fun setProfiles(profiles: List<Profile>) {
        this.profiles = profiles
    }

    fun getProfiles(): List<Profile> {
        return profiles
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val age: TextView = view.findViewById(R.id.age)
        var work: TextView = view.findViewById(R.id.work)
        var distance: TextView = view.findViewById(R.id.distance)
        var image: ImageView = view.findViewById(R.id.image)
        val previous: View = view.findViewById(R.id.previous)
        val next: View = view.findViewById(R.id.next)
        val bio: View = view.findViewById(R.id.bio)
    }
}