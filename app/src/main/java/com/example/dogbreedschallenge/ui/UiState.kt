package com.example.dogbreedschallenge.ui

data class UiState(
	val inputsState: LoadingState<List<String>>,
	val answer: String = "",
	val isAnswerCorrect: Boolean? = null
)

sealed class LoadingState<out T> {
	object None : LoadingState<Nothing>()
	object Loading : LoadingState<Nothing>()
	data class Success<T>(val data: T) : LoadingState<T>()
	object Error : LoadingState<Nothing>()
}