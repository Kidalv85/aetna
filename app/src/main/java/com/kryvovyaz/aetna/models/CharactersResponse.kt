package com.kryvovyaz.aetna.models

import com.google.gson.annotations.SerializedName


data class CharactersResponse(

  @SerializedName("info") var info: Info? = Info(),
  @SerializedName("results") var results: ArrayList<Results> = arrayListOf()

)