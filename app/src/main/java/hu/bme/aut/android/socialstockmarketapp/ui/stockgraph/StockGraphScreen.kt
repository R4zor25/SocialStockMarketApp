package hu.bme.aut.android.socialstockmarketapp.ui.stockgraph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.DatePickerview
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.SpinnerView
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import io.finnhub.api.models.StockCandles
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StockGraphScreen(navController: NavController, companySymbol: String) {

    val stockGraphScreenViewModel = hiltViewModel<StockGraphScreenViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewState = stockGraphScreenViewModel.viewState.collectAsState()
    var stockCandles by remember { mutableStateOf(StockCandles()) }
    val priceDataPointList by remember { mutableStateOf(mutableListOf<DataPoint>(DataPoint(0f, 0f))) }
    val resolutionList by remember {
        mutableStateOf(
            mutableListOf(
                "1 Minute", "5 Minutes", "15 Minutes", "30 Minutes", "60 Minutes",
                "Daily", "Weekly", "Monthly",
            )
        )
    }
    val stockValueList by remember { mutableStateOf(mutableListOf("Close prices", "High prices", "Low prices", "Open prices")) }
    var valueType: String by remember { mutableStateOf("Close prices") }
    var resolution: String by remember { mutableStateOf("60 Minutes") }
    var startDatePicked: String? by remember { mutableStateOf("Start Date") }
    var endDatePicked: String? by remember { mutableStateOf("End Date") }
    var startDateTimeStamp : Long = 0
    val startUpdatedDate = { date: Long? ->
        startDateTimeStamp = date!!
        startDatePicked = dateFormatter(date) }
    var endDateTimeStamp : Long = 0
    val endUpdatedDate = { date: Long? ->
        endDateTimeStamp = date!!
        endDatePicked = dateFormatter(date) }

    LaunchedEffect("key") {
        stockGraphScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockGraphOneShotEvent.AcquireSymbol -> {
                        stockGraphScreenViewModel.stockSymbol = companySymbol
                        stockGraphScreenViewModel.onAction(StockGraphUiAction.OnInit())
                    }
                    is StockGraphOneShotEvent.StockCandlesDataReceived -> {
                        stockCandles = it.stockCandles
                        priceDataPointList.clear()
                        refreshOnValueChange(valueType, stockCandles, priceDataPointList)
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
        topBar = { TopBar("Stock Graph", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        content = {
            
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())) {
                    if (!viewState.value.isLoading && priceDataPointList.isNotEmpty()) {
                        SampleLineGraph(priceDataPointList)
                    }
                        Spacer(Modifier.padding(top = 40.dp))
                        Row(modifier = Modifier.weight(0.065f)) {
                            SpinnerView(
                                dropDownList = resolutionList,
                                onSpinnerItemSelected = { resolution = it },
                                spinnerTitle = "Resolution"
                            )
                        }
                        Row(modifier = Modifier.weight(0.065f)) {
                            SpinnerView(
                                dropDownList = stockValueList,
                                onSpinnerItemSelected = {
                                    valueType = it
                                    refreshOnValueChange(valueType, stockCandles, priceDataPointList)
                                },
                                spinnerTitle = "Stock Value"
                            )
                        }

                        Row(modifier = Modifier.weight(0.1f)) {
                            DatePickerview(startDatePicked, startUpdatedDate)
                        }
                        Row(modifier = Modifier.weight(0.1f)) {
                            DatePickerview(endDatePicked, endUpdatedDate)
                        }
                        Row(modifier = Modifier.weight(0.1f), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center) {
                            Button(
                                onClick = {
                                    if (endDateTimeStamp != 0.toLong() && startDateTimeStamp != 0.toLong()) {
                                        stockGraphScreenViewModel.onAction(
                                            StockGraphUiAction.RefreshGraphData(resolution, startDateTimeStamp, endDateTimeStamp)
                                        )
                                    }
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
                                Text(text = "Refresh", fontSize = 24.sp, color = Color.Black)
                            }
                        }
                }
        }
    )
}

fun refreshOnValueChange(valueType: String, stockCandles: StockCandles, priceDataPointList: MutableList<DataPoint>) {
    priceDataPointList.clear()
    when(valueType){
        "Close prices" ->{
            stockCandles.c?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
        "High prices" -> {
            stockCandles.h?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
        "Low prices" -> {
            stockCandles.l?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
        "Open prices" -> {
            stockCandles.o?.forEachIndexed { i, price ->
                priceDataPointList.add(DataPoint(i.toFloat(), price))
            }
        }
    }
}

@Composable
fun SampleLineGraph(lines: MutableList<DataPoint>) {
    var price by remember { mutableStateOf(0f) }
    Column() {
        LineGraph(
            plot = LinePlot(
                listOf(
                    LinePlot.Line(
                        lines.toList(),
                        LinePlot.Connection(color = Color.Red),
                        LinePlot.Intersection(color = MyBlue, radius = 2.dp),
                        LinePlot.Highlight(color = Color.Yellow),
                    )
                ),
                grid = LinePlot.Grid(Color.Magenta, steps = 10),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .height(300.dp),
            onSelection = { xLine, points ->
                    price = points[0].y
            }
        )
        Text(text = "Exchange Ratio: $price", color = Color.Black)
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