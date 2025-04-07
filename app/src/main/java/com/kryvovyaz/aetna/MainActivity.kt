package com.kryvovyaz.aetna

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kryvovyaz.aetna.ui.screens.MainScreen
import com.kryvovyaz.aetna.util.NAVIGATION_SCREEN_SEARCH
import com.kryvovyaz.aetna.util.ShareHelper.shareCharacterDetails
import com.kryvovyaz.aetna.viewmodel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        actionBar?.hide()
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry = navController.currentBackStackEntryAsState()
            MainScreen(
                onTopBarBackIconClick = { onBackPressedDispatcher.onBackPressed() },
                navController = navController,
                searchText = viewModel.searchText,
                isShowLoadingIndicator = viewModel.isShowLoadingIndicator,
                isShowResultText = viewModel.isShowResultText,
                charactersList = viewModel.charactersList,
                onSearchValueChanged = { newSearchValue ->
                    viewModel.onSearchQueryChange(newSearchValue)
                },
                onClearSearchIconClick = {
                    viewModel.clearSearchState()
                },
                formatDate = { date ->
                    viewModel.formatDate(date)
                },
                onShareCharacterDetails = { character ->
                    shareCharacterDetails(
                        this@MainActivity, character, viewModel.isShowErrorDialog
                    )
                },
                onFilterSelected = { filter ->
                    viewModel.onFilterSelected(filter)
                },
                groupedCharactersList = viewModel.groupedCharactersList,
                isFilterSelected = viewModel.isGrouped,
                isShowErrorDialog = viewModel.isShowErrorDialog,
                onOkButtonClick = {
                    viewModel.hideAlertDialog()
                }
            )

            LaunchedEffect(currentBackStackEntry.value) {
                onBackPressedDispatcher.addCallback(this@MainActivity,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            val isRootScreen =
                                currentBackStackEntry.value?.destination?.route == NAVIGATION_SCREEN_SEARCH

                            if (isRootScreen) {
                                finish()
                                viewModel.clearSearchState()
                            } else {
                                navController.popBackStack()
                            }
                        }
                    })
            }
        }
    }
}