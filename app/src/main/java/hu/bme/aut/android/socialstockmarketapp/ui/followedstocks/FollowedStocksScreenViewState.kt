package hu.bme.aut.android.socialstockmarketapp.ui.followedstocks

data class FollowedStocksScreenViewState(val isLoading: Boolean = false)

sealed class FollowedStocksOneShotEvent{
    data class FollowedStocksReceived(val stockSymbolList : List<String>): FollowedStocksOneShotEvent()
    object AcquireUserName: FollowedStocksOneShotEvent()
}

sealed class FollowedStocksUiAction{
    object OnInit: FollowedStocksUiAction()
}