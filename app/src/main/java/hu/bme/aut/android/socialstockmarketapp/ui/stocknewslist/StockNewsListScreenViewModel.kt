package hu.bme.aut.android.socialstockmarketapp.ui.stocknewslist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.MarketNews
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
class StockNewsListScreenViewModel @Inject constructor() : ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockNewsListScreenViewState> = MutableStateFlow(StockNewsListScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockNewsListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockNewsList = listOf<MarketNews>()


    init {
        onAction(StockNewsListUiAction.OnInit())
    }

    fun onAction(stockNewsListUiAction: StockNewsListUiAction) {
        when (stockNewsListUiAction) {
            is StockNewsListUiAction.OnInit -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    stockNewsList = apiClient.marketNews("general", 0)
                    _oneShotEvents.send(StockNewsListOneShotEvent.DataListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is StockNewsListUiAction.SpinnerSelected -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    stockNewsList = apiClient.marketNews(stockNewsListUiAction.spinnerName.lowercase(Locale.getDefault()), 0)
                    _oneShotEvents.send(StockNewsListOneShotEvent.DataListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}