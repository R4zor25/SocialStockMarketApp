package hu.bme.aut.android.socialstockmarketapp.domain

import hu.bme.aut.android.socialstockmarketapp.network.FirebaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun followStock(userName: String, stockSymbol: String): Boolean = withContext(Dispatchers.IO) {
        firebaseDataSource.followStock(userName, stockSymbol)
    }

    suspend fun unfollowStock(userName: String, stockSymbol: String): Boolean = withContext(Dispatchers.IO) {
        firebaseDataSource.unfollowStock(userName, stockSymbol)
    }

    suspend fun getStocksForUser(userName: String) = withContext(Dispatchers.IO) {
        firebaseDataSource.getStocksForUser(userName)
    }
}