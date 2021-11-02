package hu.bme.aut.android.socialstockmarketapp.ui.stockgraph

import io.finnhub.api.models.SocialSentiment

data class StockGraphScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockGraphOneShotEvent{
    data class StockGraphReceived(val socialSentiment : SocialSentiment): StockGraphOneShotEvent()
    object ShowToastMessage: StockGraphOneShotEvent()
}

sealed class StockGraphUiAction{
    class OnInit(): StockGraphUiAction()
}