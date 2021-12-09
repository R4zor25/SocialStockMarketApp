package hu.bme.aut.android.socialstockmarketapp.ui.stocklist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.SpinnerView
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteBox
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteSearchBarTag
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.ValueAutoCompleteString
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.asAutoCompleteEntities
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.MainStockList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.searchbar.TextSearchBar
import io.finnhub.api.models.StockSymbol
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@InternalCoroutinesApi
@Composable
fun StockListScreen(navController: NavController) {
    val stockListScreenViewModel = hiltViewModel<StockListScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    var stockList by remember { mutableStateOf(listOf<StockSymbol>()) }
    val viewState = stockListScreenViewModel.viewState.collectAsState()
    val categoryList by rememberSaveable {
        mutableStateOf(
            mutableListOf(
                "AS", "AT", "AX", "BA", "BC", "BD", "BE", "BK", "BO", "BR", "CN", "CO", "CR", "DB", "DE", "DU", "F", "HE", "HK", "HM", "IC",
                "IR", "IS", "JK", "JO", "KL", "KQ", "KS", "L", "LN", "LS", "MC", "ME", "MI", "MU", "MX", "NE", "NL", "NS", "NZ", "OL", "PA",
                "PM", "PR", "QA", "RG", "SA", "SG", "SI", "SN", "SR", "SS", "ST", "SW", "SZ", "T", "TA", "TL", "TO", "TW", "US", "V", "VI",
                "VN", "VS", "WA", "HA", "SX", "TG", "SC"
            )
        )
    }
    val stockTitles by remember { mutableStateOf(mutableListOf<String>()) }
    var autoCompleteEntities by rememberSaveable {
        mutableStateOf(stockTitles.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        ))
    }
    var editTextValue by rememberSaveable { mutableStateOf("") }

    //This handles crash then app goes background
    //stockTitles && stockSymbolList can be too big, to restore, we collect data from viewmodel and init autocomplete and StockList
    if (stockTitles.isEmpty() && stockList.isEmpty() && stockListScreenViewModel.stockSymbolList.isNotEmpty()) {
        stockTitles.clear()

        stockList = stockListScreenViewModel.stockSymbolList.filter {
            it.description?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
        }
        for (stock in stockListScreenViewModel.stockSymbolList)
            stockTitles.add(stock.description.toString())
        autoCompleteEntities = stockTitles.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        )
    }
    LaunchedEffect("key") {
        stockListScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockListOneShotEvent.StockSymbolListReceived -> {
                        stockList = stockListScreenViewModel.stockSymbolList

                        stockTitles.clear()
                        for (stock in stockList)
                            stockTitles.add(stock.description.toString())

                        autoCompleteEntities = stockTitles.asAutoCompleteEntities(
                            filter = { item, query ->
                                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
                            }
                        )
                    }
                    else -> {
                    }
                }
            }
            .collect()
    }
    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.stock_list), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }, content = { _ ->
            Column(modifier = Modifier.background(Color.White)) {
                Row(modifier = Modifier.weight(0.1f)) {
                    SpinnerView(
                        dropDownList = categoryList,
                        onSpinnerItemSelected = {
                            stockListScreenViewModel.onAction(StockListUiAction.SpinnerSelected(it))
                            editTextValue = ""
                                                },
                        spinnerTitle = stringResource(R.string.extract_location),
                        "US"
                    )
                }
                if (!viewState.value.isLoading) {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
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
                                stockList = stockListScreenViewModel.stockSymbolList.filter {
                                    it.description?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
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
                                    stockList = stockListScreenViewModel.stockSymbolList.filter {
                                        it.description?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
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
                                    stockList = stockListScreenViewModel.stockSymbolList.filter {
                                        it.description?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
                                    }
                                }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(bottom = 50.dp, top = 20.dp)
                        .weight(1f)
                ) {
                    if (viewState.value.isLoading)
                        CircularIndeterminateProgressBar(isDisplayed = true)
                    else
                        MainStockList(stockSymbolList = stockList, onRowItemClick = { stockSymbol -> navController.navigate("stockdetail_screen/$stockSymbol") })
                }
            }
        }
    )
}