package com.example.dogbreedschallenge.data.repository

interface Repository {

	suspend fun getAllDogs(): Result<List<String>>

	suspend fun getRandomDogImage(breed: String): Result<String>

}