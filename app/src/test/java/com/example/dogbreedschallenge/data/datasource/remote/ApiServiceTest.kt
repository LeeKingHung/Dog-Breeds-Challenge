package com.example.dogbreedschallenge.data.datasource.remote

import com.google.common.truth.Truth
import com.google.gson.GsonBuilder
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiServiceTest {

	private lateinit var mockWebServer: MockWebServer
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {

		mockWebServer = MockWebServer()
		mockWebServer.start()

		// GSON conversion settings
		val listOfStringsType = object : ParameterizedType {
			override fun getRawType(): Type = List::class.java
			override fun getOwnerType(): Type? = null
			override fun getActualTypeArguments(): Array<Type> = arrayOf(String::class.java)
		}
		val gson = GsonBuilder()
			.registerTypeAdapter(listOfStringsType, BreedsJsonDeserializer())
			.create()

		apiService = Retrofit.Builder()
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
			.create(ApiService::class.java)

	}

	@After
	fun tearDown() {
		mockWebServer.shutdown()
	}

	@Test
	fun getAllDogs() = runTest {
		
		val jsonBody = RemoteDataSourceUtils.getJsonContentFromFile("Get All Breeds")
		mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonBody))
		val response = apiService.getAllDogs()

		// Assertions
		assertEquals(200, response.code())
		assertTrue(response.isSuccessful)
		val list = response.body()
		Truth.assertThat(list).containsExactly(
			"affenpinscher",
			"kelpie australian",
			"shepherd australian",
			"indian bakharwal",
			"akita"
		)
		
	}

	@Test
	fun getRandomDogImage() = runTest {
		
		val jsonBody = RemoteDataSourceUtils.getJsonContentFromFile("Random Breed Image")
		mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonBody))
		val response = apiService.getRandomDogImage("")

		// Assertions
		assertEquals(200, response.code())
		assertTrue(response.isSuccessful)
		assertEquals(
			response.body(),
			Data("https://images.dog.ceo/breeds/australian-kelpie/Resized_20200303_233358_108952253645051.jpg")
		)
		
	}

}