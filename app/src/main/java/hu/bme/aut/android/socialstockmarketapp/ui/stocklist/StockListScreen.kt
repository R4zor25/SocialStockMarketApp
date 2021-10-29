package hu.bme.aut.android.socialstockmarketapp.ui.stocklist

import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.MainStockList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import io.finnhub.api.models.StockSymbol
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@InternalCoroutinesApi
@Composable
fun StockListScreen(navController: NavController) {
    val stockListScreenViewModel = hiltViewModel<StockListScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    var stockList by remember { mutableStateOf(listOf<StockSymbol>()) }
    val viewState = stockListScreenViewModel.viewState.collectAsState()

    LaunchedEffect("key") {
        stockListScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockListOneShotEvent.DataListReceived -> {
                        stockList = stockListScreenViewModel.stockSymbolList
                    }
                    else -> {
                    }
                }
            }
            .collect()
    }

    SocialStockMarketAppTheme {
        Surface(color = Color.White) {
            Scaffold(
                modifier = Modifier.background(Color.White),
                scaffoldState = scaffoldState,
                topBar = { TopBar("Stock List" , buttonIcon = Icons.Filled.Menu, scope, scaffoldState)},
                drawerBackgroundColor = Color.White,
                drawerContent = {
                                NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
                },
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }, content = { _ ->
                    if(viewState.value.isLoading)
                        CircularIndeterminateProgressBar(isDisplayed = true)
                    else
                        MainStockList(stockSymbolList = stockList, onRowItemClick = {navController.navigate("login_screen")})
                })
        }
    }
}