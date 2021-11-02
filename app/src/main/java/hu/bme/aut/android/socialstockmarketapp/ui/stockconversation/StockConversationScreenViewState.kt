package hu.bme.aut.android.socialstockmarketapp.ui.stockconversation

data class StockConversationScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockConversationOneShotEvent{
    object StockConversationReceived: StockConversationOneShotEvent()
    object ShowToastMessage: StockConversationOneShotEvent()
}

sealed class StockConversationUiAction{
    class OnInit(): StockConversationUiAction()
}