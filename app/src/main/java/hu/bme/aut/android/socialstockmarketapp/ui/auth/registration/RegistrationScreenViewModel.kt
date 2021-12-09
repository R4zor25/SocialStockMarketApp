package hu.bme.aut.android.socialstockmarketapp.ui.auth.registration

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.AuthInteractor
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
class RegistrationScreenViewModel @Inject constructor(private val authInteractor: AuthInteractor) : ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<RegistrationScreenViewState> = MutableStateFlow(RegistrationScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<RegistrationOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun onAction(registrationUiAction: RegistrationUiAction) {
        when (registrationUiAction) {
            is RegistrationUiAction.OnRegistration -> {
                coroutineScope.launch {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    if(registrationUiAction.email.isNotBlank() && registrationUiAction.passwd.isNotBlank() && registrationUiAction.userName.isNotBlank()) {
                        val message = withContext(Dispatchers.IO) { authInteractor.createUser(registrationUiAction.email, registrationUiAction.userName, registrationUiAction.passwd) }
                        if (message == "Registration successful!") {
                            _oneShotEvents.send(RegistrationOneShotEvent.NavigateToStockList)
                        } else {
                            _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage(message))
                        }
                    }else{
                        _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage("One or more text fields are empty!"))
                    }
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}