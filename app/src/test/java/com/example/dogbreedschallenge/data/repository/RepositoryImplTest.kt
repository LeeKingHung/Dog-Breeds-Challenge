package com.example.dogbreedschallenge.data.repository

import com.example.dogbreedschallenge.data.datasource.remote.ApiService
import com.example.dogbreedschallenge.data.datasource.remote.Data
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryImplTest {

	@MockK private lateinit var apiService: ApiService
	private lateinit var repository: Repository

	companion object {
		const val TEST_BREED = ""
	}

	@Before
	fun setUp() {
		MockKAnnotations.init(this)
		repository = RepositoryImpl(apiService)
	}

	@Test
	fun getAllDogsSuccess() = runTest {
		val mockValue = listOf("")
		coEvery { apiService.getAllDogs() } returns Response.success(mockValue)
		val result = repository.getAllDogs()
		assertTrue(result.isSuccess)
		assertEquals(result.getOrNull(), mockValue)
	}

	@Test
	fun getAllDogsFailure() = runTest {
		val mockException = RuntimeException("Network error")
		coEvery { apiService.getAllDogs() } throws mockException
		val result = repository.getAllDogs()
		assertTrue(result.isFailure)
		assertEquals(result.exceptionOrNull(), mockException)
	}

	@Test
	fun getRandomDogImageSuccess() = runTest {
		val mockValue = ""
		coEvery { apiService.getRandomDogImage(TEST_BREED) } returns Response.success(Data(mockValue))
		val result = repository.getRandomDogImage(TEST_BREED)
		assertTrue(result.isSuccess)
		assertEquals(result.getOrNull(), mockValue)
	}

	@Test
	fun getRandomDogImageFailure() = runTest {
		val mockException = RuntimeException("Network error")
		coEvery { apiService.getRandomDogImage(TEST_BREED) } throws mockException
		val result = repository.getRandomDogImage(TEST_BREED)
		assertTrue(result.isFailure)
		assertEquals(result.exceptionOrNull(), mockException)
	}

}