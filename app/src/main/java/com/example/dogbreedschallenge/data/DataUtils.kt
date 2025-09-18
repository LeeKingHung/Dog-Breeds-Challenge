package com.example.dogbreedschallenge.data

object DataUtils {

	fun getBreedPathForImageLink(breed: String): String {

		return if (breed.contains(' ')) {
			val strings = breed.split(' ')
			"${strings[1]}/${strings[0]}"
		} else {
			breed
		}

	}

}