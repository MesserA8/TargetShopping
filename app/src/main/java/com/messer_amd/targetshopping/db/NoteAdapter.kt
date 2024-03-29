package com.messer_amd.targetshopping.db

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.messer_amd.targetshopping.R
import com.messer_amd.targetshopping.databinding.NoteListItemBinding
import com.messer_amd.targetshopping.entities.NoteItem
import com.messer_amd.targetshopping.utils.HtmlManager
import com.messer_amd.targetshopping.utils.TimeManager

class NoteAdapter(private val listener: Listener, private val defPref: SharedPreferences) :
    ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NoteListItemBinding.bind(view)
        private var pref: SharedPreferences? = null

        fun setData(note: NoteItem, listener: Listener, defPref: SharedPreferences) = with(binding) {
            pref = PreferenceManager.getDefaultSharedPreferences(itemView.context)
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = TimeManager.getTimeFormat(note.time, defPref)
            itemView.setOnClickListener {
                listener.onClickItem(note)
            }
            imDelete.setOnClickListener {
                listener.deleteItem(note.id!!)
            }
            setTextSize()
        }

          private  fun setTextSize() = with(binding) {
            tvTitle.setTextSize(pref?.getString("title_size_key", "18"))
            tvDescription.setTextSize(pref?.getString("content_size_key", "16"))
        }

        private fun TextView.setTextSize(size: String?) {
            if (size != null) this.textSize = size.toFloat()
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.note_list_item, parent, false)
                )
            }

        }
    }

    class ItemComparator : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }

}


