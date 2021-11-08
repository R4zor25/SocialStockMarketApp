package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.StockDetailRowItem
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import io.finnhub.api.models.CompanyProfile2
import io.finnhub.api.models.Quote
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalCoilApi::class)
@Composable
fun StockDetailScreen(navController: NavHostController, stockSymbol: String?) {
    val stockDetailViewModel = hiltViewModel<StockDetailScreenViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewState = stockDetailViewModel.viewState.collectAsState()
    var companyProfile by rememberSaveable { mutableStateOf(CompanyProfile2()) }
    var quote by rememberSaveable { mutableStateOf(Quote()) }
    var buttonText by rememberSaveable { mutableStateOf("Follow stock")}

    LaunchedEffect("key") {
        stockDetailViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockDetailOneShotEvent.AcquireSymbol -> {
                        stockDetailViewModel.symbol = stockSymbol!!
                        stockDetailViewModel.onAction(StockDetailUiAction.OnInit())
                    }
                    is StockDetailOneShotEvent.CompanyInfoReceived -> {
                        companyProfile = it.companyProfile2
                        if(it.contains)
                            buttonText = "Unfollow stock"
                    }
                    is StockDetailOneShotEvent.QuoteInfoReceived -> {
                        quote = it.quote
                    }
                    else -> {

                    }
                }
            }
            .collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = { TopBar("Stock Details", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (!viewState.value.isLoading && !viewState.value.isDataAvailable) {
                    Text(
                        "Stock data is not available for this company!", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                    )
                } else if (!viewState.value.isLoading) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Surface(shape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp), color = MyBlue) {
                            Text(
                                "General Company Information", fontSize = 19.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                            )
                        }
                        if (!companyProfile.logo.isNullOrBlank()) {
                            Image(
                                painter = rememberImagePainter(companyProfile.logo.toString()),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 12.dp, top = 12.dp, bottom = 6.dp)
                                    .size(100.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .background(MyBlue)
                            .fillMaxWidth()
                    ) {

                        if (!companyProfile.name.isNullOrBlank()) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Company Name: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${companyProfile.name}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (!companyProfile.country.isNullOrBlank()) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Country: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${companyProfile.country}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (!companyProfile.exchange.isNullOrBlank()) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Exchange: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${companyProfile.exchange}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (!companyProfile.currency.isNullOrBlank()) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Currency: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${companyProfile.currency}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (!companyProfile.weburl.isNullOrBlank()) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Webpage: ", style = TextStyle(color = Color.Black, fontSize = 16.sp))
                                ClickableText(
                                    text = with(AnnotatedString.Builder()) {
                                        append(companyProfile.weburl.toString())
                                        toAnnotatedString()
                                    },
                                    onClick = {
                                        val url = URLEncoder.encode(companyProfile.weburl.toString(), StandardCharsets.UTF_8.toString())
                                        navController.navigate("stocknewsdetail_screen/$url")
                                    },
                                    style = TextStyle(
                                        color = Color.Blue,
                                        fontSize = 16.sp,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            }
                        }

                        Column(
                            Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(top = 8.dp)) {
                            Surface(shape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp), color = MyBlue) {
                                Text(
                                    "General Stock Information", fontSize = 19.sp, fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                                )
                        }
                        }
                        if (quote.c != null) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Current Price: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${quote.c}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (quote.h != null) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Highest price today: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${quote.h}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (quote.l != null) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Lowest price today: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${quote.l}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (quote.o != null) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Open price of the day: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${quote.o}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (quote.pc != null) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Previous close price: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${quote.pc}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        if (quote.dp != null) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Percent Change: ", color = Color.Black, fontSize = 16.sp)
                                Text(text = " ${quote.dp}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        Column(
                            Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(top = 8.dp)) {
                            Surface(shape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp), color = MyBlue) {
                                Text(
                                    "Other Options", fontSize = 19.sp, fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                                )
                            }
                        }
                        
                        StockDetailRowItem(title = "Company related news") { navController.navigate("companynews_screen/$stockSymbol")}
                        StockDetailRowItem(title = "Company's social sentiment") {navController.navigate("stocksocialsentiment_screen/$stockSymbol")}
                        StockDetailRowItem(title = "Stock graph") {navController.navigate("stockgraph_screen/$stockSymbol")}
                        StockDetailRowItem(title = "Stock advice") { navController.navigate("stockadvice_screen/$stockSymbol") }
                        StockDetailRowItem(title = "Conversation about stock") {navController.navigate("stockconversation_screen/$stockSymbol")}
                        StockDetailRowItem(title = buttonText) {
                            if(buttonText == "Follow stock") {
                                if(stockDetailViewModel.followStock())
                                    buttonText = "Unfollow stock"
                            }
                            else {
                                if(stockDetailViewModel.unfollowStock())
                                    buttonText = "Follow stock"
                            }
                        }

                    }
                } else {
                    CircularIndeterminateProgressBar(isDisplayed = true)
                }
            }
        }
    )
}