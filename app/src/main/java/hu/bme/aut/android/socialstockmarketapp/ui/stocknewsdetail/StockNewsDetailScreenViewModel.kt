package hu.bme.aut.android.socialstockmarketapp.ui.stocknewsdetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockNewsDetailScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockNewsDetailScreenViewState> = MutableStateFlow(StockNewsDetailScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockNewsDetailOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    init {
        onAction(StockNewsDetailUiAction.OnInit())
    }

    fun onAction(stockNewsDetailUiAction: StockNewsDetailUiAction){
        when(stockNewsDetailUiAction){
            is StockNewsDetailUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    //TODO prep webview ? Load webview
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}