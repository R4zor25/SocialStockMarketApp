package hu.bme.aut.android.socialstockmarketapp.ui.cryptostocklist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.CryptoSymbol
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
class CryptoStockListScreenViewModel @Inject constructor(): ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<CryptoStockListScreenViewState> = MutableStateFlow(CryptoStockListScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<CryptoStockListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var cryptoSymbolList = listOf<CryptoSymbol>()


    init{
        onAction(CryptoStockListUiAction.OnInit())
    }

    fun onAction(cryptoStockListUiAction: CryptoStockListUiAction){
        when(cryptoStockListUiAction){
            is CryptoStockListUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5o81hqad3i92b40uth0"
                    cryptoSymbolList = apiClient.cryptoSymbols("binance")
                    _oneShotEvents.send(CryptoStockListOneShotEvent.DataListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is CryptoStockListUiAction.SpinnerSelected -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    cryptoSymbolList = apiClient.cryptoSymbols(cryptoStockListUiAction.spinnerName.lowercase(Locale.getDefault()))
                    _oneShotEvents.send(CryptoStockListOneShotEvent.DataListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
    }