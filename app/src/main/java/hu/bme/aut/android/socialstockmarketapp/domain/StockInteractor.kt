package hu.bme.aut.android.socialstockmarketapp.domain

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.socialstockmarketapp.network.FirebaseDataSource
import javax.inject.Inject

class StockInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun followStock(userName: String, stockSymbol: String) = withIOContext {
        firebaseDataSource.followStock(userName, stockSymbol)
    }

    suspend fun unfollowStock(userName: String, stockSymbol: String) = withIOContext {
        firebaseDataSource.unfollowStock(userName, stockSymbol)
    }

    suspend fun getStocksForUser(userName: String) = withIOContext {
        firebaseDataSource.getStocksForUser(userName)
    }
}