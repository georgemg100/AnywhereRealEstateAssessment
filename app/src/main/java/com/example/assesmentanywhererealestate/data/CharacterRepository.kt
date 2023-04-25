package com.example.assesmentanywhererealestate.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import javax.inject.Inject

class CharacterRepository @Inject constructor(val networkDataSource: NetworkDataSource) {

    suspend fun getCharacters(): ResponseModel {
        return withContext(Dispatchers.IO) {
            networkDataSource.getCharacters().await()
        }
    }
}