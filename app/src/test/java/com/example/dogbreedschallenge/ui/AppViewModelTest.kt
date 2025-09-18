package com.example.dogbreedschallenge.ui

import app.cash.turbine.test
import com.example.dogbreedschallenge.data.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class) class AppViewModelTest {

	companion object {
		val MOCK_BREEDS = listOf("a", "b", "c")
	}

	@MockK private lateinit var repository: Repository
	private lateinit var viewModel: AppViewModel
	private val testDispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {

		MockKAnnotations.init(this)
		Dispatchers.setMain(testDispatcher)

		coEvery { repository.getAllDogs() } returns Result.success(MOCK_BREEDS)
		coEvery { repository.getRandomDogImage("a") } returns Result.failure(RuntimeException("Error"))
		coEvery { repository.getRandomDogImage("b") } returns Result.failure(RuntimeException("Error"))
		coEvery { repository.getRandomDogImage("c") } returns Result.failure(RuntimeException("Error"))

		viewModel = spyk(AppViewModel(repository))

	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun updateAnswer() = runTest {
		val answer = "answer"
		viewModel.updateAnswer(answer)
		assertEquals(answer, viewModel.uiStateFlow.first().userAnswer)
	}

	@Test
	fun checkAnswer() = runTest {		
		val answer = viewModel.uiStateFlow.first().correctAnswer
		viewModel.updateAnswer(answer)
		viewModel.checkAnswer()
		assertEquals(true, viewModel.uiStateFlow.value.isAnswerCorrect)
	}

	@Test
	fun loadAllBreeds() = runTest {

		println("test")
		viewModel.loadAllBreeds()

		viewModel.uiStateFlow.test {

			// Loading state
			assertEquals(LoadingState.Loading, awaitItem().inputsState)

			// Image random, ignore
			awaitItem()

			// Loaded state
			val loadedState = awaitItem().inputsState
			assertTrue(loadedState is LoadingState.Success)
			assertEquals((loadedState as LoadingState.Success).data, MOCK_BREEDS)
			coVerify(exactly = 1) { viewModel.loadNextBreed() }
			cancelAndIgnoreRemainingEvents()

		}

	}

	@Test
	fun loadAllBreedsFailure() = runTest {

		coEvery { repository.getAllDogs() } returns Result.failure(RuntimeException("Error"))
		viewModel.loadAllBreeds()

		viewModel.uiStateFlow.test {

			// Loading state
			assertEquals(LoadingState.Loading, awaitItem().inputsState)

			// Image random, ignore
			awaitItem()

			// Loaded state
			val loadedState = awaitItem().inputsState
			assertTrue(loadedState is LoadingState.Error)
			coVerify(exactly = 0) { viewModel.loadNextBreed() }
			cancelAndIgnoreRemainingEvents()

		}

	}

	@Test
	fun loadNextBreed() = runTest {

		val expectedData = "data"
		val expectedResult = Result.success(expectedData)
		coEvery { repository.getRandomDogImage("a") } returns expectedResult
		coEvery { repository.getRandomDogImage("b") } returns expectedResult
		coEvery { repository.getRandomDogImage("c") } returns expectedResult
		
		viewModel.loadNextBreed()

		viewModel.uiStateFlow.test {

			// Loading state
			assertEquals(LoadingState.Loading, awaitItem().imageUrlState)

			// Loaded state
			val loadedState = awaitItem().imageUrlState
			assertTrue(loadedState is LoadingState.Success)
			assertEquals((loadedState as LoadingState.Success).data, expectedData)

		}

	}

	@Test
	fun loadNextBreedFailure() = runTest {

		viewModel.loadNextBreed()

		viewModel.uiStateFlow.test {

			// Loading state
			assertEquals(LoadingState.Loading, awaitItem().imageUrlState)

			// Loaded state
			val loadedState = awaitItem().imageUrlState
			assertTrue(loadedState is LoadingState.Error)

		}

	}

}