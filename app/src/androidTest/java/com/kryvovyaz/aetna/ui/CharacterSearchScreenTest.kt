package com.kryvovyaz.aetna.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.ui.screens.CharacterSearchScreen
import com.kryvovyaz.aetna.ui.screens.components.FilterType
import com.kryvovyaz.aetna.ui.theme.AetnaTheme
import com.kryvovyaz.aetna.util.DELETE_ICON_TAG
import com.kryvovyaz.aetna.util.FILTER_ICON_TAG
import com.kryvovyaz.aetna.util.LOADING_INDICATOR_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var charactersListState: MutableState<List<Character>>
    private lateinit var searchTextState: MutableState<String>
    private lateinit var isShowLoadingIndicatorState: MutableState<Boolean>
    private lateinit var isShowResultTextState: MutableState<Boolean>
    private val filterStatus = mutableStateOf(FilterType.STATUS)
    private lateinit var mockCharacterList: MutableList<Character>

    @Before
    fun setUp() {
        mockCharacterList = mutableListOf(
            Character(name = "Rick Sanchez", species = "Human", status = "Alive"),
            Character(name = "Morty Smith", species = "Human", status = "Alive")
        )

        charactersListState = mutableStateOf(mockCharacterList)
        searchTextState = mutableStateOf("")
        isShowLoadingIndicatorState = mutableStateOf(false)
        isShowResultTextState = mutableStateOf(true)
    }

    private fun setUpScreen() {
        composeTestRule.setContent {
            AetnaTheme {
                CharacterSearchScreen(
                    isShowLoadingIndicator = isShowLoadingIndicatorState,
                    isShowResultText = isShowResultTextState,
                    searchText = searchTextState,
                    charactersList = charactersListState,
                    onSearchValueChanged = { searchTextState.value = it },
                    onClearSearchIconClick = { mockCharacterList.clear() },
                    navController = rememberNavController(),
                    paddingValues = PaddingValues(),
                    onFilterSelected = { it ->
                        filterStatus.value = it
                    },
                    groupedCharactersList = remember {
                        mutableStateOf(mapOf())
                    },
                    isFilterSelected = remember {
                        mutableStateOf(false)
                    }
                )
            }
        }
    }

    @Test
    fun testShowsLoadingIndicatorWhenLoading() {
        // Given the loading state is true
        isShowLoadingIndicatorState.value = true

        setUpScreen()

        composeTestRule.onNodeWithContentDescription("Loading indicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOADING_INDICATOR_TAG).assertExists().assertIsDisplayed()
    }

    @Test
    fun testDisplaysSearchResultsWhenResultsAreAvailable() {
        // Given the results are not loading
        isShowLoadingIndicatorState.value = false
        isShowResultTextState.value = true
        searchTextState.value = "r"

        setUpScreen()

        composeTestRule.onNodeWithText("Found 2 result(s) for your query 'r'").assertIsDisplayed()
    }


    @Test
    fun testDisplaysCharacterCard() {
        isShowLoadingIndicatorState.value = false
        isShowResultTextState.value = true

        setUpScreen()

        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText("Morty Smith").assertIsDisplayed()
    }


    @Test
    fun testClearingSearchText() {
        // Set the search text and verify
        searchTextState.value = "Rick"
        setUpScreen()

        composeTestRule.onNodeWithTag(DELETE_ICON_TAG).assertIsDisplayed()
            .assertHasClickAction().performClick()
        assert(mockCharacterList.isEmpty())
    }

    @Test
    fun testFilterSelection() {
        setUpScreen()
        composeTestRule.onNodeWithTag(FILTER_ICON_TAG).assertIsDisplayed().assertHasClickAction()
            .performClick()

        composeTestRule.onAllNodes(hasText("Status"))[0].assertIsDisplayed().assertHasClickAction()
        composeTestRule.onAllNodes(hasText("Species"))[0].assertIsDisplayed().assertHasClickAction()
        composeTestRule.onAllNodes(hasText("Type"))[0].assertIsDisplayed().assertHasClickAction()
        composeTestRule.onAllNodes(hasText("None"))[0].assertIsDisplayed().assertHasClickAction()
            .performClick()

        assert(filterStatus.value == FilterType.NONE)
    }
    //TODO : add more tests
}
