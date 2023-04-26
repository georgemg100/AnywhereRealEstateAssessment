package com.example.assesmentanywhererealestate.ui

import com.example.assesmentanywhererealestate.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * A RecyclerView.Adapter to populate the screen with a store feed.
 */
class CharactersAdapter constructor(var onItemClick: (Int) -> Unit): RecyclerView.Adapter<CharacterItemViewHolder>() {
    var data: List<UiTopicsState>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemViewHolder {
        return CharacterItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_character, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CharacterItemViewHolder, position: Int) {
        with(holder.itemView) {
            setOnClickListener(View.OnClickListener {
                onItemClick(data?.get(position)?.position!!)
            })
            findViewById<TextView>(R.id.name).text = data?.get(position)?.relatedTopic?.relatedTopicsResponse?.name
            findViewById<TextView>(R.id.description).text = data?.get(position)?.relatedTopic?.relatedTopicsResponse?.Text
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
}

/**
 * Holds the view for the Store item.
 */
class CharacterItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
