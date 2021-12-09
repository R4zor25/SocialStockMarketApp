package hu.bme.aut.android.socialstockmarketapp.domain

import hu.bme.aut.android.socialstockmarketapp.domain.model.ConversationComment
import hu.bme.aut.android.socialstockmarketapp.network.FirebaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConversationInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getConversationForStock(stockSymbol: String) = withContext(Dispatchers.IO) {
        firebaseDataSource.getConversationForStock(stockSymbol)
    }

    suspend fun sendConversationComment(conversationComment: ConversationComment, stockSymbol: String) = withContext(Dispatchers.IO) {
        firebaseDataSource.sendConversationComment(conversationComment, stockSymbol)
    }
}