package hu.bme.aut.android.socialstockmarketapp.ui.stocksocialsentiment

import io.finnhub.api.models.SocialSentiment

data class StockSocialSentimentScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockSocialSentimentOneShotEvent{
    data class StockSocialSentimentReceived(val socialSentiment : SocialSentiment): StockSocialSentimentOneShotEvent()
    object ShowToastMessage: StockSocialSentimentOneShotEvent()
}

sealed class StockSocialSentimentUiAction{
    class OnInit(): StockSocialSentimentUiAction()
}