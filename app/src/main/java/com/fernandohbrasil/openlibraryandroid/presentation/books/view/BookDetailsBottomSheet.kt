package com.fernandohbrasil.openlibraryandroid.presentation.books.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.fernandohbrasil.openlibraryandroid.R
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.presentation.books.model.BookDetailsUiState


@Composable
fun BookDetailsBottomSheet(
    state: BookDetailsUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.books_details_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.shared_close)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> LoadingState()
            state.errorMessageRes != null -> ErrorState(state.errorMessageRes, onDismiss)
            state.book != null -> BookDetailsContent(book = state.book)
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(id = R.string.shared_loading_book_details))
    }
}

@Composable
private fun ErrorState(errorMessageRes: Int, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.shared_error_with_message, stringResource(id = errorMessageRes)),
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onDismiss) {
            Text(stringResource(id = R.string.shared_dismiss))
        }
    }
}

@Composable
private fun BookDetailsContent(
    book: BookDetails,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
            ) {
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = stringResource(id = R.string.books_cover_content_description, book.title),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                            )
                        }
                    },
                    success = { SubcomposeAsyncImageContent() },
                    error = {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_report_image),
                            contentDescription = null
                        )
                    },
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                if (book.authorNames.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.books_by_authors, book.authorNames.joinToString(", ")),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                if (book.firstPublishYear.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.books_published_year, book.firstPublishYear),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }

        if (book.description.isNotBlank()) {
            Column {
                Text(
                    text = stringResource(id = R.string.books_description),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                DescriptionText(
                    text = book.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        if (book.subjects.isNotEmpty()) {
            Column {
                Text(
                    text = stringResource(id = R.string.books_subjects),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = book.subjects.joinToString(separator = "; "),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun DescriptionText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val annotated = rememberAnnotatedMarkdown(text)
    Text(
        modifier = modifier,
        text = annotated,
        style = style,
    )
}

@Composable
private fun rememberAnnotatedMarkdown(text: String): AnnotatedString {
    val refRegex = Regex("^\\s*\\[([0-9A-Za-z_-]+)]:\\s*(\\S+)", setOf(RegexOption.MULTILINE))
    val refMap = refRegex.findAll(text).associate { it.groupValues[1] to it.groupValues[2] }

    val lines = text.lines()
    return buildAnnotatedString {
        val linkColor = MaterialTheme.colorScheme.primary

        fun appendInlineMarkdown(line: String) {
            var idx = 0
            val boldRegex = Regex("\\*\\*([^*]+)\\*\\*")
            val linkParenRegex = Regex("\\[([^\\]]+)]\\(([^\\)]+)\\)")
            val linkRefRegex = Regex("\\[([^\\]]+)]\\[([^\\]]+)]")

            while (idx < line.length) {
                val matchBold = boldRegex.find(line, idx)
                val matchParen = linkParenRegex.find(line, idx)
                val matchRef = linkRefRegex.find(line, idx)
                val earliest = listOfNotNull(matchBold, matchParen, matchRef).minByOrNull { it.range.first }

                if (earliest == null) {
                    append(line.substring(idx))
                    break
                }

                if (earliest.range.first > idx) {
                    append(line.substring(idx, earliest.range.first))
                }

                when (earliest) {
                    matchBold -> {
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        append(earliest.groupValues[1])
                        pop()
                    }
                    matchParen -> {
                        val label = earliest.groupValues[1]
                        val url = earliest.groupValues[2]
                        pushLink(LinkAnnotation.Url(url))
                        pushStyle(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))
                        append(label)
                        pop()
                        pop()
                    }
                    matchRef -> {
                        val label = earliest.groupValues[1]
                        val id = earliest.groupValues[2]
                        val url = refMap[id]
                        if (url != null) {
                            pushLink(LinkAnnotation.Url(url))
                            pushStyle(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))
                            append(label)
                            pop()
                            pop()
                        } else {
                            append(label)
                        }
                    }
                }
                idx = earliest.range.last + 1
            }
        }

        lines.forEachIndexed { i, raw ->
            val line = raw.trimEnd()
            when {
                line == "----------" -> {
                    append("\n")
                }
                line.trimStart().startsWith("- ") || line.trimStart().startsWith("* ") -> {
                    append("• ")
                    appendInlineMarkdown(line.trimStart().removePrefix("- ").removePrefix("* "))
                    append("\n")
                }
                refRegex.matches(line) -> {
                    // skip definition line
                }
                else -> {
                    appendInlineMarkdown(line)
                    if (i < lines.lastIndex) append("\n")
                }
            }
        }
    }
}
