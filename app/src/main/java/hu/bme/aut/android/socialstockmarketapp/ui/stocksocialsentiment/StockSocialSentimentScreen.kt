package hu.bme.aut.android.socialstockmarketapp.ui.stocksocialsentiment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import io.finnhub.api.models.SocialSentiment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun StockSocialSentimentScreen(navController: NavController, companySymbol: String){
    val stockSocialSentimentViewModel = hiltViewModel<StockSocialSentimentScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val viewState = stockSocialSentimentViewModel.viewState.collectAsState()
    var socialSentiment by rememberSaveable { mutableStateOf(SocialSentiment()) }
    val redditMention : MutableState<Long> = remember { mutableStateOf(0)}
    val redditPositive : MutableState<Long> = remember { mutableStateOf(0)}
    val redditNegative : MutableState<Long> = remember { mutableStateOf(0)}
    val twitterMention : MutableState<Long> = remember { mutableStateOf(0)}
    val twitterPositive : MutableState<Long> = remember { mutableStateOf(0)}
    val twitterNegative : MutableState<Long> = remember { mutableStateOf(0)}

    LaunchedEffect("key") {
        stockSocialSentimentViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockSocialSentimentOneShotEvent.AcquireSymbol -> {
                        stockSocialSentimentViewModel.stockSymbol = companySymbol
                        stockSocialSentimentViewModel.onAction(StockSocialSentimentUiAction.OnInit())
                    }
                    is StockSocialSentimentOneShotEvent.StockSocialSentimentReceived -> {
                        socialSentiment = it.socialSentiment
                        if(socialSentiment.reddit != null) {
                            for (reddit in socialSentiment.reddit!!){
                                reddit.let { sentiment ->
                                    redditMention.value += sentiment.mention!!
                                    redditNegative.value += sentiment.negativeMention!!
                                    redditPositive.value += sentiment.positiveMention!!
                                }
                            }
                        }
                        if(socialSentiment.twitter != null) {
                            for (twitter in socialSentiment.twitter!!){
                                twitter.let { sentiment ->
                                    twitterMention.value += sentiment.mention!!
                                    twitterNegative.value += sentiment.negativeMention!!
                                    twitterPositive.value += sentiment.positiveMention!!
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
            .collect()

    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.stock_social_sentiment), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 12.dp)
        ) {
            if (!viewState.value.isLoading) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Surface(shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp), color = MyBlue) {
                        Text(
                            stringResource(R.string.reddit), fontSize = 19.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .background(MyBlue)
                        .fillMaxWidth()
                ) {

                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = "Mentions: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${redditMention.value}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = "Positive mentions: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${redditPositive.value}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = "Negative mentions: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${redditNegative.value}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Column(
                        Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Surface(shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp), color = MyBlue) {
                            Text(
                                stringResource(R.string.twitter), fontSize = 19.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                            )
                        }
                    }

                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = "Mentions: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = "${twitterMention.value}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = "Positive mentions: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${twitterPositive.value}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = "Negative mentions: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${twitterNegative.value}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            } else {
                CircularIndeterminateProgressBar(isDisplayed = true)
            }
        }
    }
    )
}
