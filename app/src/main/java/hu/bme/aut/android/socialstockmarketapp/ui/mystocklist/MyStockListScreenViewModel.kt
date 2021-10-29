package hu.bme.aut.android.socialstockmarketapp.ui.mystocklist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.AuthInteractor
import hu.bme.aut.android.socialstockmarketapp.network.StockApiService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyStockListScreenViewModel @Inject constructor(private val authInteractor: AuthInteractor, val stockApiService: StockApiService): ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<MyStockListScreenViewState> = MutableStateFlow(MyStockListScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<MyStockListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()


    fun onAction(myStockListUiAction: MyStockListUiAction){
        when(myStockListUiAction){
            is MyStockListUiAction.OnLogin ->{
                coroutineScope.launch {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    //if (message == "Login successful!") {
                     //   _oneShotEvents.send(LoginOneShotEvent.NavigateToStockList)
                    //} else {
                     //   _viewState.value = _viewState.value.copy(errorText = message)
                     //   _oneShotEvents.send(LoginOneShotEvent.ShowToastMessage)
                    }
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }