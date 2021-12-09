package hu.bme.aut.android.socialstockmarketapp.ui.stockadvice

import io.finnhub.api.models.RecommendationTrend

data class StockAdviceScreenViewState(val isLoading: Boolean = false)

sealed class StockAdviceOneShotEvent{
    data class RecommendationTrendReceived(val recommendationTrendList: List<RecommendationTrend>): StockAdviceOneShotEvent()
    object AcquireStockSymbol: StockAdviceOneShotEvent()
}

sealed class StockAdviceUiAction{
    class OnInit(): StockAdviceUiAction()
}