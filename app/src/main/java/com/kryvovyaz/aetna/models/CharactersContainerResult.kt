package com.kryvovyaz.aetna.models

sealed class CharactersContainerResult {
    class Success(val response: ArrayList<Character>) : CharactersContainerResult() {}
    data object Failure : CharactersContainerResult() {}
}