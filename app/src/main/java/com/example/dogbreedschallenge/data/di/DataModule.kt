package com.example.dogbreedschallenge.data.di

import com.example.dogbreedschallenge.data.datasource.remote.ApiService
import com.example.dogbreedschallenge.data.datasource.remote.BreedsJsonDeserializer
import com.example.dogbreedschallenge.data.repository.Repository
import com.example.dogbreedschallenge.data.repository.RepositoryImpl
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

	@Provides
	@Singleton
	fun repository(): Repository {

		// GSON conversion settings
		val listOfStringsType = object : ParameterizedType {
			override fun getRawType(): Type = List::class.java
			override fun getOwnerType(): Type? = null
			override fun getActualTypeArguments(): Array<Type> = arrayOf(String::class.java)
		}
		val gson = GsonBuilder()
			.registerTypeAdapter(listOfStringsType, BreedsJsonDeserializer())
			.create()

		// Custom client for logging interceptor
		val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
		val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

		val apiService = Retrofit.Builder()
			.baseUrl("https://dog.ceo/api/")
			.client(client)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
			.create(ApiService::class.java)
		return RepositoryImpl(apiService)

	}

}