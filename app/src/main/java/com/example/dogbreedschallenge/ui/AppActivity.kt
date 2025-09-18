@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dogbreedschallenge.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import com.example.dogbreedschallenge.R
import com.example.dogbreedschallenge.ui.theme.AppTheme
import com.example.dogbreedschallenge.ui.theme.dimensions
import com.example.dogbreedschallenge.utils.capitalize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {

		// Edge-To-Edge config
		val navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
		enableEdgeToEdge(navigationBarStyle = navigationBarStyle)

		super.onCreate(savedInstanceState)

		setContent {

			AppTheme {
				val viewModel = hiltViewModel<AppViewModel>()
				val uiState by viewModel.uiStateFlow.collectAsState()
				Root(
					uiState,
					onRetryLoadingList = { viewModel.loadAllBreeds() },
					onAnswerChanged = { viewModel.updateAnswer(it) },
					onCheckAnswer = { viewModel.checkAnswer() },
					onNext = { viewModel.loadNextBreed() }
				)
			}

		}

	}

}

@Composable
private fun Root(uiState: UiState,
                 onRetryLoadingList: () -> Unit,
                 onAnswerChanged: (String) -> Unit,
                 onCheckAnswer: () -> Unit,
                 onNext: () -> Unit) {

	Column(
		modifier = Modifier.fillMaxSize()
			.background(color = MaterialTheme.colorScheme.background)
			.safeContentPadding()
			.padding(horizontal = MaterialTheme.dimensions.medium)
			.padding(top = MaterialTheme.dimensions.medium),
		verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.medium)
	) {

		// Title
		Text(
			stringResource(R.string.app_name),
			style = MaterialTheme.typography.headlineLarge,
			color = MaterialTheme.colorScheme.primary
		)

		when (uiState.inputsState) {

			is LoadingState.Success -> Content(
				uiState = uiState,
				onAnswerChanged = onAnswerChanged,
				onCheckAnswer = onCheckAnswer,
				onNext = onNext
			)

			is LoadingState.Error -> ErrorMessage(onRetry = onRetryLoadingList)

			// None/Loading
			else -> LoadingUi()

		}

	}

}

@Composable
private fun ColumnScope.LoadingUi() {

	// Empty Space
	Spacer(modifier = Modifier.weight(1f))

	// Loading Icon
	CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))

	// Empty Space
	Spacer(modifier = Modifier.weight(1f))

}

@Composable
private fun ColumnScope.ErrorMessage(onRetry: () -> Unit) {

	// Error Text
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
	) {

		Row(
			modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimensions.small),
			horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.small),
			verticalAlignment = Alignment.CenterVertically
		) {

			Icon(
				imageVector = Icons.Filled.Warning,
				contentDescription = stringResource(R.string.content_description_info)
			)

			Text(
				stringResource(R.string.error),
				style = MaterialTheme.typography.bodyMedium
			)

		}

	}

	// Empty Space
	Spacer(modifier = Modifier.weight(1f))

	// Retry Button
	ElevatedButton(
		onClick = onRetry,
		modifier = Modifier.fillMaxWidth(),
		shape = MaterialTheme.shapes.medium,
		colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
	) {
		Text(stringResource(R.string.retry))
	}

	// Empty Space
	Spacer(modifier = Modifier.weight(1f))

}

@Composable
private fun Content(uiState: UiState,
                    onAnswerChanged: (String) -> Unit,
                    onCheckAnswer: () -> Unit,
                    onNext: () -> Unit) {

	Column(
		modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = MaterialTheme.dimensions.medium),
		verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.medium)
	) {

		// Instruction
		Instruction()

		// Dog Picture
		Image(uiState = uiState)

		// Question
		Text(stringResource(R.string.question))

		// Answer
		Input(uiState = uiState, onValueChange = onAnswerChanged)

		// Empty Space
		Spacer(modifier = Modifier.weight(1f))

		// Confirm Button
		ElevatedButton(
			onClick = onCheckAnswer,
			modifier = Modifier.fillMaxWidth(),
			shape = MaterialTheme.shapes.medium,
			colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
			enabled = uiState.userAnswer.isNotEmpty()
		) {
			Text(stringResource(R.string.check_answer))
		}

		// Next Button
		ElevatedButton(
			onClick = onNext,
			modifier = Modifier.fillMaxWidth(),
			shape = MaterialTheme.shapes.medium,
			colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
		) {
			Text(stringResource(R.string.next_picture))
		}

	}

}

@Composable
private fun ColumnScope.Image(uiState: UiState) {

	when (uiState.imageUrlState) {

		is LoadingState.Success<*> -> SubcomposeAsyncImage(

			model = uiState.imageUrlState.data,
			contentDescription = stringResource(R.string.content_description_picture),
			modifier = Modifier
				.fillMaxWidth()
				.clip(MaterialTheme.shapes.medium),
			contentScale = ContentScale.FillWidth,

			loading = {

				// Wrapped by Box to apply alignment.
				Box(contentAlignment = Alignment.Center) {
					ImageLoadingIndicator()
				}

			},

			error = {

				// Wrapped by Box to apply alignment.
				Box(contentAlignment = Alignment.Center) {
					ImageLoadingError()
				}

			}

		)

		is LoadingState.Error -> ImageLoadingError(modifier = Modifier.align(Alignment.CenterHorizontally))

		else -> ImageLoadingIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))

	}

}

@Composable
private fun ImageLoadingIndicator(modifier: Modifier = Modifier) {
	CircularProgressIndicator(modifier = modifier.size(MaterialTheme.dimensions.extraLarge))
}

