package hu.bme.aut.android.socialstockmarketapp.ui.auth.registration

data class RegistrationScreenViewState(val isLoading: Boolean = false)

sealed class RegistrationOneShotEvent{
    object NavigateToStockList: RegistrationOneShotEvent()
    data class ShowToastMessage(val errorText: String): RegistrationOneShotEvent()
}

sealed class RegistrationUiAction{
    class OnRegistration(val email: String, val userName: String, val passwd: String): RegistrationUiAction()
}