package hu.bme.aut.android.socialstockmarketapp.ui.stockconversation

import hu.bme.aut.android.socialstockmarketapp.domain.model.ConversationComment

data class StockConversationScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockConversationOneShotEvent{
    object ShowToastMessage: StockConversationOneShotEvent()
    object AcquireSymbol: StockConversationOneShotEvent()
    data class CommentsReceived(val conversationComments: MutableList<ConversationComment>): StockConversationOneShotEvent()
}

sealed class StockConversationUiAction{
    class OnInit(): StockConversationUiAction()
    class SendMessage(val message: String): StockConversationUiAction()
}