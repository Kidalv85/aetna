package com.kryvovyaz.aetna.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import com.kryvovyaz.aetna.ui.theme.AetnaTheme
import com.kryvovyaz.aetna.ui.theme.defaultPadding
import com.kryvovyaz.aetna.ui.theme.padding_64
import com.kryvovyaz.aetna.ui.theme.padding_80
import com.kryvovyaz.aetna.ui.theme.smallPadding

@Composable
fun CharacterDetailsScreen(
    formatDate: (String?) -> String,
    onShareCharacterDetails: (Character) -> Unit,
    index: Int,
    charactersList: State<List<Character>>
) {
    val context = LocalContext.current
    AetnaTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .semantics { context.getString(R.string.app_details_screen_description) },
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = padding_80, top = padding_64)
                ) {
                    Text(
                        text = charactersList.value[index].name
                            ?: stringResource(R.string.app_unknown_name),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(defaultPadding)
                            .semantics {
                                contentDescription =
                                    "${context.getString(R.string.app_character_name_description)} ${charactersList.value[index].name}"

                            }
                    )

                    AsyncImage(
                        model = charactersList.value[index].image,
                        contentDescription = "${stringResource(R.string.app_character_image_description)} ${charactersList.value[index].name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentScale = ContentScale.FillWidth
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(defaultPadding)
                    ) {
                        val speciesDetails = "${stringResource(R.string.app_species)} ${
                            charactersList.value[index].species
                                ?: stringResource(R.string.app_unknown_species)
                        }"

                        DetailsText(
                            text = speciesDetails
                        )

                        val statusDetails = "${stringResource(R.string.app_status)} ${
                            charactersList.value[index].status
                                ?: stringResource(R.string.app_unknown_status)
                        }"

                        DetailsText(
                            text = statusDetails
                        )

                        val originDetails = "${stringResource(R.string.app_origin)} ${
                            charactersList.value[index].origin?.name
                                ?: stringResource(R.string.app_unknown_origin)
                        }"

                        DetailsText(
                            text = originDetails
                        )

                        if (!charactersList.value[index].type.isNullOrEmpty()) {
                            val typeDetails =
                                "${stringResource(R.string.app_type)} ${charactersList.value[index].origin}"
                            DetailsText(
                                text = typeDetails
                            )
                        }
                        val createdDetails = "${stringResource(R.string.app_created)} ${
                            formatDate(
                                charactersList.value[index].created
                            )
                        }"
                        DetailsText(
                            text = createdDetails
                        )
                    }
                }

                FloatingActionButton(
                    onClick = {
                        onShareCharacterDetails(charactersList.value[index])
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(defaultPadding)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(R.string.app_share_icon_description),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailsText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(bottom = smallPadding)
            .semantics {
                this.contentDescription = text
            }
    )
}