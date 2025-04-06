package com.kryvovyaz.aetna.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImage
import com.kryvovyaz.aetna.R
import com.kryvovyaz.aetna.models.Character
import com.kryvovyaz.aetna.ui.theme.defaultElevation
import com.kryvovyaz.aetna.ui.theme.defaultPadding
import com.kryvovyaz.aetna.ui.theme.height_76

@Composable
fun CharacterCard(
    character: Character,
    onCardClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation),
        border = null,
        onClick = { onCardClick.invoke() }
    ) {
        val characterName = character.name ?: stringResource(R.string.app_unknown)
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height_76),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                AsyncImage(
                    model = character.image,
                    contentDescription = "${stringResource(R.string.app_character_image_description)} $characterName",
                    contentScale = ContentScale.Crop
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(defaultPadding)
                        .semantics {
                            contentDescription =
                                "${context.getString(R.string.app_character_name_description)} $characterName"
                        },
                    text = characterName,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}