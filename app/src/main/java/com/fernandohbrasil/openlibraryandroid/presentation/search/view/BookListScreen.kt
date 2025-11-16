package com.fernandohbrasil.openlibraryandroid.presentation.search.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.presentation.books.model.BookDetailsUiState
import com.fernandohbrasil.openlibraryandroid.presentation.books.view.BookDetailsBottomSheet
import com.fernandohbrasil.openlibraryandroid.ui.theme.gradientBrush
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.rx3.asFlow
import com.fernandohbrasil.openlibraryandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    booksObservable: Observable<PagingData<SearchBook>>?,
    bookDetailsState: StateFlow<BookDetailsUiState>,
    onBookClick: (SearchBook) -> Unit,
    onDismissDetails: () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    val detailsState = bookDetailsState.collectAsState().value
    val showBottomSheet = detailsState.book != null || detailsState.isLoading || detailsState.errorMessageRes != null

    val pagingFlow = remember(booksObservable) { booksObservable?.asFlow() }
    val lazyPagingItems: LazyPagingItems<SearchBook>? = pagingFlow?.collectAsLazyPagingItems()
    val isInitialLoading = lazyPagingItems != null &&
        lazyPagingItems.itemCount == 0 &&
        (lazyPagingItems.loadState.refresh is LoadState.Loading)

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(gradientBrush())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(id = R.string.search_field_label)) },
                    placeholder = { Text(stringResource(id = R.string.search_field_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search_button)
                        )
                    },
                    singleLine = true,
                    enabled = !isInitialLoading,
                )
                Button(
                    onClick = {
                        if (searchQuery.isNotBlank()) {
                            onSearch(searchQuery.trim())
                        }
                    },
                    enabled = !isInitialLoading && searchQuery.isNotBlank(),
                ) {
                    Text(stringResource(id = R.string.search_button))
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                val isInitial = lazyPagingItems == null
                val isEmpty = lazyPagingItems?.itemCount == 0
                val refreshState = lazyPagingItems?.loadState?.refresh

                when {
                    isInitial -> InitialState()
                    isEmpty && refreshState is LoadState.Loading -> ShimmerBookListPlaceholder()
                    isEmpty && refreshState is LoadState.Error -> ErrorState(refreshState, searchQuery, onSearch)
                    isEmpty -> EmptyState()
                    else -> SuccessState(lazyPagingItems, onBookClick, lazyPagingItems.loadState.append)
                }
            }

            ShowBookDetails(showBottomSheet, onDismissDetails, detailsState)
        }
    }
}




@Composable
private fun InitialState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(id = R.string.search_empty_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(id = R.string.search_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ShimmerBookListPlaceholder(
    items: Int = 8,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(count = items, key = { it }, contentType = { "placeholder" }) { _ ->
            BookItemPlaceholder()
        }
    }
}

@Composable
private fun BookItemPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ShimmerBox(
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp),
                shape = RectangleShape,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(18.dp),
                )
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp),
                )
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(12.dp),
                )
            }
        }
    }
}

@Composable
private fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate",
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(shimmerTranslate - 200f, 0f),
        end = Offset(shimmerTranslate, 0f),
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}

@Composable
private fun ErrorState(refresh: LoadState, searchQuery: String, onSearch: (String) -> Unit) {
    val error = (refresh as LoadState.Error).error
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(
                id = R.string.shared_error_with_message,
                error.message ?: stringResource(id = R.string.search_failed_to_load)
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (searchQuery.isNotBlank()) {
                onSearch(searchQuery.trim())
            }
        }) {
            Text(stringResource(id = R.string.shared_retry))
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(id = R.string.search_no_results_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(id = R.string.search_no_results_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SuccessState(
    lazyPagingItems: LazyPagingItems<SearchBook>,
    onBookClick: (SearchBook) -> Unit,
    append: LoadState,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.key },
        ) { index ->
            val book = lazyPagingItems[index]
            if (book != null) {
                BookItem(
                    book = book,
                    onClick = { onBookClick(book) },
                )
            }
        }

        // Loading footer
        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        // Error footer
        if (lazyPagingItems.loadState.append is LoadState.Error) {
            val error = (append as LoadState.Error).error
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.shared_error_with_message,
                                error.message ?: stringResource(id = R.string.search_failed_to_load_more)
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                        Button(onClick = {
                            lazyPagingItems.retry()
                        }) {
                            Text(stringResource(id = R.string.shared_retry))
                        }
                    }
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ShowBookDetails(
    showBottomSheet: Boolean,
    onDismissDetails: () -> Unit,
    detailsState: BookDetailsUiState
) {
    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
        ModalBottomSheet(
            onDismissRequest = onDismissDetails,
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth(),
        ) {
            BookDetailsBottomSheet(
                state = detailsState,
                onDismiss = onDismissDetails,
            )
        }
    }
}

