package hu.bme.aut.android.socialstockmarketapp.ui.auth.login

data class LoginScreenViewState(val isLoading: Boolean = false)

sealed class LoginOneShotEvent {
    object NavigateToStockList : LoginOneShotEvent()
    data class ShowToastMessage(val errorText: String) : LoginOneShotEvent()
}

sealed class LoginUiAction {
    class OnLogin(val email: String, val passwd: String) : LoginUiAction()
}