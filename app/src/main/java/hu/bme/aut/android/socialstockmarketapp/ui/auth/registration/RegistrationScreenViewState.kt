package hu.bme.aut.android.socialstockmarketapp.ui.auth.registration

data class RegistrationScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class RegistrationOneShotEvent{
    object NavigateToStockList: RegistrationOneShotEvent()
    object ShowToastMessage: RegistrationOneShotEvent()
}

sealed class RegistrationUiAction{
    class OnRegistration(val email: String, val userName: String, val passwd: String): RegistrationUiAction()
}