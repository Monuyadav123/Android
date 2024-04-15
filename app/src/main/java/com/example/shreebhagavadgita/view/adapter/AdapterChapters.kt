package com.example.shreebhagavadgita.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shreebhagavadgita.databinding.ItemViewChapterBinding
import com.example.shreebhagavadgita.models.ChaptersItem
import kotlin.reflect.KFunction1

class AdapterChapters(val onChapterIVClicked: KFunction1<ChaptersItem, Unit>) : RecyclerView.Adapter<AdapterChapters.ChapterViewHolder>() {

    class ChapterViewHolder(val binding:ItemViewChapterBinding):ViewHolder(binding.root)



//    Diffutil for the adapter to update the recyclerview with new data and animations when the data changes in the recyclerview adapter
    val diffUtil= object :  DiffUtil.ItemCallback<ChaptersItem>(){
        override fun areItemsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem==newItem
        }

    }

//    AsyncListDiffer to handle the diffutil in the adapter to update the recyclerview with new data and animations when the data changes in the recyclerview adapter
    val differ= AsyncListDiffer(this,diffUtil)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        return ChapterViewHolder(ItemViewChapterBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
        }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {

            val chapter=differ.currentList[position]
            holder.binding.apply {
                tvChapterTitle.text=chapter.name_translated
                chapterNumber.text="Chapter ${chapter.chapter_number}"
                chapterDescription.text=chapter.chapter_summary
                verseCount.text=chapter.verses_count.toString()
            }

            holder.binding.llChapter.setOnClickListener {
                onChapterIVClicked(chapter)
            }


    }




}