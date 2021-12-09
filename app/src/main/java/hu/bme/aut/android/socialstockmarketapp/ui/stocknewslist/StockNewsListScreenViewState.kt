package hu.bme.aut.android.socialstockmarketapp.ui.stocknewslist

data class StockNewsListScreenViewState(val isLoading: Boolean = false)

sealed class StockNewsListOneShotEvent{
    object DataListReceived: StockNewsListOneShotEvent()
    object ShowToastMessage: StockNewsListOneShotEvent()
}

sealed class StockNewsListUiAction{
    class OnInit(): StockNewsListUiAction()
    class SpinnerSelected(val spinnerName: String): StockNewsListUiAction()
}