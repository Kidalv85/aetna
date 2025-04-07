package com.kryvovyaz.aetna.ui.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.kryvovyaz.aetna.R
import com.kryvovyaz.aetna.ui.theme.defaultIconSize
import com.kryvovyaz.aetna.ui.theme.strokeWidthSmall
import com.kryvovyaz.aetna.util.DELETE_ICON_TAG
import com.kryvovyaz.aetna.util.FILTER_ICON_TAG

enum class FilterType {
    STATUS,
    SPECIES,
    TYPE,
    NONE
}

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    text: String,
    readOnly: Boolean,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onFilterSelected: (FilterType) -> Unit
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val filterOptions = listOf(
        stringResource(id = R.string.filter_status),
        stringResource(id = R.string.filter_species),
        stringResource(id = R.string.filter_type),
        stringResource(id = R.string.filter_none),
    )
    val context = LocalContext.current

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = strokeWidthSmall,
                    color = Color.Black,
                    shape = MaterialTheme.shapes.medium
                )
                .semantics {
                    contentDescription =
                        context.getString(R.string.app_search_criteria_description)
                },
            value = text,
            onValueChange = onValueChange,
            readOnly = readOnly,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = stringResource(R.string.app_dropdown_filter_icon_description),
                    modifier = Modifier
                        .size(defaultIconSize)
                        .clickable {
                            setExpanded(!expanded)
                        }
                        .testTag(FILTER_ICON_TAG),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.app_clear_search_icon_description),
                        modifier = Modifier
                            .size(defaultIconSize)
                            .clickable { onClear() }
                            .testTag(DELETE_ICON_TAG),
                        tint = Color.Red
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.app_search_text),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.semantics {
                        contentDescription =
                            "${context.getString(R.string.app_search_criteria_description)} $text"
                    }
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) }
        ) {
            filterOptions.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        val filterType = when (index) {
                            0 -> FilterType.STATUS
                            1 -> FilterType.SPECIES
                            2 -> FilterType.TYPE
                            else -> FilterType.NONE
                        }
                        onFilterSelected(filterType)
                        setExpanded(false)
                    },
                    modifier = Modifier.semantics {
                        contentDescription =
                            "${context.getString(R.string.app_filter_criteria_description)} $option"

                    }
                )
            }
        }
    }
}