package com.kryvovyaz.aetna.util

import android.content.Context
import android.content.Intent
import com.kryvovyaz.aetna.R
import com.kryvovyaz.aetna.models.Character

object ShareHelper {
    fun shareCharacterDetails(
        context: Context, character: Character
    ) {
        val shareMessage = """
            ${context.getString(R.string.app_created)} ${character.created ?: context.getString(R.string.app_unknown_name)}
            ${context.getString(R.string.app_species)} ${character.species ?: context.getString(R.string.app_unknown_species)}
            ${context.getString(R.string.app_status)} ${character.status ?: context.getString(R.string.app_unknown_status)}
            ${context.getString(R.string.app_origin)} ${character.origin?.name ?: context.getString(R.string.app_unknown_origin)}
            ${context.getString(R.string.app_type)} ${character.type ?: context.getString(R.string.app_unknown_type)}
            ${context.getString(R.string.app_image)} ${character.image}
            """.trimIndent()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = FORMAT_TEXT_PLAIN
        }

        val chooserIntent = Intent.createChooser(intent, SHARE_VIA)
        context.startActivity(chooserIntent)
    }

}