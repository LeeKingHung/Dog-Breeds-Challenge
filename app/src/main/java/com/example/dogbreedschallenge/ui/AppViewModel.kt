package com.example.dogbreedschallenge.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel  : ViewModel() {
	
	private val _uiStateFlow = MutableStateFlow(UiState())
	val uiStateFlow = _uiStateFlow.asStateFlow()

	fun updateAnswer(answer: String) {
		_uiStateFlow.update { it.copy(answer = answer) }
	}

	fun checkAnswer() {
		
	}
	
}