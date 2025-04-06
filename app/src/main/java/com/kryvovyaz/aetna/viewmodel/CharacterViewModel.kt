package com.kryvovyaz.aetna.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kryvovyaz.aetna.models.CharactersContainerResult
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.repository.CharacterRepositoryImpl
import com.kryvovyaz.aetna.ui.screens.components.FilterType
import com.kryvovyaz.aetna.util.DATE_FORMAT
import com.kryvovyaz.aetna.util.DATE_FORMAT_FROM_API
import com.kryvovyaz.aetna.util.EMPTY
import com.kryvovyaz.aetna.util.NO_TYPE
import com.kryvovyaz.aetna.util.UNKNOWN
import com.kryvovyaz.aetna.util.UNKNOWN_DATE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: CharacterRepositoryImpl) :
    ViewModel() {
    private val _charactersList: SnapshotStateList<Character> = mutableStateListOf()
    val charactersList: State<List<Character>> = derivedStateOf {
        _charactersList
    }
    val searchText = mutableStateOf(EMPTY)
    val isShowLoadingIndicator = mutableStateOf(false)
    val isShowResultText = mutableStateOf(false)
    private val selectedFilter = mutableStateOf(FilterType.NONE)
    private val _groupedCharactersList = mutableStateOf<Map<String, List<Character>>>(emptyMap())
    val groupedCharactersList: State<Map<String, List<Character>>> = _groupedCharactersList

    private val _isGrouped = mutableStateOf(false)
    val isGrouped: State<Boolean> = _isGrouped

    fun onSearchQueryChange(query: String) {
        searchText.value = query
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isEmpty()) {
                isShowResultText.value = false
                clearSearchState()
            } else {
                isShowLoadingIndicator.value = true
                when (val result = repository.fetchMemeContainer(query.trim())) {
                    is CharactersContainerResult.Failure -> {
                        isShowResultText.value = true
                        isShowLoadingIndicator.value = false
                    }

                    is CharactersContainerResult.Success -> {
                        isShowResultText.value = true
                        isShowLoadingIndicator.value = false
                        _charactersList.clear()
                        _charactersList.addAll(result.response)
                        if (selectedFilter.value != FilterType.NONE) {
                            onFilterSelected(selectedFilter.value)
                        } else {
                            resetGrouping()
                        }
                    }
                }
            }
        }
    }

    fun formatDate(dateString: String?): String {
        return try {
            val sdf = SimpleDateFormat(DATE_FORMAT_FROM_API, Locale.getDefault())
            val date = sdf.parse(dateString ?: EMPTY)
            val formattedDate = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            formattedDate.format(date ?: Date())
        } catch (e: Exception) {
            UNKNOWN_DATE
        }
    }

    fun clearSearchState() {
        searchText.value = EMPTY
        isShowResultText.value = false
        _charactersList.clear()
        resetGrouping()
    }

    fun onFilterSelected(filterType: FilterType) {
        viewModelScope.launch {
            _isGrouped.value = true
            selectedFilter.value = filterType
            when (filterType) {
                FilterType.STATUS -> {
                    val groupedCharacters = _charactersList.groupBy { it.status ?: UNKNOWN  }
                    updateGroupedCharacters(groupedCharacters)
                }

                FilterType.SPECIES -> {
                    val groupedCharacters = _charactersList.groupBy { it.species ?: UNKNOWN  }
                    updateGroupedCharacters(groupedCharacters)
                }

                FilterType.TYPE -> {
                    val groupedCharacters = _charactersList.groupBy {
                        if (it.type.isNullOrEmpty()) NO_TYPE else it.type!!
                    }
                    updateGroupedCharacters(groupedCharacters)
                }

                FilterType.NONE -> {
                    resetGrouping()
                }
            }
        }
    }

    private fun updateGroupedCharacters(groupedCharacters: Map<String, List<Character>>) {
        _groupedCharactersList.value = groupedCharacters
        _isGrouped.value = true
    }

    private fun resetGrouping() {
        _groupedCharactersList.value = emptyMap()
        _isGrouped.value = false
    }
}