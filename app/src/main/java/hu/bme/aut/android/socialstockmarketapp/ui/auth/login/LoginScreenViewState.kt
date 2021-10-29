package hu.bme.aut.android.socialstockmarketapp.ui.auth.login

data class LoginScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class LoginOneShotEvent {
    object NavigateToStockList : LoginOneShotEvent()
    object ShowToastMessage: LoginOneShotEvent()
}

sealed class LoginUiAction {
    class OnLogin(val email: String, val passwd: String) : LoginUiAction()
}