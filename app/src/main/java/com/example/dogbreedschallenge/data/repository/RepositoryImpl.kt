package com.example.dogbreedschallenge.data.repository

import android.util.Log
import com.example.dogbreedschallenge.data.datasource.remote.ApiService

class RepositoryImpl(private val apiService: ApiService) : Repository {

	companion object {
		const val TAG = "Repository"
	}

	override suspend fun getAllDogs(): Result<List<String>> {
		return try {
			val response = apiService.getAllDogs()
			if (response.isSuccessful) Result.success(response.body()!!)
			else Result.failure(Exception())
		} catch (e: Exception) {
			Log.e(TAG, "", e)
			Result.failure(e)
		}
	}

	override suspend fun getRandomDogImage(breed: String): Result<String> {
		return try {
			val response = apiService.getRandomDogImage(breed)
			if (response.isSuccessful) Result.success(response.body()!!.data)
			else Result.failure(Exception())
		} catch (e: Exception) {
			Log.e(TAG, "", e)
			Result.failure(e)
		}
	}

}