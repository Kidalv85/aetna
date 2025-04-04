package com.kryvovyaz.aetna.repository

import com.kryvovyaz.aetna.api.CharacterService
import com.kryvovyaz.aetna.models.CharactersContainerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val characterService: CharacterService) :
    CharacterRepository {

    override suspend fun fetchMemeContainer(name: String): CharactersContainerResult =
        withContext(Dispatchers.IO) {
            try {
                val rootResponse = characterService.getCharacters(name)
                return@withContext if (rootResponse.results.isNotEmpty()) {
                    CharactersContainerResult.Success(rootResponse.results)
                } else {
                    CharactersContainerResult.Failure()
                }
            } catch (ex: Exception) {
                return@withContext CharactersContainerResult
                    .Failure()
            }
        }
}