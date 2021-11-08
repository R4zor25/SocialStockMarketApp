package hu.bme.aut.android.socialstockmarketapp.ui.stockadvice

import io.finnhub.api.models.RecommendationTrend

data class StockAdviceScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockAdviceOneShotEvent{
    data class RecommendationTrendReceived(val recommendationTrendList: List<RecommendationTrend>): StockAdviceOneShotEvent()
    object ShowToastMessage: StockAdviceOneShotEvent()
    object AcquireStockSymbol: StockAdviceOneShotEvent()
}

sealed class StockAdviceUiAction{
    class OnInit(): StockAdviceUiAction()
}