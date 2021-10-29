package hu.bme.aut.android.socialstockmarketapp.ui.mystocklist

data class MyStockListScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class MyStockListOneShotEvent {
    object StockListDataReceived : MyStockListOneShotEvent()
    object ShowToastMessage: MyStockListOneShotEvent()
}

sealed class MyStockListUiAction {
    class OnLogin(val email: String, val passwd: String) : MyStockListUiAction()
}