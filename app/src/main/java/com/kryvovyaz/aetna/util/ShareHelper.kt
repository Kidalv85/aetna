package com.kryvovyaz.aetna.util

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.MutableState
import com.kryvovyaz.aetna.R
import com.kryvovyaz.aetna.models.Character

object ShareHelper {
    fun shareCharacterDetails(
        context: Context,
        character: Character?,
        isShowErrorDialog: MutableState<Boolean>
    ) {
        if (character == null) {
            isShowErrorDialog.value = true
            return
        }
        val shareMessage = """
        ${context.getString(R.string.app_nane_text)} ${character.name ?: context.getString(R.string.app_unknown_name)}
        ${context.getString(R.string.app_species)} ${character.species ?: context.getString(R.string.app_unknown_species)}
        ${context.getString(R.string.app_status)} ${character.status ?: context.getString(R.string.app_unknown_status)}
        ${context.getString(R.string.app_origin)} ${
            character.origin?.name ?: context.getString(
                R.string.app_unknown_origin
            )
        }
        ${context.getString(R.string.app_type)} ${
            character.type.takeIf { !it.isNullOrBlank() } ?: context.getString(
                R.string.app_unknown_type
            )
        }
        ${context.getString(R.string.app_image)} ${
            character.image ?: context.getString(
                R.string.app_unknown_image
            )
        }
        """.trimIndent()

        // Create the intent to share the message
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = FORMAT_TEXT_PLAIN
        }

        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        if (activities.isEmpty()) {
            isShowErrorDialog.value = true
            return
        }

        val chooserIntent = Intent.createChooser(intent, SHARE_VIA)
        context.startActivity(chooserIntent)
    }

}