package hu.bme.aut.android.socialstockmarketapp.ui.stocknewslist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.SpinnerView
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteBox
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteSearchBarTag
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.asAutoCompleteEntities
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.StockNewsList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.searchbar.TextSearchBar
import io.finnhub.api.models.MarketNews
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@ExperimentalAnimationApi
@Composable
fun StockNewsListScreen(navController: NavController) {
    val stockNewsListScreenViewModel = hiltViewModel<StockNewsListScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    var stockNewsList by remember { mutableStateOf(listOf<MarketNews>()) }
    val viewState = stockNewsListScreenViewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    val categoryList by remember { mutableStateOf(mutableListOf("General", "Forex", "Crypto", "Merger")) }
    val items by remember { mutableStateOf(mutableListOf<String>()) }


    var autoCompleteEntities by remember  { mutableStateOf(items.asAutoCompleteEntities(
        filter = { item, query ->
            item.lowercase(Locale.getDefault())?.contains(query.lowercase(Locale.getDefault()))!!
        }
    ))}

    LaunchedEffect("key") {
        stockNewsListScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockNewsListOneShotEvent.DataListReceived -> {
                        stockNewsList = stockNewsListScreenViewModel.stockNewsList

                        for(valami in stockNewsList)
                            items.add(valami.headline.toString())

                        autoCompleteEntities = items.asAutoCompleteEntities(
                            filter = { item, query ->
                                item?.lowercase(Locale.getDefault())?.contains(query.lowercase(Locale.getDefault()))!!
                            }
                        )
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
                topBar = { TopBar("Stock Market News", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
                drawerBackgroundColor = Color.White,
                drawerContent = {
                    NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
                },
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }, content = { _ ->

                    Column() {
                        Row(modifier = Modifier.weight(0.065f)) {
                            SpinnerView(
                                dropDownList = categoryList, onSpinnerItemSelected = { stockNewsListScreenViewModel.onAction(StockNewsListUiAction.SpinnerSelected(it)) },
                                spinnerTitle = "News Category"
                            )
                        }
                        Row() {
                            if (!autoCompleteEntities.isEmpty()) {
                                AutoCompleteBox(
                                    items = autoCompleteEntities,
                                    itemContent = { item ->
                                        ValueAutoCompleteItem(item.value)
                                    }
                                ) {
                                    var value by remember { mutableStateOf("") }
                                    val view = LocalView.current

                                    onItemSelected { item ->
                                        value = item.value
                                        filter(value)
                                        stockNewsList = stockNewsListScreenViewModel.stockNewsList.filter {
                                            it.headline?.lowercase(Locale.getDefault())?.contains(value.lowercase()) ?: false
                                        }
                                        view.clearFocus()
                                    }

                                    TextSearchBar(
                                        modifier = Modifier
                                            .testTag(AutoCompleteSearchBarTag)
                                            .padding(start = 12.dp),
                                        value = value,
                                        label = "Search in Headline",
                                        onDoneActionClick = {
                                            view.clearFocus()
                                        },
                                        onClearClick = {
                                            value = ""
                                            filter(value)

                                            view.clearFocus()
                                        },
                                        onFocusChanged = { focusState ->
                                            isSearching = focusState.isFocused
                                        },
                                        onValueChanged = { query ->
                                            value = query
                                            filter(value)
                                            stockNewsList = stockNewsListScreenViewModel.stockNewsList.filter {
                                                it.headline?.lowercase(Locale.getDefault())?.contains(value.lowercase()) ?: false
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        if (viewState.value.isLoading)
                            CircularIndeterminateProgressBar(isDisplayed = true)
                        else {
                            Row(modifier = Modifier.padding(bottom = 50.dp, top = 20.dp).weight(0.8f)) {
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
                            }
                        }
                    }
                })
        }
    }
}

@Composable
fun ValueAutoCompleteItem(item: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = item, style = MaterialTheme.typography.subtitle2)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun Preview(){
    StockNewsListScreen(navController = NavController(LocalContext.current))
}