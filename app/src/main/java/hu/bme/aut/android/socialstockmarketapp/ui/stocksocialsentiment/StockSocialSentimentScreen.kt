package hu.bme.aut.android.socialstockmarketapp.ui.stocksocialsentiment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import io.finnhub.api.models.SocialSentiment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun StockSocialSentimentScreen(navController: NavController, companySymbol: String){
    val stockSocialSentimentViewModel = hiltViewModel<StockSocialSentimentScreenViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewState = stockSocialSentimentViewModel.viewState.collectAsState()
    var socialSentiment by rememberSaveable { mutableStateOf(SocialSentiment()) }
    var redditMention : MutableState<Long> = remember { mutableStateOf(0)}
    var redditPositive : MutableState<Long> = remember { mutableStateOf(0)}
    var redditNegative : MutableState<Long> = remember { mutableStateOf(0)}
    var twitterMention : MutableState<Long> = remember { mutableStateOf(0)}
    var twitterPositive : MutableState<Long> = remember { mutableStateOf(0)}
    var twitterNegative : MutableState<Long> = remember { mutableStateOf(0)}

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
                                reddit.let {
                                    redditMention.value += it.mention!!
                                    redditNegative.value += it.negativeMention!!
                                    redditPositive.value += it.positiveMention!!
                                }
                            }
                        }
                        if(socialSentiment.twitter != null) {
                            for (twitter in socialSentiment.twitter!!){
                                twitter.let {
                                    twitterMention.value += it.mention!!
                                    twitterNegative.value += it.negativeMention!!
                                    twitterPositive.value += it.positiveMention!!
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
        topBar = { TopBar("Social Sentiment", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
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
                            "Reddit", fontSize = 19.sp, fontWeight = FontWeight.Bold,
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
                                "Twitter", fontSize = 19.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                            )
                        }
                    }

                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = "Twitter mentions: ", color = Color.Black, fontSize = 16.sp)
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
