package com.kryvovyaz.aetna.repository

import com.kryvovyaz.aetna.api.CharacterService
import com.kryvovyaz.aetna.models.CharactersContainerResult
import com.vladkryvovyaz.mvvm_hilt_test.util.RESPONSE_PARSING_ERROR_MESSAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val characterService: CharacterService) :
    CharacterRepository {

    override suspend fun fetchMemeContainer(name : String): CharactersContainerResult =
        withContext(Dispatchers.IO) {
            val memeCall = characterService.getCharacters(name)

            try {
                val response = memeCall.execute()
                val forecastContainer = response.body()
                forecastContainer?.let {
                    return@withContext CharactersContainerResult.Success(it)
                } ?: run {
                    return@withContext CharactersContainerResult.Failure(
                        Error(RESPONSE_PARSING_ERROR_MESSAGE)
                    )
                }

            } catch (ex: Exception) {
                return@withContext CharactersContainerResult
                    .Failure(java.lang.Error(ex.message))
            }
        }
}