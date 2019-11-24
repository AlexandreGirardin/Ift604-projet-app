package com.ift604.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CardStackAdapter(
    private var profiles: List<Profile> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.card_profile, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profil = profiles[position]
        holder.name.text = profil.name
        holder.age.text = profil.age.toString()
        holder.work.text = profil.work
        holder.distance.text = "${profil.distance} km away"
        val picture = "http://s5.favim.com/orig/140716/girl-hair-lips-mouth-Favim.com-1918955.jpg"
        Picasso.get()
            .load(picture)
            .placeholder(R.drawable.profile_large)
            .error(R.drawable.profile_large)
            .into(holder.image)


        holder.previous.setOnClickListener { v ->
            Toast.makeText(v.context, "Previous", Toast.LENGTH_SHORT).show()
        }

        holder.next.setOnClickListener { v ->
            Toast.makeText(v.context, "Next", Toast.LENGTH_SHORT).show()
        }

        holder.bio.setOnClickListener { v ->
            Toast.makeText(v.context, "Bio", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    fun setProfils(spots: List<Profile>) {
        this.profiles = spots
    }

    fun getProfils(): List<Profile> {
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