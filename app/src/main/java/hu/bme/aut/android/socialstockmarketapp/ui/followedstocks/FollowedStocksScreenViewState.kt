package hu.bme.aut.android.socialstockmarketapp.ui.followedstocks

data class FollowedStocksScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class FollowedStocksOneShotEvent{
    data class FollowedStocksReceived(val stockSymbolList : List<String>): FollowedStocksOneShotEvent()
    object ShowToastMessage: FollowedStocksOneShotEvent()
    object AcquireUserName: FollowedStocksOneShotEvent()

}

sealed class FollowedStocksUiAction{
    class OnInit(): FollowedStocksUiAction()
}