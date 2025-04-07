package com.kryvovyaz.aetna.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.icu.text.SimpleDateFormat
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.models.CharactersContainerResult
import com.kryvovyaz.aetna.repository.CharacterRepositoryImpl
import com.kryvovyaz.aetna.ui.screens.components.FilterType
import com.kryvovyaz.aetna.util.UNKNOWN_DATE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.launch
import io.mockk.verify
import org.junit.After

@ExperimentalCoroutinesApi
class CharacterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: CharacterRepositoryImpl

    @RelaxedMockK
    lateinit var mockCharacter: Character

    private lateinit var viewModel: CharacterViewModel
    private lateinit var spyk: CharacterViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockCharacter.name } returns "Rick"
        every { mockCharacter.status } returns "Alive"
        every { mockCharacter.species } returns "Human"
        every { mockCharacter.type } returns null
        every { mockCharacter.gender } returns null
        every { mockCharacter.origin } returns null
        viewModel = CharacterViewModel(repository)
        spyk = spyk(viewModel, recordPrivateCalls = true)
    }

    @After
    fun tearUp(){
        unmockkAll()
    }

    //TODO:More tests need to be added but due to a time limit not adding them all

    @Test
    fun `test onSearchQueryChange success`() = runTest {
        val query = "Rick"
        val characters = arrayListOf(
            mockCharacter
        )
        coEvery { repository.fetchCharactersContainer(query) } returns CharactersContainerResult.Success(
            characters
        )
        every { spyk["updateLoadingState"]() } returns Unit
        every { spyk["updateFilteredList"]() } returns Unit

        val job = launch {
            spyk.onSearchQueryChange(query)
            assertEquals(query, spyk.searchText.value)
            verify { spyk["updateLoadingState"]() }
            verify { spyk["updateFilteredList"]() }
            assert(spyk.charactersList.value.contains(mockCharacter))
            coVerify { repository.fetchCharactersContainer(query) }
        }
        job.cancel()
    }

    @Test
    fun `test onSearchQueryChange failure`() = runTest {
        every { spyk.showAlertDialog() } returns Unit
        val query = "NonExistingCharacter"
        coEvery { repository.fetchCharactersContainer(query) } returns CharactersContainerResult.Failure

        val job = launch {
            spyk.onSearchQueryChange(query)
            assertEquals(query, spyk.searchText.value)
            verify { spyk["updateLoadingState"]() }
            verify { spyk.showAlertDialog() }
        }
        job.cancel()
    }

    @Test
    fun `test onSearchQueryChange no query`() = runTest {
        spyk.searchText.value = "test"
        every { spyk.clearSearchState() } returns Unit

        val job = launch {
            spyk.onSearchQueryChange("")
            verify { spyk.clearSearchState() }
            assert(spyk.searchText.value.isEmpty())
        }
        job.cancel()
    }

    @Test
    fun `test formatDate with valid date`() {
        mockkConstructor(SimpleDateFormat::class)
        every { anyConstructed<SimpleDateFormat>().format(any()) } returns "test"
        every { anyConstructed<SimpleDateFormat>().parse(any()) } returns mockk(relaxed = true)

        viewModel.formatDate("2017-11-05T11:34:16.447Z")

        verify { anyConstructed<SimpleDateFormat>().parse(any()) }
    }

    @Test
    fun `test formatDate with invalid date`() {
        val dateString = "invalid-date"
        val formattedDate = viewModel.formatDate(dateString)
        assertEquals(formattedDate, UNKNOWN_DATE)
    }

    @Test
    fun `test formatDate with null date`() {
        val formattedDate = viewModel.formatDate(null)
        assertEquals(formattedDate, UNKNOWN_DATE)
    }

    @Test
    fun `test onFilterSelected with status filter`() = runTest {

        val characters = arrayListOf(
            mockCharacter
        )
        coEvery { repository.fetchCharactersContainer("Rick") } returns CharactersContainerResult.Success(
            characters
        )
        val job = launch {
            viewModel.onSearchQueryChange("Rick")

            // When
            viewModel.onFilterSelected(FilterType.STATUS)

            // Then
            assertTrue(viewModel.isGrouped.value)
            assertTrue(viewModel.groupedCharactersList.value.containsKey("Alive"))
        }
        job.cancel()
    }

    @Test
    fun `test onFilterSelected with none filter`() = runTest {
        val characters = arrayListOf(
            mockCharacter
        )
        coEvery { repository.fetchCharactersContainer("Rick") } returns CharactersContainerResult.Success(
            characters
        )
        val job = launch {
            viewModel.onSearchQueryChange("Rick")

            // When
            viewModel.onFilterSelected(FilterType.NONE)

            // Then
            assertFalse(viewModel.isGrouped.value)
            assertTrue(viewModel.groupedCharactersList.value.isEmpty())
        }
        job.cancel()
    }

    @Test
    fun `test clearSearchState`() {
        every { spyk["resetGrouping"]() } returns Unit
        spyk.searchText.value = "Some Query"
        spyk.isShowResultText.value = true
        spyk.clearSearchState()
        assertEquals(spyk.searchText.value, "")
        assertFalse(spyk.isShowResultText.value)
        assertTrue(spyk.charactersList.value.isEmpty())
        verify { spyk["resetGrouping"]() }
    }

    @Test
    fun `test hideAlertDialog`() {
        viewModel.isShowErrorDialog.value = true

        viewModel.hideAlertDialog()

        assertFalse(viewModel.isShowErrorDialog.value)
    }

    @Test
    fun `test showAlertDialog`() {
        viewModel.isShowErrorDialog.value = false

        viewModel.showAlertDialog()

        assertTrue(viewModel.isShowErrorDialog.value)
    }
}
