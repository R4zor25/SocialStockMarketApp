package hu.bme.aut.android.socialstockmarketapp.ui.stocknewsdetail

data class StockNewsDetailScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockNewsDetailOneShotEvent{
    object DataListReceived: StockNewsDetailOneShotEvent()
    object ShowToastMessage: StockNewsDetailOneShotEvent()
}

sealed class StockNewsDetailUiAction{
    class OnInit(): StockNewsDetailUiAction()
}