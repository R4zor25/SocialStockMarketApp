package hu.bme.aut.android.socialstockmarketapp.ui.followedstocks

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteBox
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteSearchBarTag
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.ValueAutoCompleteString
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.asAutoCompleteEntities
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.FollowedStocksList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.searchbar.TextSearchBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FollowedStocksScreen(navController: NavController, userName: String){
    val followedStocksScreenViewModel = hiltViewModel<FollowedStocksScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    var stockList by rememberSaveable { mutableStateOf(listOf<String>()) }
    val viewState = followedStocksScreenViewModel.viewState.collectAsState()
    val stockTitles by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var autoCompleteEntities by rememberSaveable {
        mutableStateOf(stockTitles.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        ))
    }
    var editTextValue by rememberSaveable { mutableStateOf("") }


    LaunchedEffect("key") {
        followedStocksScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                   is  FollowedStocksOneShotEvent.FollowedStocksReceived -> {
                        stockList = it.stockSymbolList
                        stockTitles.clear()
                        for (stock in stockList)
                            stockTitles.add(stock.toString())

                        autoCompleteEntities = stockTitles.asAutoCompleteEntities(
                            filter = { item, query ->
                                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
                            }
                        )
                    }
                    is FollowedStocksOneShotEvent.AcquireUserName -> {
                        followedStocksScreenViewModel.userName = userName
                        followedStocksScreenViewModel.onAction(FollowedStocksUiAction.OnInit)
                    }
                    else -> { }
                }
            }
            .collect()
    }
    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.followed_stock_list), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        }, content = { _ ->
            Column(modifier = Modifier.background(Color.White)) {
                if(!viewState.value.isLoading){
                    Row() {
                        AutoCompleteBox(
                            items = autoCompleteEntities,
                            itemContent = { item ->
                                ValueAutoCompleteString(item.value)
                            }
                        ) {
                            val view = LocalView.current

                            onItemSelected { item ->
                                editTextValue = item.value
                                filter(editTextValue)
                                stockList = followedStocksScreenViewModel.stockSymbolList.filter {
                                    it.lowercase(Locale.getDefault()).contains(editTextValue.lowercase())
                                }
                                view.clearFocus()
                            }

                            TextSearchBar(
                                modifier = Modifier
                                    .testTag(AutoCompleteSearchBarTag)
                                    .padding(start = 12.dp),
                                value = editTextValue,
                                label = stringResource(R.string.search_in_stock_title),
                                onDoneActionClick = {
                                    view.clearFocus()
                                },
                                onClearClick = {
                                    editTextValue = ""
                                    filter(editTextValue)
                                    stockList = followedStocksScreenViewModel.stockSymbolList.filter {
                                        it.lowercase(Locale.getDefault()).contains(editTextValue.lowercase()) ?: false
                                    }
                                    view.clearFocus()
                                },
                                onFocusChanged = { focusState ->
                                    isSearching = focusState.isFocused
                                    filter(editTextValue)
                                },
                                onValueChanged = { query ->
                                    editTextValue = query
                                    filter(editTextValue)
                                    stockList = followedStocksScreenViewModel.stockSymbolList.filter {
                                        it.lowercase(Locale.getDefault()).contains(editTextValue.lowercase()) ?: false
                                    }
                                }
                            )
                        }
                    }
                }
                Row(modifier = Modifier
                    .padding(bottom = 50.dp, top = 20.dp)
                    .weight(0.8f)) {
                    if (viewState.value.isLoading)
                        CircularIndeterminateProgressBar(isDisplayed = true)
                    else
                        FollowedStocksList(stockSymbols = stockList) {stockSymbol -> navController.navigate("stockdetail_screen/$stockSymbol") }
                }
            }
        }
    )
}