package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

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
import javax.inject.Inject

@HiltViewModel
class StockDetailScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockDetailScreenViewState> = MutableStateFlow(StockDetailScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockDetailOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockNewsList = listOf<MarketNews>()


    init {
        onAction(StockDetailUiAction.OnInit())
    }

    fun onAction(stockDetailUiAction: StockDetailUiAction){
        when(stockDetailUiAction){
            is StockDetailUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    //TODO Load datas from diff Api calls
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}