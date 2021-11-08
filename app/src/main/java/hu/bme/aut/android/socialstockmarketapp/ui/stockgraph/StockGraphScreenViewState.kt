package hu.bme.aut.android.socialstockmarketapp.ui.stockgraph

import io.finnhub.api.models.StockCandles

data class StockGraphScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockGraphOneShotEvent{
    data class StockCandlesDataReceived(val stockCandles: StockCandles): StockGraphOneShotEvent()
    object ShowToastMessage: StockGraphOneShotEvent()
    object AcquireSymbol: StockGraphOneShotEvent()
}

sealed class StockGraphUiAction{
    class OnInit(): StockGraphUiAction()
    class RefreshGraphData(val resolution: String, val startDateTimeStamp: Long, val endDateTimeStamp: Long): StockGraphUiAction()
}