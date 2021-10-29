package hu.bme.aut.android.socialstockmarketapp.ui.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.auth.registration.BgCard
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CustomTextField
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.gradient
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@OptIn(ExperimentalComposeUiApi::class)
@InternalCoroutinesApi
@Composable
fun LoginScreen(navController: NavController) {
    val loginScreenViewModel = hiltViewModel<LoginScreenViewModel>()
    SocialStockMarketAppTheme {
        Box {
            BgCard()
            MainCardLogin(navController, loginScreenViewModel)
        }
    }
}

@ExperimentalComposeUiApi
@InternalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Box {
        BgCard()
        MainCardLogin(
            navController = NavController(LocalContext.current),
            viewModel = hiltViewModel<LoginScreenViewModel>()
        )
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

@ExperimentalComposeUiApi
@InternalCoroutinesApi
@Composable
fun MainCardLogin(navController: NavController, viewModel: LoginScreenViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    LoginOneShotEvent.NavigateToStockList -> navController.navigate("stocklist_screen"){
                        navController.popBackStack()
                    }
                    LoginOneShotEvent.ShowToastMessage -> Toast.makeText(context, viewState.value.errorText, Toast.LENGTH_LONG).show()
                    else -> {
                    }
                }
            }
            .collect()
    }


    Surface(
        color = Color.White, modifier = Modifier.requiredHeight(700.dp),
        shape = RoundedCornerShape(60.dp).copy(ZeroCornerSize, ZeroCornerSize)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_logo), contentDescription = "",
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            if (!viewState.value.isLoading) {
                ClickableText(
                    text = AnnotatedString("Sign In"), onClick = {
                        email = "demetermate@gmail.com"
                        passwd = "testtest"
                        viewModel.onAction(LoginUiAction.OnLogin(email, passwd))
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                            style = TextStyle (
                            color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight(700),
                    textDecoration = TextDecoration.Underline
                ))
            } else
                CircularIndeterminateProgressBar(isDisplayed = viewState.value.isLoading)
            Spacer(modifier = Modifier.padding(16.dp))

            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = { Icon(Icons.Filled.Email, "contentDescription") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                onTextChange = { email = it },
                text = email,
                hint = "Email",
                passwordVisibility = true,
                getPasswordVisibility = { true }
            )

            Spacer(modifier = Modifier.padding(12.dp))

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
                getPasswordVisibility = { passwordVisibility }
            )
            ClickableText(
                text = AnnotatedString("Registration"), onClick = {
                    navController.navigate("registration_screen")
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp, 4.dp),
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            Button(
                onClick = {
                    viewModel.onAction(LoginUiAction.OnLogin(email, passwd))
                },
                shape = CircleShape,
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MyBlue
                ),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = "Login", fontSize = 24.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}
//TODO Leading Icon, Password field, Loading ProgressBar, StockListScreen,
//TODO Registration_screen button, maybe fast login with LongClick