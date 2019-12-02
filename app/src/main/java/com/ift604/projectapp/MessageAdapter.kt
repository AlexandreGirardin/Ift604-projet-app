package com.ift604.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MessageAdapter(
    private var messages: List<Profile> = emptyList()
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.message, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = messages[position]
        holder.name.text = match.name
        holder.preview.text = "Netflix and chill?"
        Picasso.get()
            .load(ApiClient.getUrl() + match.photo)
            .placeholder(R.drawable.profile_large)
            .error(R.drawable.profile_large)
            .into(holder.image)

//        handle Layout or picture press here and show message layout
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun setMessages(spots: List<Profile>) {
        this.messages = spots
    }

    fun getMessages(): List<Profile> {
        return messages
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.messagePicture)
        val name: TextView = view.findViewById(R.id.messageName)
        val preview: TextView = view.findViewById(R.id.messagePreview)
    }
}
