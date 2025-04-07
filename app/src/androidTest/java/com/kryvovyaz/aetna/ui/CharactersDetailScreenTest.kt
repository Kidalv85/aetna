package com.kryvovyaz.aetna.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.ui.screens.CharacterDetailsScreen
import com.kryvovyaz.aetna.util.CHARACTER_IMAGE_TAG
import com.kryvovyaz.aetna.util.SHARE_ICON_TAG

@RunWith(AndroidJUnit4::class)
class CharactersDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var character: Character
    private lateinit var charactersList: List<Character>
    private lateinit var formatDate: (String?) -> String
    private val testList = mutableListOf<Character>()

    @Before
    fun setUp() {
        character = Character(
            name = "Rick Sanchez",
            species = "Human",
            status = "Alive",
            origin = null,
            image = "https://image.url",
            type = null,
            created = "2025-04-06"
        )
        charactersList = listOf(character)

        formatDate = { it ?: "Unknown Date" }

        composeTestRule.setContent {
            CharacterDetailsScreen(
                formatDate = formatDate,
                onShareCharacterDetails = { character->
                    testList.add(character)
                },
                index = 0,
                charactersList = mutableStateOf(charactersList)
            )
        }
    }

    @Test
    fun testCharacterDetailsScreenDisplaysCorrectCharacterDetails() {
        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText("Species : Human").assertIsDisplayed()
        composeTestRule.onNodeWithText("Status : Alive").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type").assertDoesNotExist()
        composeTestRule.onNodeWithText("Origin : Unknown Origin").assertIsDisplayed()
        composeTestRule.onNodeWithTag(CHARACTER_IMAGE_TAG).assertExists()
    }

    @Test
    fun testShareButtonIsClickable() {
        composeTestRule.onNodeWithTag(SHARE_ICON_TAG)
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        assert(testList.contains(character))
    }

    @Test
    fun testContentDescriptionForAccessibility() {
        composeTestRule.onNodeWithContentDescription("Species : Human").assertExists()
        composeTestRule.onNodeWithContentDescription("Status : Alive").assertExists()
    }

}