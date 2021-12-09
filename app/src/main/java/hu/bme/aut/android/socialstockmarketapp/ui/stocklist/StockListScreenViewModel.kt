package hu.bme.aut.android.socialstockmarketapp.ui.stocklist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.StockSymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListScreenViewModel @Inject constructor(): ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockListScreenViewState> = MutableStateFlow(StockListScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockSymbolList = listOf<StockSymbol>()

    init{
        onAction(StockListUiAction.OnInit())
    }

    fun onAction(stockListUiAction: StockListUiAction){
        when(stockListUiAction){
            is StockListUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5o81hqad3i92b40uth0"
                    stockSymbolList = apiClient.stockSymbols("US", "", "", "")
                    _oneShotEvents.send(StockListOneShotEvent.StockSymbolListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is StockListUiAction.SpinnerSelected -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    stockSymbolList = apiClient.stockSymbols(stockListUiAction.spinnerName, "", "", "")
                    _oneShotEvents.send(StockListOneShotEvent.StockSymbolListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }

}