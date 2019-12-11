package com.ift604.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yuyakaido.android.cardstackview.CardStackView

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
        holder.distance.text = "${profile.distance} km away"

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
        val distance: TextView = view.findViewById(R.id.likeDistance)
        val cardView: CardView = view.findViewById(R.id.likeCard)
    }
}