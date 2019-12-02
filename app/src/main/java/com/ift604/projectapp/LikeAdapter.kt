package com.ift604.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class LikeAdapter(
    private var profiles: List<Profile> = emptyList()
) : RecyclerView.Adapter<LikeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.like_profile, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.name.text = "${profile.name}, ${profile.age}"

        Picasso.get()
            .load(ApiClient.getUrl() + profile.photo)
            .placeholder(R.drawable.profile_large)
            .error(R.drawable.profile_large)
            .into(holder.image)

//        handle Layout or picture press here and show profile bio
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    fun setMatches(spots: List<Profile>) {
        this.profiles = spots
    }

    fun getMatches(): List<Profile> {
        return profiles
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.likePicture)
        val name: TextView = view.findViewById(R.id.likeName)
        val work: TextView = view.findViewById(R.id.likeWork)
    }
}