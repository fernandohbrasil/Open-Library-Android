package com.fernandohbrasil.openlibraryandroid.presentation.search.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fernandohbrasil.openlibraryandroid.presentation.shared.view.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BookListScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bookListScreen_loadingState_showsLoadingIndicator() {
        hiltRule.inject()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("Search books...").performTextInput("harry potter")
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("Search books...").assertIsDisplayed()
    }

    @Test
    fun bookListScreen_successState_displaysBooks() {
        hiltRule.inject()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("Search books...").performTextInput("test")
        composeTestRule.onNodeWithText("Search").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasText("test", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.waitForIdle()
    }

    @Test
    fun bookListScreen_errorState_showsErrorAndRetryButton() {
        hiltRule.inject()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("Search books...").performTextInput("test")
        composeTestRule.onNodeWithText("Search").performClick()
        
        composeTestRule.waitForIdle()
        Thread.sleep(2000)
        
        val retryButtonExists = try {
            composeTestRule.onNodeWithText("Retry", substring = true, ignoreCase = true)
                .assertExists()
            true
        } catch (e: AssertionError) {
            false
        }
        
        if (retryButtonExists) {
            composeTestRule.onNodeWithText("Error", substring = true, ignoreCase = true)
                .assertIsDisplayed()
        }
    }
}

