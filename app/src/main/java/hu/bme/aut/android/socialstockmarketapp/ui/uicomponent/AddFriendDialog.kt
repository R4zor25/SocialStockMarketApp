package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@ExperimentalComposeUiApi
@Composable
fun AddFriendDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (String) -> Unit
) {
    var userName by rememberSaveable { mutableStateOf("") }


    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color.White
        ) {

            Column(modifier = Modifier.padding(8.dp)) {

                Text(
                    text = "Add Friend",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Color Selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {


                    Column {
                        CustomTextField(modifier = Modifier
                            .background(
                                brush = gradient,
                                shape = RoundedCornerShape(40.dp)),
                            text = userName, onTextChange = { userName = it }, hint = "Username", getPasswordVisibility = { true }, passwordVisibility = true)
                    }
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onNegativeClick) {
                        Text(text = "CANCEL")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {
                        onPositiveClick(userName)
                    }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun AddFriendDialogPreview() {
    AddFriendDialog(onDismiss = { }, onNegativeClick = { }, onPositiveClick = {})
}
