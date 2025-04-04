package com.kryvovyaz.aetna.api

import com.kryvovyaz.aetna.models.CharactersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterService {
    @GET("character/")
    suspend fun getCharacters(
        @Query("name") name: String
    ): Call<CharactersResponse>
}