package hu.bme.aut.android.socialstockmarketapp.ui.stockgraph

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StockGraphScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockGraphScreenViewState> = MutableStateFlow(StockGraphScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockGraphOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockSymbol : String = ""


    init {
        coroutineScope.launch {
            _oneShotEvents.send(StockGraphOneShotEvent.AcquireSymbol)
        }
    }

    fun onAction(stockGraphUiAction: StockGraphUiAction){
        when(stockGraphUiAction){
            is StockGraphUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    val calendar = Calendar.getInstance()
                    val end = calendar.timeInMillis
                    calendar.add(Calendar.MONTH, -1)
                    val start = calendar.timeInMillis
                    val candles = apiClient.stockCandles(stockSymbol, "60", start/1000, end/1000)
                    _oneShotEvents.send(StockGraphOneShotEvent.StockCandlesDataReceived(candles))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is StockGraphUiAction.RefreshGraphData ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    val resolution = when(stockGraphUiAction.resolution){
                        "1 Minute" -> "1"
                        "5 Minutes" -> "5"
                        "15 Minutes" -> "15"
                        "30 Minutes" -> "30"
                        "60 Minutes" -> "60"
                        "Daily" -> "D"
                        "Weekly" -> "W"
                        "Monthly" -> "M"
                        else -> "60"
                    }
                    val candles = apiClient.stockCandles(stockSymbol, resolution,
                        stockGraphUiAction.startDateTimeStamp/1000, stockGraphUiAction.endDateTimeStamp/1000)
                    _oneShotEvents.send(StockGraphOneShotEvent.StockCandlesDataReceived(candles))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}