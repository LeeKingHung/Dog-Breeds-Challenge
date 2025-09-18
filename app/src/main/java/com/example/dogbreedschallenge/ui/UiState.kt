package com.example.dogbreedschallenge.ui

data class UiState(
	val inputs: List<String> = listOf("Affenpinscher", "African", "Airedale"),
	val answer: String = "",
	val isAnswerCorrect: Boolean? = null
)