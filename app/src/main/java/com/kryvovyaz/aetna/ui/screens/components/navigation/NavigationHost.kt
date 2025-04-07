package com.kryvovyaz.aetna.ui.screens.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.ui.screens.CharacterDetailsScreen
import com.kryvovyaz.aetna.ui.screens.CharacterSearchScreen
import com.kryvovyaz.aetna.ui.screens.components.FilterType
import com.kryvovyaz.aetna.util.NAVIGATION_PARAM_INDEX
import com.kryvovyaz.aetna.util.NAVIGATION_SCREEN_DETAILS
import com.kryvovyaz.aetna.util.NAVIGATION_SCREEN_SEARCH

@Composable
fun NavigationHost(
    paddingValues: PaddingValues,
    navController: NavHostController,
    isShowLoadingIndicator: MutableState<Boolean>,
    isShowResultText: MutableState<Boolean>,
    searchText: MutableState<String>,
    charactersList: State<List<Character>>,
    onSearchValueChanged: (String) -> Unit,
    onClearSearchIconClick: () -> Unit,
    formatDate: (String?) -> String,
    onShareCharacterDetails: (Character) -> Unit,
    onFilterSelected: (FilterType) -> Unit,
    groupedCharactersList: State<Map<String, List<Character>>>,
    isFilterSelected: State<Boolean>,
) {
    NavHost(navController = navController, startDestination = NAVIGATION_SCREEN_SEARCH) {
        composable(NAVIGATION_SCREEN_SEARCH) {
            CharacterSearchScreen(
                paddingValues = paddingValues,
                navController = navController,
                searchText = searchText,
                isShowLoadingIndicator = isShowLoadingIndicator,
                isShowResultText = isShowResultText,
                charactersList = charactersList,
                onSearchValueChanged = onSearchValueChanged,
                onClearSearchIconClick = onClearSearchIconClick,
                onFilterSelected = onFilterSelected,
                groupedCharactersList = groupedCharactersList,
                isFilterSelected = isFilterSelected
            )
        }
        composable(
            NAVIGATION_SCREEN_DETAILS,
            arguments = listOf(navArgument(NAVIGATION_PARAM_INDEX) { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt(NAVIGATION_PARAM_INDEX) ?: -1
            if (index != -1) {
                CharacterDetailsScreen(
                    index = index,
                    charactersList = charactersList,
                    onShareCharacterDetails = onShareCharacterDetails,
                    formatDate = formatDate
                )
            }
        }
    }
}