package com.example.assesmentanywhererealestate.data.model

import com.example.assesmentanywhererealestate.BuildConfig
import retrofit2.Call
import retrofit2.http.*

interface NetworkDataSource {
    @GET(BuildConfig.QUERY_PARAM)
    fun getCharacters(): Call<ResponseModel>
}
