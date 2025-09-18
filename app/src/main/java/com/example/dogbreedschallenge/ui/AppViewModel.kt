package com.example.dogbreedschallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedschallenge.data.DataUtils
import com.example.dogbreedschallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

	private val _uiStateFlow = MutableStateFlow(UiState())
	val uiStateFlow = _uiStateFlow.asStateFlow()

	init {
		loadAllBreeds()
	}

	fun updateAnswer(answer: String) {
		_uiStateFlow.update { it.copy(userAnswer = answer) }
	}

	fun checkAnswer() {

	}

	fun loadAllBreeds() {

		_uiStateFlow.update { it.copy(inputsState = LoadingState.Loading) }

		viewModelScope.launch(Dispatchers.IO) {

			repository.getAllDogs().onSuccess { breeds ->
				_uiStateFlow.update { it.copy(inputsState = LoadingState.Success(breeds)) }
				loadNextBreed()
			}.onFailure {
				_uiStateFlow.update { it.copy(inputsState = LoadingState.Error) }
			}

		}

	}

	private fun loadNextBreed() {

		val inputs = (uiStateFlow.value.inputsState as? LoadingState.Success<List<String>>)?.data ?: return
		val correctAnswer = inputs.random()
		_uiStateFlow.update { it.copy(correctAnswer = correctAnswer, userAnswer = "", imageUrlState = LoadingState.Loading) }
		val breed = DataUtils.getBreedPathForImageLink(correctAnswer)

		viewModelScope.launch(Dispatchers.IO) {

			repository.getRandomDogImage(breed).onSuccess { link ->
				_uiStateFlow.update { it.copy(imageUrlState = LoadingState.Success(link)) }
			}.onFailure {
				_uiStateFlow.update { it.copy(imageUrlState = LoadingState.Error) }
			}

		}

	}

}