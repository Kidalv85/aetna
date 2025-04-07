package com.kryvovyaz.aetna.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.models.Location
import com.kryvovyaz.aetna.models.Origin
import com.kryvovyaz.aetna.ui.theme.AetnaTheme

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CharacterScreenPreview() {
    AetnaTheme {
        CharacterSearchScreen(
            searchText = remember {
                mutableStateOf("Rick")
            },
            isShowLoadingIndicator = remember {
                mutableStateOf(false)
            },
            isShowResultText = remember {
                mutableStateOf(false)
            },
            charactersList = remember {
                mutableStateOf(listOf(mockCharacter))
            },
            onSearchValueChanged = {},
            onClearSearchIconClick = {},
            navController = rememberNavController(),
            paddingValues = PaddingValues(),
            onFilterSelected = {},
            isFilterSelected = remember {
                mutableStateOf(false)
            },
            groupedCharactersList = remember {
                mutableStateOf(mapOf())
            },
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun CharacterDetailsScreenPreview() {
    AetnaTheme {
        CharacterDetailsScreen(
            formatDate = { _ -> "2017-11-04" },
            onShareCharacterDetails = {},
            index = 0,
            charactersList = remember {
                mutableStateOf(listOf(mockCharacter) )
            }
        )
    }
}

private val mockCharacter = Character(
    id = 1,
    name = "Rick Sanchez",
    status = "Alive",
    species = "Human",
    type = "",
    gender = "Male",
    origin = Origin(name = "Earth (C-137)", url = ""),
    location = Location(name = "Citadel of Ricks", url = ""),
    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    episode = arrayListOf(),
    url = "https://rickandmortyapi.com/api/character/1",
    created = "2017-11-04T18:48:46.250Z"
)