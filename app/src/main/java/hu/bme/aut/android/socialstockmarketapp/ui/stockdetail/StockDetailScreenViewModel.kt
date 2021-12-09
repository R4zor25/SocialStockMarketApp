package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.FriendInteractor
import hu.bme.aut.android.socialstockmarketapp.domain.StockInteractor
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.CompanyProfile2
import io.finnhub.api.models.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailScreenViewModel @Inject constructor(private val stockInteractor: StockInteractor, private val friendInteractor: FriendInteractor): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockDetailScreenViewState> = MutableStateFlow(StockDetailScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockDetailOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var symbol: String = ""

    var companyProfile : CompanyProfile2? = null


    init {
        coroutineScope.launch {
            _oneShotEvents.send(StockDetailOneShotEvent.AcquireSymbol)
        }
    }

    fun onAction(stockDetailUiAction: StockDetailUiAction){
        when(stockDetailUiAction){
            //getting company profile, if not available we show error message
                //else we request more data about the stock and display it
            is StockDetailUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5vrl32ad3ibtqnndf0g"
                    companyProfile = apiClient.companyProfile2(symbol, null, null)
                    if(companyProfile!!.currency == null && companyProfile!!.name == null && companyProfile!!.exchange == null ) {
                        _viewState.value = _viewState.value.copy(isLoading = false)
                    } else {
                        val stocks = stockInteractor.getStocksForUser(friendInteractor.getCurrentUser()!!)
                        var contains = false
                        for(stock in stocks){
                            if(stock == symbol)
                                contains = true
                        }
                        var quote: Quote = Quote()
                        quote = apiClient.quote(symbol)
                        _oneShotEvents.send(StockDetailOneShotEvent.QuoteInfoReceived(quote))
                        _oneShotEvents.send(StockDetailOneShotEvent.CompanyInfoReceived(companyProfile!!, contains))
                        _viewState.value = _viewState.value.copy(isLoading = false)
                        _viewState.value = _viewState.value.copy(isDataAvailable = true)
                    }
                }
            }
        }
    }

    fun followStock(){
        coroutineScope.launch {
            if(stockInteractor.followStock(friendInteractor.getCurrentUser()!!, symbol))
                _oneShotEvents.send(StockDetailOneShotEvent.FollowSuccessful)
            else
                _oneShotEvents.send(StockDetailOneShotEvent.ShowToastMessage("Something went wrong!"))
        }
    }

    fun unfollowStock(){
        coroutineScope.launch {
            if(stockInteractor.unfollowStock(friendInteractor.getCurrentUser()!!, symbol))
                _oneShotEvents.send(StockDetailOneShotEvent.UnfollowSuccessful)
            else
                _oneShotEvents.send(StockDetailOneShotEvent.ShowToastMessage("Something went wrong!"))
        }
    }
}