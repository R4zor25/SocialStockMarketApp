package hu.bme.aut.android.socialstockmarketapp.ui.stockgraph

import io.finnhub.api.models.StockCandles

data class StockGraphScreenViewState(val isLoading: Boolean = false, val dataAvailable: Boolean = false, val redrawNeeded: Boolean = false)

sealed class StockGraphOneShotEvent {
    data class StockCandlesDataReceived(val stockCandles: StockCandles) : StockGraphOneShotEvent()
    object AcquireSymbol : StockGraphOneShotEvent()
}

sealed class StockGraphUiAction {
    class OnInit() : StockGraphUiAction()
    class RefreshGraphData(val resolution: String, val startDateTimeStamp: Long, val endDateTimeStamp: Long) : StockGraphUiAction()
    class RedrawGraphData() : StockGraphUiAction()
}