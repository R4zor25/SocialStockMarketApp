package hu.bme.aut.android.socialstockmarketapp.ui.auth.login

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
class LoginScreenViewModel @Inject constructor(private val authInteractor: AuthInteractor): ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<LoginScreenViewState> = MutableStateFlow(LoginScreenViewState(errorText = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<LoginOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()



    fun onAction(loginUiAction: LoginUiAction){
        when(loginUiAction){
            is LoginUiAction.OnLogin ->{
                coroutineScope.launch {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    val message = withContext(Dispatchers.IO) { authInteractor.login(loginUiAction.email, loginUiAction.passwd) }
                    if (message == "Login successful!") {
                        _oneShotEvents.send(LoginOneShotEvent.NavigateToStockList)
                    } else {
                        _viewState.value = _viewState.value.copy(errorText = message)
                        _oneShotEvents.send(LoginOneShotEvent.ShowToastMessage)
                    }
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }

}