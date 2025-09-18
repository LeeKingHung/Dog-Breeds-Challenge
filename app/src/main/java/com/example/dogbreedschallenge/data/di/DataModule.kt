package com.example.dogbreedschallenge.data.di

import com.example.dogbreedschallenge.data.datasource.remote.ApiService
import com.example.dogbreedschallenge.data.datasource.remote.BreedsJsonDeserializer
import com.example.dogbreedschallenge.data.repository.Repository
import com.example.dogbreedschallenge.data.repository.RepositoryImpl
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

	@Provides
	@Singleton
	fun repository(): Repository {
		
		// GSON conversion settings
		val gson = GsonBuilder()
			.registerTypeAdapter(object : TypeToken<List<String>>() {}.type, BreedsJsonDeserializer())
			.create()
		
		val apiService = Retrofit.Builder()
			.baseUrl("https://dog.ceo/api/")
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
			.create(ApiService::class.java)
		return RepositoryImpl(apiService)
		
	}

}