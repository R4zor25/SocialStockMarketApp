package hu.bme.aut.android.socialstockmarketapp.ui.followedstocks

import io.finnhub.api.models.SocialSentiment

data class FollowedStocksScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class FollowedStocksOneShotEvent{
    data class FollowedStocksReceived(val socialSentiment : SocialSentiment): FollowedStocksOneShotEvent()
    object ShowToastMessage: FollowedStocksOneShotEvent()
}

sealed class FollowedStocksUiAction{
    class OnInit(): FollowedStocksUiAction()
}