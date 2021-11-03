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
import java.text.SimpleDateFormat
import java.util.*
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

    var stockSymbol: String = ""

    init {
        coroutineScope.launch {
            _oneShotEvents.send(CompanyNewsOneShotEvent.AcquireSymbol)
        }
    }

    fun onAction(companyNewsUiAction: CompanyNewsUiAction){
        when(companyNewsUiAction){
            is CompanyNewsUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    val pattern = "yyyy-MM-dd"
                    val simpleDateFormat = SimpleDateFormat(pattern)
                    val lastyear: String = calcLastYear()
                    val date: String = simpleDateFormat.format(Date())
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    companyNewsList = apiClient.companyNews(stockSymbol, from = lastyear, to = date )
                    _oneShotEvents.send(CompanyNewsOneShotEvent.CompanyNewsReceived(companyNewsList))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }

    private fun calcLastYear(): String {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.YEAR, -1)
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(cal.time)
    }
}