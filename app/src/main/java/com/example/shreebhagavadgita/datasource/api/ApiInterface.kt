package com.example.shreebhagavadgita.datasource.api

import com.example.shreebhagavadgita.models.ChaptersItem
import com.example.shreebhagavadgita.models.VersesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {


//    @Headers(
//        "Accept: application/json",
//        "X-RapidAPI-Key: 668327c950mshdc2c78d52897a25p19555bjsn8765ad5debfd",
//        "X-RapidAPI-Host: bhagavad-gita3.p.rapidapi.com"
//    )

    @GET("/v2/chapters/")
    fun getAllChapter():Call<List<ChaptersItem>>

    @GET("/v2/chapters/{chapterNumber}/verses/")
    fun getVerses(@Path("chapterNumber") chapterNumber : Int) : Call<List<VersesItem>>


    @GET("/v2/chapters/{chapterNumber}/verses/{verseNumber}/")
    fun getParticularVerse(
        @Path("chapterNumber") chapterNumber: Int,
        @Path("verseNumber") verseNumber: Int
    ): Call<VersesItem>


}