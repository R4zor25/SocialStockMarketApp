package hu.bme.aut.android.socialstockmarketapp.ui.stocklist

data class StockListScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockListOneShotEvent {
    object DataListReceived : StockListOneShotEvent()
    object ShowToastMessage : StockListOneShotEvent()
}

sealed class StockListUiAction {
    class OnInit() : StockListUiAction()
    class SpinnerSelected(val spinnerName: String) : StockListUiAction()
}