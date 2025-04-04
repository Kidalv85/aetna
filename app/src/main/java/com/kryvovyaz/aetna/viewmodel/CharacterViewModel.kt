
package com.kryvovyaz.aetna.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kryvovyaz.aetna.models.CharactersContainerResult
import com.kryvovyaz.aetna.repository.CharacterRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository : CharacterRepositoryImpl) : ViewModel() {

    val state = mutableStateOf("")

    fun onSearchQueryChange(query: String) {
        Log.d("Vlad","viewmodel onSearchQueryChange ")
        viewModelScope.launch {
            val result =  repository.fetchMemeContainer(query)
            when {
                result is CharactersContainerResult.IsLoading -> {
                    Log.d("Vlad","result loading")

                }
                result is CharactersContainerResult.Failure ->{
                    Log.d("Vlad","result failure : ${result.toString()}")
                }
                result is CharactersContainerResult.Success ->{
                    Log.d("Vlad","result success : ${result.response.toString()}")
                    state.value = result.response.toString()
                }

            }
        }

    }

    // Initialize with "rick" search to show data on app launch
//    init {
//        onSearchQueryChange("rick")
//    }
}
