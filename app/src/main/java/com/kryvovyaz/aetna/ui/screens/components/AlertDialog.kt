package com.kryvovyaz.aetna.ui.screens.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.kryvovyaz.aetna.R
import com.kryvovyaz.aetna.ui.theme.AetnaTheme

@Composable
fun ErrorAlertDialog(
    isShowErrorDialog: MutableState<Boolean>,
    title: String = stringResource(R.string.app_error_title),
    bodyText: String = stringResource(R.string.app_something_went_wrong),
    onOkButtonClick: () -> Unit,
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textButton: String = stringResource(id = R.string.app_ok),
    textButtonColor: Color = MaterialTheme.colorScheme.primary,
    maxLines: Int = 3
) {
    if (isShowErrorDialog.value) {
        AlertDialog(
            modifier = Modifier,
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    modifier = Modifier,
                    onClick = { onOkButtonClick.invoke() })
                {
                    Text(
                        text = textButton,
                        color = textButtonColor
                    )
                }
            },
            title = {
                Text(
                    text = title,
                    style = titleTextStyle
                )
            },
            text = {
                Text(
                    text = bodyText,
                    style = bodyTextStyle,
                    maxLines = maxLines
                )
            }
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun ErrorAlertDialogPreview(){
    AetnaTheme {
        ErrorAlertDialog(
            isShowErrorDialog = remember {
                mutableStateOf(true)
            },
            onOkButtonClick = {}
        )
    }
}