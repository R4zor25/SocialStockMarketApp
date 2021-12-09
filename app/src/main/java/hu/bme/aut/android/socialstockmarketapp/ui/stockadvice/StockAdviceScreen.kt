package hu.bme.aut.android.socialstockmarketapp.ui.stockadvice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.RecommendationTrendList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import io.finnhub.api.models.RecommendationTrend
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun StockAdviceScreen(navController: NavController, companySymbol: String ){
    val stockAdviceScreenViewModel = hiltViewModel<StockAdviceScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val viewState = stockAdviceScreenViewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    var recommendationTrendList by remember { mutableStateOf(listOf<RecommendationTrend>())}

    LaunchedEffect("key") {
        stockAdviceScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    is StockAdviceOneShotEvent.AcquireStockSymbol -> {
                        stockAdviceScreenViewModel.stockSymbol = companySymbol
                        stockAdviceScreenViewModel.onAction(StockAdviceUiAction.OnInit())
                    }
                    is StockAdviceOneShotEvent.RecommendationTrendReceived -> {
                        recommendationTrendList = it.recommendationTrendList
                    }
                    else -> { }
                }
            }
            .collect()
    }

    Surface() {
        Scaffold(
            backgroundColor = Color.White,
            modifier = Modifier.background(Color.White),
            scaffoldState = scaffoldState,
            topBar = { TopBar(stringResource(R.string.recommendation_trends), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
            drawerBackgroundColor = Color.White,
            drawerContent = {
                NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
            }, content = { _ ->


                Column() {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    ) {
                        if (viewState.value.isLoading)
                            CircularIndeterminateProgressBar(isDisplayed = true)
                        else if(recommendationTrendList.isNotEmpty())
                            RecommendationTrendList(recommendationTrendList = recommendationTrendList, listState = listState)
                        else
                            Text(
                                stringResource(R.string.recommendation_trend_is_not_available_for_this_stock), fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                            )
                    }
                }
            })
    }

}