package hu.bme.aut.android.socialstockmarketapp.ui.generalstockinformation

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.finnhub.api.models.BasicFinancials
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun GeneralStockInformationScreen(navController: NavController, companySymbol: String){
    val generalStockSocialSentimentScreenViewModel = hiltViewModel<GeneralStockInformationScreenViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewState = generalStockSocialSentimentScreenViewModel.viewState.collectAsState()
    var basicFinancials by rememberSaveable { mutableStateOf(BasicFinancials()) }

    LaunchedEffect("key") {
        generalStockSocialSentimentScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    GeneralStockInformationOneShotEvent.AcquireSymbol -> {
                        generalStockSocialSentimentScreenViewModel.stockSymbol = companySymbol
                        generalStockSocialSentimentScreenViewModel.onAction(GeneralStockInformationUiAction.OnInit())
                    }
                    is GeneralStockInformationOneShotEvent.BasicFinancialsDataReceived -> {
                        basicFinancials = it.basicFinancials
                        basicFinancials.metric
                    }
                    else -> {}
                }
            }
            .collect()
    }/*

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
                            Text(text = " ${basicFinancials.}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
    */
}