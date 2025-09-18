package com.example.dogbreedschallenge.ui

/**
 * @param isAnswerCorrect if null -> no checking yet
 */
data class UiState(
	val inputsState: LoadingState<List<String>> = LoadingState.None,
	val imageUrlState: LoadingState<String> = LoadingState.None,
	val userAnswer: String = "",
	val correctAnswer: String = "",
	val isAnswerCorrect: Boolean? = null
)

sealed class LoadingState<out T> {
	object None : LoadingState<Nothing>()
	object Loading : LoadingState<Nothing>()
	data class Success<T>(val data: T) : LoadingState<T>()
	object Error : LoadingState<Nothing>()
}