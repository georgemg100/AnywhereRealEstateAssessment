package com.example.assesmentanywhererealestate.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assesmentanywhererealestate.data.CharacterRepository
import com.example.assesmentanywhererealestate.data.ResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(val charactersRepository: CharacterRepository): ViewModel() {

    private val _charactersLiveData: MutableLiveData<ResponseModel> = MutableLiveData()
    val charactersLiveData: LiveData<ResponseModel> = _charactersLiveData

    init {
        viewModelScope.launch {
            _charactersLiveData.value = charactersRepository.getCharacters()
        }
    }

}