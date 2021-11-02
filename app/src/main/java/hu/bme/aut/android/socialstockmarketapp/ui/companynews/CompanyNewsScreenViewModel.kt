package hu.bme.aut.android.socialstockmarketapp.ui.companynews

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.CompanyNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CompanyNewsScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<CompanyNewsScreenViewState> = MutableStateFlow(CompanyNewsScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<CompanyNewsOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var companyNewsList = listOf<CompanyNews>()

    val l1 = LocalDate.parse("14-02-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    val unix1 = l1.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
    val l2 = LocalDate.parse("14-01-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    val unix2 = l2.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond




    init {
        onAction(CompanyNewsUiAction.OnInit())
    }

    fun onAction(companyNewsUiAction: CompanyNewsUiAction){
        when(companyNewsUiAction){
            is CompanyNewsUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    companyNewsList = apiClient.companyNews("AAPL", unix1.toString(), unix2.toString() )
                    _oneShotEvents.send(CompanyNewsOneShotEvent.CompanyNewsReceived(companyNewsList))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is CompanyNewsUiAction.SpinnerSelected -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    //companyNewsList = apiClient.marketNews(stockNewsListUiAction.spinnerName.lowercase(Locale.getDefault()), 0)
                    //_oneShotEvents.send(StockNewsListOneShotEvent.DataListReceived)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}