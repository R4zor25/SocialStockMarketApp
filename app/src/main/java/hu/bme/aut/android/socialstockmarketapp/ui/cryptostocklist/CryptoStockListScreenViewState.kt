package hu.bme.aut.android.socialstockmarketapp.ui.cryptostocklist

data class CryptoStockListScreenViewState(val isLoading: Boolean = false)

sealed class CryptoStockListOneShotEvent {
    object DataListReceived: CryptoStockListOneShotEvent()
}

sealed class CryptoStockListUiAction {
    object OnInit : CryptoStockListUiAction()
    class SpinnerSelected(val spinnerName: String): CryptoStockListUiAction()
}