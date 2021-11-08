package hu.bme.aut.android.socialstockmarketapp.ui.stocklist

import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.ui.stocknewslist.ValueAutoCompleteItem
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.*
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteBox
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteSearchBarTag
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.asAutoCompleteEntities
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
    var stockList by rememberSaveable { mutableStateOf(listOf<StockSymbol>()) }
    val viewState = stockListScreenViewModel.viewState.collectAsState()
    val categoryList by remember {
        mutableStateOf(
            mutableListOf(
                "AS", "AT", "AX", "BA", "BC", "BD", "BE", "BK", "BO", "BR", "CN", "CO", "CR", "DB", "DE", "DU", "F", "HE", "HK", "HM", "IC",
                "IR", "IS", "JK", "JO", "KL", "KQ", "KS", "L", "LN", "LS", "MC", "ME", "MI", "MU", "MX", "NE", "NL", "NS", "NZ", "OL", "PA",
                "PM", "PR", "QA", "RG", "SA", "SG", "SI", "SN", "SR", "SS", "ST", "SW", "SZ", "T", "TA", "TL", "TO", "TW", "US", "V", "VI",
                "VN", "VS", "WA", "HA", "SX", "TG", "SC"
            )
        )
    }
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
        stockListScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockListOneShotEvent.DataListReceived -> {
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
                modifier = Modifier,
                scaffoldState = scaffoldState,
                topBar = { TopBar("Stock List", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
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
                                dropDownList = categoryList, onSpinnerItemSelected = { stockListScreenViewModel.onAction(StockListUiAction.SpinnerSelected(it)) },
                                spinnerTitle = "Exchange Location"
                            )
                        }
                        if(!viewState.value.isLoading){
                        Row() {
                            AutoCompleteBox(
                                items = autoCompleteEntities,
                                itemContent = { item ->
                                    ValueAutoCompleteItem(item.value)
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
                                    label = "Search in Stock Titles",
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
                            Row(modifier = Modifier
                                .padding(bottom = 50.dp, top = 20.dp)
                                .weight(0.8f)) {
                                if (viewState.value.isLoading)
                                    CircularIndeterminateProgressBar(isDisplayed = true)
                                else
                                    MainStockList(stockSymbolList = stockList, onRowItemClick = { stockSymbol -> navController.navigate("stockdetail_screen/$stockSymbol") })
                            }
                    }
                }
            )
}