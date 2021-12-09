package hu.bme.aut.android.socialstockmarketapp.ui.stocksocialsentiment

import io.finnhub.api.models.SocialSentiment

data class StockSocialSentimentScreenViewState(val isLoading: Boolean = false)

sealed class StockSocialSentimentOneShotEvent{
    data class StockSocialSentimentReceived(val socialSentiment : SocialSentiment): StockSocialSentimentOneShotEvent()
    object AcquireSymbol: StockSocialSentimentOneShotEvent()
}

sealed class StockSocialSentimentUiAction{
    class OnInit(): StockSocialSentimentUiAction()
}