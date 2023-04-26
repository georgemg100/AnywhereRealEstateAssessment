package com.example.assesmentanywhererealestate.data

import com.example.assesmentanywhererealestate.data.model.IndexedRelatedTopics
import com.example.assesmentanywhererealestate.data.model.NetworkDataSource
import com.example.assesmentanywhererealestate.data.model.RelatedTopicsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(val networkDataSource: NetworkDataSource, private val externalScope: CoroutineScope) {
    //creates a state flow that caches latest value to allow data sharing between consumers
    val characterFlow: Flow<IndexedRelatedTopics> = flow {
        emit(networkDataSource.getCharacters().await())
    }.map {
        IndexedRelatedTopics(it.relatedTopics.mapIndexed { index, relatedTopicResponse ->
            RelatedTopicsModel(index, relatedTopicResponse)
        })
    }
        .stateIn(externalScope, SharingStarted.WhileSubscribed(), IndexedRelatedTopics(emptyList()))
}