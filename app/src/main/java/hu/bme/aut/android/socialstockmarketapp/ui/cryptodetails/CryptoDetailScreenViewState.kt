package hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails

data class CryptoDetailScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class CryptoDetailOneShotEvent{
    object DataListReceived: CryptoDetailOneShotEvent()
    object ShowToastMessage: CryptoDetailOneShotEvent()
}

sealed class CryptoDetailUiAction{
    class OnInit(): CryptoDetailUiAction()
}