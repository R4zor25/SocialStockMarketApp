package hu.bme.aut.android.socialstockmarketapp.ui.cryptostocklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.SpinnerView
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.CryptoSymbolList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import io.finnhub.api.models.CryptoSymbol
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@OptIn(ExperimentalComposeUiApi::class)
@InternalCoroutinesApi
@Composable
fun CryptoStockListScreen(navController: NavController) {
    val cryptoStockListScreenViewModel = hiltViewModel<CryptoStockListScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    var cryptoSymbolList by remember { mutableStateOf(listOf<CryptoSymbol>()) }
    val viewState = cryptoStockListScreenViewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    val categoryList by remember {
        mutableStateOf(
            mutableListOf(
                "KRAKEN", "HITBTC", "COINBASE", "GEMINI", "POLONIEX", "Binance", "ZB", "BITTREX",
                "KUCOIN", "OKEX", "BITFINEX", "HUOBI"
            )
        )
    }

    LaunchedEffect("key") {
        cryptoStockListScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    CryptoStockListOneShotEvent.DataListReceived -> {
                        cryptoSymbolList = cryptoStockListScreenViewModel.cryptoSymbolList
                    }
                    else -> { }
                }
            }
            .collect()
    }

    SocialStockMarketAppTheme {
        Surface(color = Color.White) {
            Scaffold(
                modifier = Modifier.background(Color.White),
                scaffoldState = scaffoldState,
                topBar = { TopBar("Crypto Currency", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
                drawerBackgroundColor = Color.White,
                drawerContent = {
                    NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
                },
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }, content = { _ ->

                    ConstraintLayout {
                        val (spinner, newsList) = createRefs()

                        Row(modifier = Modifier.constrainAs(spinner) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.value(80.dp)
                        }) {
                            SpinnerView(dropDownList =  categoryList, onSpinnerItemSelected =  { cryptoStockListScreenViewModel.onAction(CryptoStockListUiAction.SpinnerSelected(it)) },
                            spinnerTitle = "Exchange")
                        }
                        if (viewState.value.isLoading)
                            CircularIndeterminateProgressBar(isDisplayed = true)
                        else {
                            Row(modifier = Modifier.constrainAs(newsList) {
                                top.linkTo(spinner.bottom)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }) {

                                CryptoSymbolList(cryptoSymbolList = cryptoSymbolList, onGridItemClicked = { cryptoSymbol ->
                                    navController.navigate("cryptodetail_screen/$cryptoSymbol")},
                                    listState = listState)
                                /*
                                StockNewsList(stockNewsList = stockNewsList, listState = listState,
                                    onRowItemClick = { newsUrl ->
                                        val url = URLEncoder.encode(newsUrl, StandardCharsets.UTF_8.toString())
                                        navController.navigate("stocknewsdetail_screen/$url") {
                                            popUpTo("login_screen") {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    })
                                    88
                                 */
                            }
                        }
                    }
                })
        }
    }
}