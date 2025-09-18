package com.example.dogbreedschallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

	private val _uiStateFlow = MutableStateFlow(UiState(inputsState = LoadingState.None))
	val uiStateFlow = _uiStateFlow.asStateFlow()

	init {
		loadAllBreeds()
	}

	fun updateAnswer(answer: String) {
		_uiStateFlow.update { it.copy(answer = answer) }
	}

	fun checkAnswer() {

	}

	 fun loadAllBreeds() {

		_uiStateFlow.update { it.copy(inputsState = LoadingState.Loading) }

		viewModelScope.launch(context = Dispatchers.IO) {

			repository.getAllDogs().onSuccess { breeds ->
				_uiStateFlow.update { it.copy(inputsState = LoadingState.Success(breeds)) }
			}.onFailure {
				_uiStateFlow.update { it.copy(inputsState = LoadingState.Error) }
			}

		}

	}

}