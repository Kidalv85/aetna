package com.kryvovyaz.aetna.models

sealed class CharactersContainerResult {
    class Success(val response: CharactersResponse) : CharactersContainerResult() {}
    class Failure(val error: Error) : CharactersContainerResult() {}
    object IsLoading : CharactersContainerResult() {}
}