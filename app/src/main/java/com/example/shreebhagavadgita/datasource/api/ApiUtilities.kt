package com.example.shreebhagavadgita.datasource.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {


    val headers = mapOf<String, String>(
        "Accept" to "application/json",
        "X-RapidAPI-Key" to "668327c950mshdc2c78d52897a25p19555bjsn8765ad5debfd",
        "X-RapidAPI-Host" to "bhagavad-gita3.p.rapidapi.com"
    )

    val client= OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .apply {
                    headers.forEach { (key, value) ->
                        addHeader(key, value)
                    }
                }
                .build()
            chain.proceed(request)
        }
    }.build()



    val api : ApiInterface by lazy{
        Retrofit.Builder()
            .baseUrl("https://bhagavad-gita3.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}