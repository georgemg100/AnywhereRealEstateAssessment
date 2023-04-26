package com.example.assesmentanywhererealestate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assesmentanywhererealestate.data.CharacterRepository
import com.example.assesmentanywhererealestate.ui.UiTopicsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(val charactersRepository: CharacterRepository): ViewModel() {

    private val _charactersLiveData: MutableLiveData<CharacterUiState> = MutableLiveData()
    val charactersLiveData: LiveData<CharacterUiState> = _charactersLiveData

    val _characterDetailsLiveData: MutableLiveData<DetailsUiState> = MutableLiveData()
    val characterDetailsLiveData: LiveData<DetailsUiState> = _characterDetailsLiveData

    private val _filteredCharacterLiveData = MutableLiveData<CharacterUiState>()
    val filteredCharacterLiveData = _filteredCharacterLiveData

    var curPosition = 0
    init {
        viewModelScope.launch {
            charactersRepository.characterFlow
                .catch {
                    CharacterUiState.Error(it.message)
                }
                .map {
                    val uiTopics = it.indexedRelatedTopics.map {relatedTopicModel ->
                        relatedTopicModel.relatedTopicsResponse.name = parseNameFromUrl(relatedTopicModel.relatedTopicsResponse.FirstURL)
                        UiTopicsState(relatedTopicModel.index, relatedTopicModel)
                    }
                    CharacterUiState.Success(uiTopics)
                }
                .collect {
                    _charactersLiveData.value = it
                }
        }
    }

    fun getDetails(position: Int) {
        curPosition = position
        viewModelScope.launch {
            charactersRepository.characterFlow
                .collect {
                    if (!it.indexedRelatedTopics.isEmpty()) {
                        val description = it.indexedRelatedTopics[curPosition].relatedTopicsResponse.Text
                        val iconUrl = it.indexedRelatedTopics[curPosition].relatedTopicsResponse.Icon.URL
                        val title = it.indexedRelatedTopics[curPosition].relatedTopicsResponse.name
                        _characterDetailsLiveData.value =
                            DetailsUiState.Success(
                                description,
                                "https://duckduckgo.com$iconUrl",
                                title
                            )
                    } else {
                        _characterDetailsLiveData.value = DetailsUiState.Loading
                    }
                }
        }
    }

    fun doSearch(query: String) {
        viewModelScope.launch {
            charactersRepository.characterFlow.map {
                val filteredItems = it.indexedRelatedTopics.filter { relatedTopicModel ->
                    relatedTopicModel.relatedTopicsResponse.Text.contains(
                        query,
                        true
                    )
                }
                val uiTopics = filteredItems.map {relatedTopicModel ->
                    UiTopicsState(relatedTopicModel.index, relatedTopicModel)
                }
                if (uiTopics.isEmpty()) CharacterUiState.Error("no results matching")
                else CharacterUiState.Success(uiTopics)
                }.collect() {
                    _filteredCharacterLiveData.value = it
                }
        }
    }

    private fun parseNameFromUrl(urlString: String): String {
        val prefix = "https://duckduckgo.com/"
        return urlString.substringAfter(prefix).replace("_", " ")
    }
}

sealed interface CharacterUiState {
    object Loading: CharacterUiState
    data class Error(val error: String?): CharacterUiState
    data class Success(val relatedTopics: List<UiTopicsState>): CharacterUiState
}

sealed interface DetailsUiState {
    object Loading: DetailsUiState
    data class Success(val description: String, val iconUrl: String?, val title: String?) :
        DetailsUiState
}