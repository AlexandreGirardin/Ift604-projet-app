package com.ift604.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewMatchAdapter(
    private var matches: List<Profile> = emptyList()
) : RecyclerView.Adapter<NewMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.new_match, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matches[position]
        holder.name.text = match.name

        Picasso.get()
            .load(match.pictures[0])
            .placeholder(R.drawable.profile_large)
            .error(R.drawable.profile_large)
            .into(holder.image)

//        handle Layout or picture press here and show message layout
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    fun setMatches(spots: List<Profile>) {
        this.matches = spots
    }

    fun getMatches(): List<Profile> {
        return matches
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.newMatchPicture)
        val name: TextView = view.findViewById(R.id.newMatchName)
    }
}
