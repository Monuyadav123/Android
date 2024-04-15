package com.example.shreebhagavadgita.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shreebhagavadgita.models.ChaptersItem
import com.example.shreebhagavadgita.models.VersesItem
import com.example.shreebhagavadgita.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MainViewModel: ViewModel() {

    val appRepository = AppRepository()
    fun getAllChapters(): Flow<List<ChaptersItem>> = appRepository.getAllChapters()
    fun getVerse(chapterNumber: Int): Flow<List<VersesItem>> = appRepository.getVerse(chapterNumber)
    fun getParticularVerse(chapterNumber: Int,verseNumber:Int): Flow<VersesItem> = appRepository.getParticularVerse(chapterNumber,verseNumber)
}


