package hu.bme.aut.android.socialstockmarketapp.ui.auth.registration

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.AuthInteractor
import hu.bme.aut.android.socialstockmarketapp.network.StockApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(private val authInteractor: AuthInteractor): ViewModel() {
    private val coroutineScope = MainScope()
    private val stockApiService = StockApiService()

    private val _viewState: MutableStateFlow<RegistrationScreenViewState> = MutableStateFlow(RegistrationScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<RegistrationOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun onAction(registrationUiAction: RegistrationUiAction){
        when(registrationUiAction){
            is RegistrationUiAction.OnRegistration ->{
                coroutineScope.launch {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    val message = withContext(Dispatchers.IO) { authInteractor.createUser(registrationUiAction.email, registrationUiAction.userName, registrationUiAction.passwd) }
                    if (message == "Registration successful!") {
                        _oneShotEvents.send(RegistrationOneShotEvent.NavigateToStockList)
                    } else {
                        _viewState.value = _viewState.value.copy(errorText = message)
                        _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage)
                    }
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}