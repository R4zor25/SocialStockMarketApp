package hu.bme.aut.android.socialstockmarketapp.ui.cryptostocklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
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
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.CryptoSymbolList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.searchbar.TextSearchBar
import io.finnhub.api.models.CryptoSymbol
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*


@OptIn(ExperimentalComposeUiApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@InternalCoroutinesApi
@Composable
fun CryptoStockListScreen(navController: NavController) {
    val cryptoStockListScreenViewModel = hiltViewModel<CryptoStockListScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    var cryptoSymbolList by rememberSaveable { mutableStateOf(listOf<CryptoSymbol>()) }
    val viewState = cryptoStockListScreenViewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    val categoryList = stringArrayResource(R.array.crypto_trader_array)
    val cryptoSymbols by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var autoCompleteEntities by remember {
        mutableStateOf(cryptoSymbols.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        ))
    }
    var editTextValue by rememberSaveable { mutableStateOf("") }

    LaunchedEffect("key") {
        cryptoStockListScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    CryptoStockListOneShotEvent.DataListReceived -> {
                        cryptoSymbolList = cryptoStockListScreenViewModel.cryptoSymbolList
                        cryptoSymbols.clear()
                        for (symbol in cryptoSymbolList)
                            cryptoSymbols.add(symbol.displaySymbol.toString())

                        autoCompleteEntities = cryptoSymbols.asAutoCompleteEntities(
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

    Surface(color = Color.White) {
        Scaffold(
            modifier = Modifier.background(Color.White),
            scaffoldState = scaffoldState,
            topBar = { TopBar(stringResource(R.string.crypto_currency), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
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
                            dropDownList = categoryList.toMutableList(), onSpinnerItemSelected = {
                                cryptoStockListScreenViewModel.onAction(CryptoStockListUiAction.SpinnerSelected(it))
                                editTextValue = ""
                                                                                                 },
                            spinnerTitle = stringResource(R.string.exchange),
                            null
                        )
                    }
                    Row() {
                        if (!viewState.value.isLoading) {
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
                                    cryptoSymbolList = cryptoStockListScreenViewModel.cryptoSymbolList.filter {
                                        it.displaySymbol?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
                                    }
                                    view.clearFocus()
                                }

                                TextSearchBar(
                                    modifier = Modifier
                                        .testTag(AutoCompleteSearchBarTag)
                                        .padding(start = 12.dp),
                                    value = editTextValue,
                                    label = stringResource(R.string.search_in_crypto_exchange),
                                    onDoneActionClick = {
                                        view.clearFocus()
                                    },
                                    onClearClick = {
                                        editTextValue = ""
                                        filter(editTextValue)
                                        cryptoSymbolList = cryptoStockListScreenViewModel.cryptoSymbolList.filter {
                                            it.displaySymbol?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
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
                                        cryptoSymbolList = cryptoStockListScreenViewModel.cryptoSymbolList.filter {
                                            it.displaySymbol?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(bottom = 50.dp, top = 20.dp)
                            .weight(0.8f)
                    ) {
                        if (viewState.value.isLoading)
                            CircularIndeterminateProgressBar(isDisplayed = true)
                        else
                            CryptoSymbolList(
                                cryptoSymbolList = cryptoSymbolList, onGridItemClicked = { cryptoSymbol ->
                                    navController.navigate("cryptodetail_screen/$cryptoSymbol")
                                },
                                listState = listState
                            )
                    }
                }
            })
    }
}