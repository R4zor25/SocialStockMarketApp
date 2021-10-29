package hu.bme.aut.android.socialstockmarketapp.ui.mystocklist

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.ui.auth.registration.BgCard
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@OptIn(ExperimentalComposeUiApi::class)
@InternalCoroutinesApi
@Composable
    fun MyStockListScreen(navController: NavController) {
    val myStockListScreenViewModel = hiltViewModel<MyStockListScreenViewModel>()
    SocialStockMarketAppTheme {
            Box {
                //BgCard()
                MainCardMyStockList(navController, myStockListScreenViewModel)
            }
        }
    }

@ExperimentalComposeUiApi
@InternalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun MyStockListScreenPreview() {
    Box {
        BgCard()
        MainCardMyStockList(navController = NavController(LocalContext.current),
            viewModel = hiltViewModel<MyStockListScreenViewModel>() )
    }
}

@Composable
fun BgCard() {
    Surface(color = Color.Green, modifier = Modifier.fillMaxSize()) {
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
fun MainCardMyStockList(navController: NavController, viewModel: MyStockListScreenViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    MyStockListOneShotEvent.ShowToastMessage -> Toast.makeText(context, viewState.value.errorText, Toast.LENGTH_LONG).show()
                    else -> {
                    }
                }
            }
            .collect()
    }

    SocialStockMarketAppTheme {
        Surface(color = Color.Red) {
            // Scaffold Component
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { TopBar("My Stock List", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
                drawerBackgroundColor = Color.White,
                drawerContent = {
                    NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
                },
                // Bottom navigation
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }, content = { padding ->
                    Text(text = "MyStockList")
                    // Navhost: where screens are placed
                })

        }
    }
}