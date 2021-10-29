package hu.bme.aut.android.socialstockmarketapp.ui.stocklist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.AuthInteractor
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.StockSymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListScreenViewModel @Inject constructor(private val authInteractor: AuthInteractor): ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockListScreenViewState> = MutableStateFlow(StockListScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var stockSymbolList = listOf<StockSymbol>()

    /*API IDEAS
    SymbolLookUp --> Részvény keresés legjobban matchelő a név alapján--> Kellhet
        Céges adatokat lehet lekérni(CompanyProfile2) --> Erre fel elhet húzni egy Screen-t
        Tőzsdehíreket lehet lekérni(MarketNews) --> Lehet az egyik kezdőképernyő, ami egy webview-ra irányít ?
        Cég hírek ? eh(CompanyNews) --> Nem hiszem
        PEERS -> Hasonló cégek lekérése az adott országban
        Adott cég általános adatait Dátumtól dátumig(BasicFinancials) --> Részletes képernyőre jó lehet nagyon
        Nagyobb befektetők részvény változásai az adott cégben tólig(Insider Transactions) -->Not really
        Adott cég nyereség veszteség stb. adat ? --> Maybe
        Adott cégnél a piaci tendenciák alapján mit érdemes tenni (Recommendation Trends), --> Mindenképpen
        Adott cég real time értékei (Quote) --> Maybe
        Adott cég Gyerta fosa --> IGEN MINDENKÉPPEN DIAGRAMHOZ EBBŐL!!!!
        Crypto Exchanges--> Mindenképpen()
        CryptoSymbols --> Maybe nem kell
        Candle--> Mindenképpen
        Social Sentiment --> Mennyire vannak jó véleménnyel az adott részvényról Redditen és Twitteren, hehe, mindenképpen érdekes
    * */

    init{
        onAction(StockListUiAction.OnInit())
    }

    fun onAction(stockListUiAction: StockListUiAction){
        when(stockListUiAction){
            is StockListUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5o81hqad3i92b40uth0"
                    stockSymbolList = apiClient.stockSymbols("US", "", "", "")
                    _oneShotEvents.send(StockListOneShotEvent.DataListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }

}