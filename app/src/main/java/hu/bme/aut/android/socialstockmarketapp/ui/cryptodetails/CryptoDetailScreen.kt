package hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.madrapps.plot.line.DataPoint
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.DatePickerview
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.SpinnerView
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.graph.StockPriceLineGraph
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import io.finnhub.api.models.CryptoCandles
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

@OptIn(InternalCoroutinesApi::class)
@Composable
fun CryptoDetailScreen(navController: NavController, cryptoSymbol: String) {

    val cryptoDetailScreenViewModel = hiltViewModel<CryptoDetailScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val viewState = cryptoDetailScreenViewModel.viewState.collectAsState()
    var cryptoCandles by remember { mutableStateOf(CryptoCandles()) }
    val priceDataPointList by remember { mutableStateOf(mutableListOf<DataPoint>(DataPoint(0f, 0f))) }
    val resolutionList by rememberSaveable { mutableStateOf(mutableListOf("1 Minute", "5 Minutes", "15 Minutes", "30 Minutes", "60 Minutes", "Daily", "Weekly", "Monthly")) }
    val cryptoValueList by rememberSaveable { mutableStateOf(mutableListOf("Close price", "High price", "Low price", "Open price")) }
    val startDate: Date = Calendar.getInstance().apply {
        add(Calendar.MONTH, -1)
    }.time
    var valueType: String by remember { mutableStateOf("Close price") }
    var resolution: String by remember { mutableStateOf("60 Minutes") }
    var startDatePicked: String? by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd").format(startDate)) }
    var endDatePicked: String? by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd").format(Date())) }
    var startDateTimeStamp: Long = startDate.time
    val startUpdatedDate = { date: Long? ->
        startDateTimeStamp = date!!
        startDatePicked = dateFormatter(date)
    }
    var endDateTimeStamp: Long = Date().time
    val endUpdatedDate = { date: Long? ->
        endDateTimeStamp = date!!
        endDatePicked = dateFormatter(date)
    }

    LaunchedEffect("key") {
        cryptoDetailScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    CryptoDetailOneShotEvent.AcquireSymbol -> {
                        cryptoDetailScreenViewModel.cryptoSymbol = cryptoSymbol
                        cryptoDetailScreenViewModel.onAction(CryptoDetailUiAction.OnInit)
                    }
                    is CryptoDetailOneShotEvent.StockCandlesDataReceived -> {
                        cryptoCandles = it.cryptoCandles
                        priceDataPointList.clear()
                        refreshOnValueChange(valueType, cryptoCandles, priceDataPointList)
                    }
                    else -> {
                    }
                }
            }
            .collect()

    }
    Scaffold(
        backgroundColor = Color.White,
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.crypto_graph), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        content = {
            if (!viewState.value.dataAvailable && !viewState.value.isLoading) {
                Text(
                    "Unfortunately, data is not available for this stock!", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                )
            } else if (viewState.value.dataAvailable && !viewState.value.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (!viewState.value.isLoading && priceDataPointList.isNotEmpty() && !viewState.value.redrawNeeded)
                        StockPriceLineGraph(priceDataPointList, stringResource(R.string.exchange_ratio))
                    Spacer(Modifier.padding(top = 40.dp))
                    Row(modifier = Modifier.weight(0.065f)) {
                        SpinnerView(
                            dropDownList = resolutionList.toMutableList(),
                            onSpinnerItemSelected = { resolution = it },
                            spinnerTitle = stringResource(R.string.resolution),
                            resolution
                        )
                    }
                    Row(modifier = Modifier.weight(0.065f)) {
                        SpinnerView(
                            dropDownList = cryptoValueList.toMutableList(),
                            onSpinnerItemSelected = {
                                valueType = it
                                refreshOnValueChange(valueType, cryptoCandles, priceDataPointList)
                                cryptoDetailScreenViewModel.onAction(CryptoDetailUiAction.RedrawGraphData())
                            },
                            spinnerTitle = stringResource(R.string.stock_value),
                            valueType
                        )
                    }

                    Column(modifier = Modifier.weight(0.1f)) {
                        Text(
                            stringResource(R.string.from), color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        DatePickerview(startDatePicked, startUpdatedDate)
                    }
                    Column(modifier = Modifier.weight(0.1f)) {
                        Text(
                            stringResource(R.string.to), color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        DatePickerview(endDatePicked, endUpdatedDate)
                    }
                    Row(modifier = Modifier.weight(0.1f), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = {
                                cryptoDetailScreenViewModel.onAction(
                                    CryptoDetailUiAction.RefreshGraphData(resolution, startDateTimeStamp, endDateTimeStamp)
                                )
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .width(130.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MyBlue
                            ),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text(text = stringResource(R.string.refresh), fontSize = 24.sp, color = Color.Black)
                        }
                    }
                }
            } else {
                CircularIndeterminateProgressBar(isDisplayed = true)
            }
        }
    )
}

fun refreshOnValueChange(valueType: String, cryptoCandles: CryptoCandles, priceDataPointList: MutableList<DataPoint>) {
    priceDataPointList.clear()
    when (valueType) {
        "Close price" -> {
            cryptoCandles.c?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
        "High price" -> {
            cryptoCandles.h?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
        "Low price" -> {
            cryptoCandles.l?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
        "Open price" -> {
            cryptoCandles.o?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
    }
}

fun dateFormatter(milliseconds: Long?): String? {
    milliseconds?.let {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        return formatter.format(calendar.getTime())
    }
    return null
}