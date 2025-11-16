package com.fernandohbrasil.openlibraryandroid.presentation.books.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fernandohbrasil.openlibraryandroid.R
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.presentation.books.model.BookDetailsUiState
import com.fernandohbrasil.openlibraryandroid.ui.theme.OpenLibraryTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookDetailsBottomSheetTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<androidx.activity.ComponentActivity>()

    @Test
    fun bookDetailsBottomSheet_loadingState_showsLoadingIndicator() {
        val loadingState = BookDetailsUiState(
            book = null,
            isLoading = true,
            errorMessageRes = null
        )

        composeTestRule.setContent {
            OpenLibraryTheme {
                BookDetailsBottomSheet(
                    state = loadingState,
                    onDismiss = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Loading book details...").assertIsDisplayed()
    }

    @Test
    fun bookDetailsBottomSheet_successState_displaysBookDetails() {
        val bookDetails = BookDetails(
            key = "/works/OL123",
            title = "Test Book Title",
            authorNames = listOf("Test Author"),
            coverUrl = "https://example.com/cover.jpg",
            firstPublishYear = "2020",
            subjects = listOf("Fiction", "Adventure"),
            description = "This is a test book description."
        )

        val successState = BookDetailsUiState(
            book = bookDetails,
            isLoading = false,
            errorMessageRes = null
        )

        composeTestRule.setContent {
            OpenLibraryTheme {
                BookDetailsBottomSheet(
                    state = successState,
                    onDismiss = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Book Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Book Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Author", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("2020", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a test book description.").assertIsDisplayed()
    }

    @Test
    fun bookDetailsBottomSheet_errorState_showsErrorAndDismissButton() {
        val errorState = BookDetailsUiState(
            book = null,
            isLoading = false,
            errorMessageRes = R.string.shared_error_network
        )

        composeTestRule.setContent {
            OpenLibraryTheme {
                BookDetailsBottomSheet(
                    state = errorState,
                    onDismiss = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("You are offline or the network is unstable.", substring = true)
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Dismiss").assertIsDisplayed()
    }
}

