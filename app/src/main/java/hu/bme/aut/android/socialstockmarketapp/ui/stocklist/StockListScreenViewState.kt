package hu.bme.aut.android.socialstockmarketapp.ui.stocklist

data class StockListScreenViewState(val isLoading: Boolean = false)

sealed class StockListOneShotEvent {
    object StockSymbolListReceived : StockListOneShotEvent()
}

sealed class StockListUiAction {
    class OnInit() : StockListUiAction()
    class SpinnerSelected(val spinnerName: String) : StockListUiAction()
}