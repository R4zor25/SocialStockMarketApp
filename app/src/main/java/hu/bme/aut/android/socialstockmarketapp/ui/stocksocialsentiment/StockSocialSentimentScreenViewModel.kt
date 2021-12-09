package hu.bme.aut.android.socialstockmarketapp.ui.stocksocialsentiment

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
class StockSocialSentimentScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockSocialSentimentScreenViewState> = MutableStateFlow(StockSocialSentimentScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockSocialSentimentOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockSymbol: String = ""

    init {
        coroutineScope.launch {
            _oneShotEvents.send(StockSocialSentimentOneShotEvent.AcquireSymbol)
        }
    }

    fun onAction(stockSocialSentimentUiAction: StockSocialSentimentUiAction){
        when(stockSocialSentimentUiAction){
            is StockSocialSentimentUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    val socialSentiment = apiClient.socialSentiment(stockSymbol, "", "")
                    _oneShotEvents.send(StockSocialSentimentOneShotEvent.StockSocialSentimentReceived(socialSentiment = socialSentiment))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}