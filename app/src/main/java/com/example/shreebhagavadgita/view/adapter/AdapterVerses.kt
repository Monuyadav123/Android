package com.example.shreebhagavadgita.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shreebhagavadgita.databinding.ItemViewVersesBinding

class AdapterVerses(val onVersesItemClicked: (String, Int) -> Unit) :RecyclerView.Adapter<AdapterVerses.VerseViewHolder>() {
    class VerseViewHolder (val binding: ItemViewVersesBinding):ViewHolder(binding.root)


    val diffUtil= object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

    }


    val differ = AsyncListDiffer(this,diffUtil)
//    what is AsyncListDiffer? https://stackoverflow.com/questions/63117717/what-is-async-list-differ-in-android
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        return VerseViewHolder(ItemViewVersesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        var verse = differ.currentList[position]
        holder.binding.tvVerseNumber.text = "Verse ${position+1}"
        holder.binding.tvVerse.text = verse

        holder.binding.linearLayout2.setOnClickListener {
            onVersesItemClicked(verse,position+1)
        }
    }
}