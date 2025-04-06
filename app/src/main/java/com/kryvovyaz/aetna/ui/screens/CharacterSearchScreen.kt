package com.kryvovyaz.aetna.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.ui.screens.components.CharacterCard
import com.kryvovyaz.aetna.ui.screens.components.SearchBar
import com.kryvovyaz.aetna.ui.theme.defaultPadding
import com.kryvovyaz.aetna.ui.theme.PurpleGrey
import com.kryvovyaz.aetna.R
import com.kryvovyaz.aetna.ui.screens.components.FilterType
import com.kryvovyaz.aetna.ui.theme.defaultIconSize
import com.kryvovyaz.aetna.ui.theme.height_24
import com.kryvovyaz.aetna.ui.theme.progressBarSize
import com.kryvovyaz.aetna.ui.theme.smallPadding
import com.kryvovyaz.aetna.ui.theme.strokeWidth
import com.kryvovyaz.aetna.util.DOLLAR
import com.kryvovyaz.aetna.util.EXCLAMATION
import com.kryvovyaz.aetna.util.NAVIGATION_SCREEN_DETAILS_ROOT
import com.kryvovyaz.aetna.ui.theme.PurpleDarkGrey

@Composable
fun CharacterSearchScreen(
    isShowLoadingIndicator: MutableState<Boolean>,
    isShowResultText: MutableState<Boolean>,
    searchText: MutableState<String>,
    charactersList: State<List<Character>>,
    onSearchValueChanged: (String) -> Unit,
    onClearSearchIconClick: () -> Unit,
    navController: NavHostController,
    paddingValues: PaddingValues,
    onFilterSelected: (FilterType) -> Unit,
    groupedCharactersList: State<Map<String, List<Character>>>,
    isFilterSelected: State<Boolean>
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(defaultPadding)
            .semantics {
                contentDescription = context.getString(R.string.app_search_screen_description)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            SearchBar(
                text = searchText.value,
                readOnly = false,
                onValueChange = { newValue ->
                    onSearchValueChanged.invoke(newValue)
                },
                onClear = {
                    onClearSearchIconClick()
                },
                onFilterSelected = onFilterSelected
            )
            AnimatedVisibility(visible = searchText.value.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.app_search_icon_description),
                    modifier = Modifier
                        .size(defaultIconSize)
                        .padding(horizontal = defaultPadding)
                        .weight(1f)
                        .clickable {
                            onClearSearchIconClick()
                        }
                )
            }
        }

        if (isShowLoadingIndicator.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(progressBarSize)
                    .padding(
                        top = defaultPadding,
                        bottom = defaultPadding
                    )
                    .semantics {
                        contentDescription =
                            context.getString(R.string.app_loading_indicator_description)
                    },
                color = PurpleGrey,
                strokeWidth = strokeWidth,
                trackColor = PurpleDarkGrey,
            )
        }
        val searchResultText =
            if (!isShowResultText.value) stringResource(R.string.app_search_criteria)
            else stringResource(R.string.app_search_results).replace(
                DOLLAR,
                charactersList.value.size.toString()
            )
                .replace(EXCLAMATION, searchText.value)

        Text(
            text = searchResultText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = defaultPadding)
                .semantics { contentDescription = searchResultText },
            style = MaterialTheme.typography.bodyLarge

        )

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = defaultPadding),
            verticalArrangement = Arrangement.spacedBy(defaultPadding)
        ) {
            if (isFilterSelected.value) {
                groupedCharactersList.value.forEach { (category, characters) ->
                    item {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(
                                    horizontal = defaultPadding,
                                    vertical = smallPadding
                                )
                                .semantics { contentDescription = category }
                        )
                    }

                    items(characters) { character ->
                        CharacterCard(
                            character = character
                        ) {
                            val index: Int = charactersList.value.indexOf(character)
                            navController.navigate("details_screen/$index")
                        }
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(smallPadding)
                        )
                    }
                }
            } else {
                itemsIndexed(charactersList.value) { index, item ->
                    CharacterCard(
                        character = item,
                        onCardClick = {
                            navController.navigate(NAVIGATION_SCREEN_DETAILS_ROOT + index)
                        }
                    )
                    if (index == charactersList.value.size - 1) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height_24)
                                .background(
                                    Color.Transparent
                                )
                        )
                    }
                }
            }
        }
    }
}
