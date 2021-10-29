package hu.bme.aut.android.socialstockmarketapp.ui.cryptostocklist

data class CryptoStockListScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class CryptoStockListOneShotEvent {
    object DataListReceived: CryptoStockListOneShotEvent()
    object ShowToastMessage: CryptoStockListOneShotEvent()
}

sealed class CryptoStockListUiAction {
    class OnInit() : CryptoStockListUiAction()
    class SpinnerSelected(val spinnerName: String): CryptoStockListUiAction()
}