@Composable
private fun ImageLoadingError(modifier: Modifier = Modifier) {
	Icon(
		Icons.Filled.Warning,
		modifier = modifier
			.fillMaxWidth()
			.height(MaterialTheme.dimensions.extraLarge)
			.clip(MaterialTheme.shapes.medium)
			.background(color = MaterialTheme.colorScheme.secondaryContainer)
			.padding(MaterialTheme.dimensions.medium),
		contentDescription = stringResource(R.string.content_description_unable_to_load_picture)
	)
}

@Composable
private fun Instruction() {

	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
	) {

		Row(
			modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimensions.small),
			horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.small),
			verticalAlignment = Alignment.CenterVertically
		) {

			Icon(
				imageVector = Icons.Filled.Info,
				contentDescription = stringResource(R.string.content_description_info)
			)

			Text(
				stringResource(R.string.instruction),
				style = MaterialTheme.typography.bodyMedium
			)

		}

	}

}

@Composable
private fun Input(uiState: UiState, onValueChange: (String) -> Unit) {

	var isShowingBottomSheet by remember { mutableStateOf(false) }
	val isAnswerCorrect = uiState.isAnswerCorrect

	// Bottom Sheet
	if (isShowingBottomSheet) {
		val list = (uiState.inputsState as LoadingState.Success<List<String>>).data
		AnswerSelectionBottomSheet(
			list = list,
			onSelected = { onValueChange(it) },
			onDismissRequest = { isShowingBottomSheet = false }
		)
	}

	// Text Field ====================================================== 

	val leadingIcon = @Composable {

		IconButton(onClick = { isShowingBottomSheet = true }) {
			Icon(
				imageVector = Icons.Filled.Edit,
				contentDescription = stringResource(R.string.content_description_select_from_list)
			)
		}

	}

	val placeholder = @Composable {
		Text(stringResource(R.string.placeholder_input))
	}

	val supportingText = if (isAnswerCorrect == null) {

		null

	} else {

		@Composable {
			val resource = if (isAnswerCorrect) R.string.correct_response else R.string.incorrect_response
			Text(stringResource(resource))
		}

	}

	OutlinedTextField(
		value = uiState.userAnswer,
		onValueChange = onValueChange,
		modifier = Modifier.fillMaxWidth(),
		readOnly = true,
		shape = MaterialTheme.shapes.medium,
		placeholder = placeholder,
		leadingIcon = leadingIcon,
		supportingText = supportingText,
		isError = isAnswerCorrect == false
	)

	//  ====================================================== 

}

@Composable
private fun AnswerSelectionBottomSheet(list: List<String>,
                                       sheetState: SheetState = rememberModalBottomSheetState(),
                                       onSelected: (String) -> Unit,
                                       onDismissRequest: () -> Unit) {

	val scope = rememberCoroutineScope()
	var searchText by remember { mutableStateOf("") }
	val formattedList = list.map { it.capitalize() }
	val filteredList = formattedList.filter { it.contains(searchText, ignoreCase = true) }

	ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {

		Column(
			modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimensions.medium),
			verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.medium)
		) {

			// Search 
			val leadingIcon = @Composable {
				Icon(
					imageVector = Icons.Filled.Search,
					contentDescription = stringResource(R.string.search)
				)
			}
			OutlinedTextField(
				value = searchText,
				onValueChange = { searchText = it },
				modifier = Modifier.fillMaxWidth(),
				placeholder = { Text(stringResource(R.string.search)) },
				shape = MaterialTheme.shapes.medium,
				leadingIcon = leadingIcon
			)

			// List
			LazyColumn(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.extraSmall)
			) {

				items(filteredList) {

					val onClick: () -> Unit = {

						onSelected(it)
						searchText = ""

						scope.launch { sheetState.hide() }.invokeOnCompletion {
							if (!sheetState.isVisible) onDismissRequest()
						}

					}

					Card(
						colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
						shape = MaterialTheme.shapes.small,
						modifier = Modifier.fillMaxWidth(),
						onClick = onClick
					) {
						Text(it, modifier = Modifier.padding(MaterialTheme.dimensions.small))
					}

				}

			}

		}

	}

}

@Preview(showBackground = true)
@Composable
private fun PreviewDefault() = AppTheme {
	Root(
		uiState = UiState(inputsState = LoadingState.Success(listOf("Affenpinscher", "African", "Airedale"))),
		onRetryLoadingList = {},
		onAnswerChanged = {},
		onCheckAnswer = {},
		onNext = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewError() = AppTheme {
	Root(
		uiState = UiState(inputsState = LoadingState.Error),
		onRetryLoadingList = {},
		onAnswerChanged = {},
		onCheckAnswer = {},
		onNext = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewInitialLoading() = AppTheme {
	Root(
		uiState = UiState(inputsState = LoadingState.Loading),
		onRetryLoadingList = {},
		onAnswerChanged = {},
		onCheckAnswer = {},
		onNext = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewImageLoadingError() = AppTheme {
	Root(
		uiState = UiState(imageUrlState = LoadingState.Error, inputsState = LoadingState.Success(emptyList())),
		onRetryLoadingList = {},
		onAnswerChanged = {},
		onCheckAnswer = {},
		onNext = {})
}

@Preview
@Composable
private fun PreviewAnswerSelectionBottomSheet() = AppTheme {

	AnswerSelectionBottomSheet(
		list = listOf("affenpinscher", "african", "airedale"),
		sheetState = SheetState(skipPartiallyExpanded = true, density = LocalDensity.current, initialValue = SheetValue.Expanded),
		onSelected = {},
		onDismissRequest = {}
	)

} 