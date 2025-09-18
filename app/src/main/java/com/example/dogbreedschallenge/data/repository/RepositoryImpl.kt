package com.example.dogbreedschallenge.data.repository

import com.example.dogbreedschallenge.data.datasource.remote.ApiService

class RepositoryImpl(private val apiService: ApiService) : Repository {

	override suspend fun getAllDogs(): Result<List<String>> {
		return try {
			val response = apiService.getAllDogs()
			Result.success(response.body()!!.data)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override suspend fun getRandomDogImage(breed: String): Result<String> {
		return try {
			val response = apiService.getRandomDogImage(breed)
			Result.success(response.body()!!.data)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

}