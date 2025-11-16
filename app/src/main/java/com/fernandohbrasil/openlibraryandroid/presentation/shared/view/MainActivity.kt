package com.fernandohbrasil.openlibraryandroid.presentation.shared.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fernandohbrasil.openlibraryandroid.presentation.books.viewmodel.BookDetailsViewModel
import com.fernandohbrasil.openlibraryandroid.presentation.search.view.BookListScreen
import com.fernandohbrasil.openlibraryandroid.presentation.search.viewmodel.BookListViewModel
import com.fernandohbrasil.openlibraryandroid.ui.theme.OpenLibraryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenLibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bookListViewModel: BookListViewModel = viewModel()
                    val bookDetailsViewModel: BookDetailsViewModel = viewModel()
                    val booksObservable = bookListViewModel.booksObservable.collectAsState().value
                    BookListScreen(
                        booksObservable = booksObservable,
                        bookDetailsState = bookDetailsViewModel.bookDetailsState,
                        onBookClick = bookDetailsViewModel::loadBookDetails,
                        onDismissDetails = bookDetailsViewModel::clearBookDetails,
                        onSearch = bookListViewModel::searchBooks,
                    )
                }
            }
        }
    }
}