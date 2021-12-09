package hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails

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
class CryptoDetailScreenViewModel @Inject constructor() : ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<CryptoDetailScreenViewState> = MutableStateFlow(CryptoDetailScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<CryptoDetailOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var cryptoSymbol: String = ""


    init {
        coroutineScope.launch {
            _oneShotEvents.send(CryptoDetailOneShotEvent.AcquireSymbol)
        }
    }

    fun onAction(cryptoDetailUiAction: CryptoDetailUiAction) {
        when (cryptoDetailUiAction) {
            is CryptoDetailUiAction.OnInit -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5o81hqad3i92b40uth0"
                    val calendar = Calendar.getInstance()
                    val end = calendar.timeInMillis
                    calendar.add(Calendar.MONTH, -1)
                    val start = calendar.timeInMillis
                    val candles = apiClient.cryptoCandles(cryptoSymbol, "60", start / 1000, end / 1000)
                    if (candles.c != null && candles.o != null && candles.h != null && candles.l != null) {
                        _viewState.value = _viewState.value.copy(dataAvailable = true)
                        _oneShotEvents.send(CryptoDetailOneShotEvent.StockCandlesDataReceived(candles))
                    }
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is CryptoDetailUiAction.RefreshGraphData -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    val resolution = when (cryptoDetailUiAction.resolution) {
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
                    val candles = apiClient.cryptoCandles(
                        cryptoSymbol, resolution,
                        cryptoDetailUiAction.startDateTimeStamp / 1000, cryptoDetailUiAction.endDateTimeStamp / 1000
                    )
                    _oneShotEvents.send(CryptoDetailOneShotEvent.StockCandlesDataReceived(candles))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is CryptoDetailUiAction.RedrawGraphData -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(redrawNeeded = true)
                    Thread.sleep(200)
                    _viewState.value = _viewState.value.copy(redrawNeeded = false)
                }
            }
        }
    }
}