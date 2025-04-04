package com.kryvovyaz.aetna.models

sealed class CharactersContainerResult {
    class Success(val response: ArrayList<Results>) : CharactersContainerResult() {}
    class Failure() : CharactersContainerResult() {}
    object IsLoading : CharactersContainerResult() {}
}