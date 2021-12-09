package hu.bme.aut.android.socialstockmarketapp.ui.stocknewslist

import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
    var stockNewsList by rememberSaveable { mutableStateOf(listOf<MarketNews>()) }
    val viewState = stockNewsListScreenViewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    val categoryList = stringArrayResource(R.array.news_type_array)
    val newsHeadlines by remember { mutableStateOf(mutableListOf<String>()) }
    var editTextValue by rememberSaveable { mutableStateOf("") }


    var autoCompleteEntities by rememberSaveable {
        mutableStateOf(newsHeadlines.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        ))
    }

    LaunchedEffect("key") {
        stockNewsListScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockNewsListOneShotEvent.DataListReceived -> {
                        stockNewsList = stockNewsListScreenViewModel.stockNewsList

                        newsHeadlines.clear()
                        for (marketNews in stockNewsList)
                            newsHeadlines.add(marketNews.headline.toString())
                        autoCompleteEntities = newsHeadlines.asAutoCompleteEntities(
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
            topBar = { TopBar(stringResource(R.string.stock_market_news), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
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
                                editTextValue = ""
                                stockNewsListScreenViewModel.onAction(StockNewsListUiAction.SpinnerSelected(it))
                            },
                            spinnerTitle = stringResource(R.string.news_category),
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
                                    stockNewsList = stockNewsListScreenViewModel.stockNewsList.filter {
                                        it.headline?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
                                    }
                                    view.clearFocus()
                                }

                                TextSearchBar(
                                    modifier = Modifier
                                        .testTag(AutoCompleteSearchBarTag)
                                        .padding(start = 12.dp),
                                    value = editTextValue,
                                    label = stringResource(R.string.search_in_headline),
                                    onDoneActionClick = {
                                        view.clearFocus()
                                    },
                                    onClearClick = {
                                        editTextValue = ""
                                        stockNewsList = stockNewsListScreenViewModel.stockNewsList
                                        filter(editTextValue)

                                        view.clearFocus()
                                    },
                                    onFocusChanged = { focusState ->
                                        isSearching = focusState.isFocused
                                        filter(editTextValue)

                                    },
                                    onValueChanged = { query ->
                                        editTextValue = query
                                        filter(editTextValue)
                                        stockNewsList = stockNewsListScreenViewModel.stockNewsList.filter {
                                            it.headline?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
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
                            StockNewsList(stockNewsList = stockNewsList, listState = listState,
                                onRowItemClick = { newsUrl ->
                                    val url = URLEncoder.encode(newsUrl, StandardCharsets.UTF_8.toString())
                                    navController.navigate("stocknewsdetail_screen/$url")
                                })
                    }
                }
            })
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun Preview() {
    StockNewsListScreen(navController = NavController(LocalContext.current))
}