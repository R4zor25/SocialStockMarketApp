package hu.bme.aut.android.socialstockmarketapp.ui.stockadvice

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
import javax.inject.Inject

@HiltViewModel
class StockAdviceScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockAdviceScreenViewState> = MutableStateFlow(StockAdviceScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockAdviceOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockSymbol: String = ""


    init {
        coroutineScope.launch {
            _oneShotEvents.send(StockAdviceOneShotEvent.AcquireStockSymbol)
        }
    }

    fun onAction(stockAdviceUiAction: StockAdviceUiAction){
        when(stockAdviceUiAction){
            is StockAdviceUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    val recommendationTrend = apiClient.recommendationTrends(stockSymbol)
                    _oneShotEvents.send(StockAdviceOneShotEvent.RecommendationTrendReceived(recommendationTrendList = recommendationTrend))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}