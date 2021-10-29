package hu.bme.aut.android.socialstockmarketapp.ui.auth.registration

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CustomTextField
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.gradient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun RegistrationScreen(navController: NavController) {
    val registrationScreenViewModel = hiltViewModel<RegistrationScreenViewModel>()
    SocialStockMarketAppTheme {
        Box {
            BgCard()
            MainCardRegistration(navController, registrationScreenViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    Box {
        BgCard()
        MainCardRegistration(navController = NavController(LocalContext.current), viewModel = hiltViewModel<RegistrationScreenViewModel>())
    }

}

@Composable
fun BgCard() {
    Surface(color = MyBlue, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-30).dp)
        ) {
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainCardRegistration(navController: NavController, viewModel: RegistrationScreenViewModel) {

    var email by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    var userName by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    RegistrationOneShotEvent.NavigateToStockList -> navController.navigate("stocklist_screen"){
                        navController.popBackStack()
                    }
                    RegistrationOneShotEvent.ShowToastMessage -> Toast.makeText(context, viewState.value.errorText, Toast.LENGTH_LONG).show()
                    else -> {}
                }
            }
            .collect()
    }
    Surface(
        color = Color.White, modifier = Modifier.requiredHeight(720.dp),
        shape = RoundedCornerShape(60.dp).copy(ZeroCornerSize, ZeroCornerSize)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_logo), contentDescription = "",
                modifier = Modifier
                    .height(280.dp)
                    .width(280.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "Sign Up", fontWeight = FontWeight(700), fontSize = 32.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(16.dp))
            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = { Icon(Icons.Filled.Person, "contentDescription") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                onTextChange = { userName = it },
                text = userName,
                hint = "Username",
                passwordVisibility = true,
                getPasswordVisibility = {true}
            )

            Spacer(modifier = Modifier.padding(8.dp))

            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = { Icon(Icons.Filled.Email, "contentDescription") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                onTextChange = { email = it },
                text = email,
                hint = "Email",
                passwordVisibility = true,
                getPasswordVisibility = {true}
            )
            Spacer(modifier = Modifier.padding(8.dp))
            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = {
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(image, "contentDescription")
                }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                onTextChange = { passwd = it },
                text = passwd,
                hint = "Password",
                passwordVisibility = false,
                getPasswordVisibility = {passwordVisibility}
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            Button(
                onClick = { viewModel.onAction(RegistrationUiAction.OnRegistration(email, userName, passwd)) }, shape = CircleShape,
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MyBlue
                ),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = "Register", fontSize = 24.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}