package com.kryvovyaz.aetna.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    onTopBarIconClick: () -> Unit,
    text: MutableState<String>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.wrapContentSize(),
                navigationIcon = {
                    IconButton(onClick = onTopBarIconClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(text = "Aetna Search")
                }
            )
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SearchBar(
                    text = "",
                    readOnly = false,
                    onValueChange = {
                        // event(SearchEvent.UpdateSearchQuery(it))
                    },
                    onSearch = {
                        //event(SearchEvent.SearchNews)
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = text.value,
                    modifier = Modifier.fillMaxSize(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

//            state.articles?.let {
//                val articles = it.collectAsLazyPagingItems()
//                ArticlesList(
//                    articles = articles,
//                    onClick = navigateToDetails
//                )
//            }

//            when {
//                state.isLoading -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//                state.error != null -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Error: ${state.error}",
//                            color = MaterialTheme.colorScheme.error,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.padding(16.dp)
//                        )
//                    }
//                }
//                state.characters.isEmpty() && !state.isLoading && state.searchQuery.isNotBlank() -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "No characters found for '${state.searchQuery}'",
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.padding(16.dp)
//                        )
//                    }
//                }
//                else -> {
////                    CharacterGrid(
////                        characters = state.characters,
////                        modifier = Modifier.fillMaxSize()
////                    )
//                }
                //  }
            }
        }
    )
}

// Rest of the components remain the same...


@Composable
private fun SearchBar(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
    text: String,
    readOnly: Boolean,
    onClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isClicked = interactionSource.collectIsPressedAsState().value
    LaunchedEffect(key1 = isClicked) {
        if (isClicked) {
            onClick?.invoke()
        }
    }

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .searchBar(),
            value = text,
            onValueChange = onValueChange,
            readOnly = readOnly,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            },
            placeholder = {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors().copy(
                errorPlaceholderColor = Color.Red,
                focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            ),
            textStyle = MaterialTheme.typography.bodySmall,
            interactionSource = interactionSource
        )
    }
}

fun Modifier.searchBar(): Modifier = composed {
    if (!isSystemInDarkTheme()) {
        border(
            width = 1.dp,
            color = Color.Black,
            shape = MaterialTheme.shapes.medium
        )
    } else {
        this
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchBarPreview() {
    AetnaTheme {
        SearchBar(text = "", onValueChange = {}, readOnly = false, onSearch = {})
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CharacterScreenPreview() {
    AetnaTheme {
        CharacterScreen(
            text = remember {
                mutableStateOf("")
            },
            onTopBarIconClick = {}
        )
    }
}