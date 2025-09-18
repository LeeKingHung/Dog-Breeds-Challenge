package com.example.dogbreedschallenge.data.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

	@GET("breeds/list/all")
	suspend fun getAllDogs(): Response<List<String>>

	@GET("breed/{breed}/images/random")
	suspend fun getRandomDogImage(@Path("breed") breed: String): Response<Data<String>>

}