package com.example.samplenotesapp.ui.feature.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.samplenotesapp.databinding.ItemNoteBinding
import com.example.samplenotesapp.ui.model.NotePresentation

data class NotesCardViewHolder(
    val binding: ItemNoteBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun setOnClickListener(listener: () -> Unit) {
        binding.root.setOnClickListener {
            listener()
        }
    }

    fun bind(item: NotePresentation) {
        binding.title.text = item.title
        binding.createdAt.text = item.createdAt
        binding.description.text = item.description
    }
}