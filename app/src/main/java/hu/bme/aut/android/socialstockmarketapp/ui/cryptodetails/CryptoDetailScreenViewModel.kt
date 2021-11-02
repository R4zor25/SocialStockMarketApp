package hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails

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
class CryptoDetailScreenViewModel @Inject constructor(): ViewModel() {

    private val coroutineScope = MainScope()

    private val _Screen_viewState: MutableStateFlow<CryptoDetailScreenViewState> = MutableStateFlow(CryptoDetailScreenViewState(errorText = ""))
    val viewState = _Screen_viewState.asStateFlow()

    private val _oneShotEvents = Channel<CryptoDetailOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()


    init {
        onAction(CryptoDetailUiAction.OnInit())
    }

    fun onAction(cryptoDetailUiAction: CryptoDetailUiAction){
        when(cryptoDetailUiAction){
            is CryptoDetailUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    _Screen_viewState.value = _Screen_viewState.value.copy(isLoading = true)
                    ApiClient.apiKey["token"] = "c5p9hp2ad3idr38u7mb0"
                    _Screen_viewState.value = _Screen_viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}