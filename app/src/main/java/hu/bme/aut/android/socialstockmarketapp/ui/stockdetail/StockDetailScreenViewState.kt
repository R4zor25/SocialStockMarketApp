package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

data class StockDetailScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockDetailOneShotEvent{
    object DataListReceived: StockDetailOneShotEvent()
    object ShowToastMessage: StockDetailOneShotEvent()
}

sealed class StockDetailUiAction{
    class OnInit(): StockDetailUiAction()
}