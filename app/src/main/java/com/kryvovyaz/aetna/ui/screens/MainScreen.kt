package com.kryvovyaz.aetna.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.ui.screens.components.ErrorAlertDialog
import com.kryvovyaz.aetna.ui.screens.components.FilterType
import com.kryvovyaz.aetna.ui.screens.components.TopBar
import com.kryvovyaz.aetna.ui.screens.components.navigation.NavigationHost
import com.kryvovyaz.aetna.ui.theme.AetnaTheme

@Composable
fun MainScreen(
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
    onTopBarBackIconClick: () -> Unit,
    isShowErrorDialog: MutableState<Boolean>,
    onOkButtonClick: () -> Unit
) {
    AetnaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    TopBar(onTopBarBackIconClick = onTopBarBackIconClick)
                },
                content = { paddingValues ->
                    NavigationHost(
                        paddingValues = paddingValues,
                        navController = navController,
                        searchText = searchText,
                        isShowLoadingIndicator = isShowLoadingIndicator,
                        isShowResultText = isShowResultText,
                        charactersList = charactersList,
                        onSearchValueChanged = onSearchValueChanged,
                        onClearSearchIconClick = onClearSearchIconClick,
                        formatDate = formatDate,
                        onShareCharacterDetails = onShareCharacterDetails,
                        onFilterSelected = onFilterSelected,
                        groupedCharactersList = groupedCharactersList,
                        isFilterSelected = isFilterSelected
                    )
                    ErrorAlertDialog(
                        isShowErrorDialog = isShowErrorDialog,
                        onOkButtonClick = onOkButtonClick
                    )
                })
        }
    }
}