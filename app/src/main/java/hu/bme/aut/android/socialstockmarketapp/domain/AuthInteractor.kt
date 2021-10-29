package hu.bme.aut.android.socialstockmarketapp.domain

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.socialstockmarketapp.network.FirebaseDataSource
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
){
    suspend fun createUser(email: String, userName: String, password: String): String = withIOContext {
        firebaseDataSource.createUser(email, userName, password)
    }

    suspend fun login(email: String, password: String):String = withIOContext {
        firebaseDataSource.login(email, password)
    }

    fun getEmail(): String {
        return firebaseDataSource.getCurrentUserEmail()
    }
}