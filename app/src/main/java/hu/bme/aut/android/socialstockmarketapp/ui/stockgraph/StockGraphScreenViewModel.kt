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
import javax.inject.Inject

@HiltViewModel
class StockGraphScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockGraphScreenViewState> = MutableStateFlow(StockGraphScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockGraphOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()


    init {
        onAction(StockGraphUiAction.OnInit())
    }

    fun onAction(stockGraphUiAction: StockGraphUiAction){
        when(stockGraphUiAction){
            is StockGraphUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    //TODO API hívás és one shot event
                    //_oneShotEvents.send(CompanyNewsOneShotEvent.CompanyNewsReceived(companyNewsList))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}