package com.example.samplenotesapp.ui.feature.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenotesapp.databinding.ItemNoteBinding
import com.example.samplenotesapp.ui.model.NotePresentation

class NotesAdapter(
    context: Context,
    items: List<NotePresentation> = listOf(),
    onItemClickListener: (NotePresentation) -> Unit,
) : RecyclerView.Adapter<NotesCardViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    var onItemClickListener: (NotePresentation) -> Unit = onItemClickListener
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var items = items
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            // Update view only if value changed
            if (field != value) {
                field = value

                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesCardViewHolder {
        val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
        return NotesCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesCardViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.setOnClickListener {
            onItemClickListener(item)
        }
    }
}