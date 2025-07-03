package com.alvin.catatanku.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvin.catatanku.R
import com.alvin.catatanku.data.NoteModel

class NoteAdapter(
    private val notes: MutableList<NoteModel.Data>,
    val listener: OnAdapterListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        val note = notes[position]
        holder.tvNote.text = note.note
        holder.itemView.setOnClickListener {
            listener.onClick(note)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNote = view.findViewById<TextView>(R.id.tv_note)
        val iconDelete = view.findViewById<ImageView>(R.id.icon_delete)
    }

    fun setData(data: List<NoteModel.Data>) {
        notes.clear()
        notes.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(note: NoteModel.Data)
    }

}