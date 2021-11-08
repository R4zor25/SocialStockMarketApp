package hu.bme.aut.android.socialstockmarketapp.ui.followedstocks

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.FriendInteractor
import hu.bme.aut.android.socialstockmarketapp.domain.StockInteractor
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
class FollowedStocksScreenViewModel @Inject constructor(private val stockInteractor: StockInteractor, private val friendInteractor: FriendInteractor): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<FollowedStocksScreenViewState> = MutableStateFlow(FollowedStocksScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<FollowedStocksOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockSymbolList = listOf<String>()

    var userName = ""


    init {
        coroutineScope.launch {
            _oneShotEvents.send(FollowedStocksOneShotEvent.AcquireUserName)
        }
    }

    fun onAction(followedStocksUiAction: FollowedStocksUiAction){
        when(followedStocksUiAction){
            is FollowedStocksUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    if(userName == " ")
                        userName = friendInteractor.getCurrentUser()!!
                    stockSymbolList =  stockInteractor.getStocksForUser(userName)
                    _oneShotEvents.send(FollowedStocksOneShotEvent.FollowedStocksReceived(stockSymbolList))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}