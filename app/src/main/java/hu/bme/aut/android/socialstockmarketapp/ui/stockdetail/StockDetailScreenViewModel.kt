package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.CompanyProfile2
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

    var symbol: String = ""

    var companyProfile : CompanyProfile2? = null

    /*MILYEN API HÍVÁSOK IDE
     Céges adatok a tetejére --> CompanyProfile2
        Name
        weburl
        logo
        currency
        exchange
        country
      Real Time adat a cégről --> Quote --ez mehet erre a screenre még szerintem

    CÉGHÍREK --> CompanyNews
        Ugynaz maradhat + idő, mivel kell datepicker, emiatt lehet másik screen kéne

    ÁLTALÁNOS cég adatok --> Basic Financials
        Current Ratio
        salesPerShare
        netMargin
        metricben
            10 day average trading volume
            52 week high
            52week low, and date
            52 week price return daily
            beta??
    Nyereség, veszteség --> Financials as reported, lehet nem kell

    Social Sentiment --> Vélemények az adott cégről --> Külön screen-en jó lesz


    Cég gyertája --> Candles --> Külön Screen --> Diagram hozzá
    Adott cégnél piaci tendenciák --> Mit érdemes --> Külön Screen Jobb
    Beszélgetés
*/



    init {
        coroutineScope.launch {
            _oneShotEvents.send(StockDetailOneShotEvent.AcquireSymbol)
        }
    }

    fun onAction(stockDetailUiAction: StockDetailUiAction){
        when(stockDetailUiAction){
            is StockDetailUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5vrl32ad3ibtqnndf0g"
                    companyProfile = apiClient.companyProfile2(symbol, null, null)
                    _oneShotEvents.send(StockDetailOneShotEvent.CompanyInfoReceived(companyProfile!!))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}