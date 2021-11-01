package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import io.finnhub.api.models.CompanyProfile2
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoilApi::class)
@Composable
fun StockDetailScreen(navController: NavHostController, stockSymbol: String?) {
    val stockDetailViewModel = hiltViewModel<StockDetailScreenViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewState = stockDetailViewModel.viewState.collectAsState()
    var companyProfile by rememberSaveable { mutableStateOf(CompanyProfile2()) }
    val uriHandler = LocalUriHandler.current

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

                    }
                    else -> {

                    }
                }
            }
            .collect()

        /*Céges adatok a tetejére --> CompanyProfile2
                Name
        weburl
        logo
        currency
        exchange
        country
        Real Time adat a cégről --> Quote --ez mehet erre a screenre még szerintem*/
    }
    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = { TopBar("Stock List", buttonIcon = null, scope, scaffoldState) },
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 12.dp)) {
                if (!companyProfile.logo.isNullOrBlank()) {
                    Image(
                        painter = rememberImagePainter(companyProfile.logo.toString()),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp)
                            .size(100.dp)
                    )
                }
                if(!companyProfile.name.isNullOrBlank()) {
                    Row() {
                        Text(text = "Company Name: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${companyProfile.name}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                if(!companyProfile.country.isNullOrBlank()) {
                    Row() {
                        Text(text = "Country: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${companyProfile.country}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                if(!companyProfile.exchange.isNullOrBlank()) {
                    Row() {
                        Text(text = "Exchange: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${companyProfile.exchange}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                if(!companyProfile.currency.isNullOrBlank()) {
                    Row() {
                        Text(text = "Currency: ", color = Color.Black, fontSize = 16.sp)
                        Text(text = " ${companyProfile.currency}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                if(!companyProfile.weburl.isNullOrBlank()) {
                    Row {
                        ClickableText(
                            text = buildAnnotatedString {
                                pushStringAnnotation(tag = companyProfile.weburl.toString(), annotation = companyProfile.weburl.toString())
                            },
                            onClick = { uriHandler.openUri(companyProfile.weburl.toString()) },
                            style = TextStyle(
                                color = Color.Blue,
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }
            }
        }
    )
}