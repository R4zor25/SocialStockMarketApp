package hu.bme.aut.android.socialstockmarketapp.ui.companynews

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteBox
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.AutoCompleteSearchBarTag
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete.asAutoCompleteEntities
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.searchbar.TextSearchBar
import io.finnhub.api.models.CompanyNews
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*

@ExperimentalAnimationApi
@Composable
fun CompanyNewsScreen(navController: NavController, companySymbol: String) {
    val companyNewsScreenViewModel = hiltViewModel<CompanyNewsScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    var companyNewsList by rememberSaveable { mutableStateOf(listOf<CompanyNews>()) }
    val viewState = companyNewsScreenViewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    val newsHeadlines by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var editTextValue by rememberSaveable { mutableStateOf("") }


    var autoCompleteEntities by rememberSaveable {
        mutableStateOf(newsHeadlines.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        ))
    }

    LaunchedEffect("key") {
        companyNewsScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    is CompanyNewsOneShotEvent.CompanyNewsReceived -> {
                        companyNewsList = it.companyNews

                        newsHeadlines.clear()
                        for (companyNews in companyNewsList)
                            newsHeadlines.add(companyNews.headline.toString())
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
            topBar = { TopBar("Company News", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
            drawerBackgroundColor = Color.White,
            drawerContent = {
                NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }, content = { _ ->

                Column() {
                    //TODO 2 DatePicker, felparamozott min és max date-el
                    Row() {
                        if (!viewState.value.isLoading) {
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
                                    companyNewsList = companyNewsScreenViewModel.companyNewsList.filter {
                                        it.headline?.lowercase(Locale.getDefault())?.contains(editTextValue.lowercase()) ?: false
                                    }
                                    view.clearFocus()
                                }

                                TextSearchBar(
                                    modifier = Modifier
                                        .testTag(AutoCompleteSearchBarTag)
                                        .padding(start = 12.dp),
                                    value = editTextValue,
                                    label = "Search in Headline",
                                    onDoneActionClick = {
                                        view.clearFocus()
                                    },
                                    onClearClick = {
                                        editTextValue = ""
                                        companyNewsList = companyNewsScreenViewModel.companyNewsList
                                        filter(editTextValue)

                                        view.clearFocus()
                                    },
                                    onFocusChanged = { focusState ->
                                        isSearching = focusState.isFocused

                                    },
                                    onValueChanged = { query ->
                                        editTextValue = query
                                        filter(editTextValue)
                                        companyNewsList = companyNewsScreenViewModel.companyNewsList.filter {
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
                        else{//TODO CompanyNEws Lazy column komponens készítése
                        }
                            //StockNewsList(stockNewsList = companyNewsList, listState = listState,
                              //  onRowItemClick = { newsUrl ->
                                   // val url = URLEncoder.encode(newsUrl, StandardCharsets.UTF_8.toString())
                                  //  navController.navigate("stocknewsdetail_screen/$url")
                               // })
                    }
                }
            })
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
fun Preview() {
    CompanyNewsScreen(navController = NavController(LocalContext.current), "")
}