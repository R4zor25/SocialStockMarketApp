package hu.bme.aut.android.socialstockmarketapp.domain

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.socialstockmarketapp.domain.model.ConversationComment
import hu.bme.aut.android.socialstockmarketapp.network.FirebaseDataSource
import javax.inject.Inject

class ConversationInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getConversationForStock(stockSymbol: String) = withIOContext {
        firebaseDataSource.getConversationForStock(stockSymbol)
    }

    suspend fun sendConversationComment(conversationComment: ConversationComment, stockSymbol: String) = withIOContext {
        firebaseDataSource.sendConversationComment(conversationComment, stockSymbol)
    }
}