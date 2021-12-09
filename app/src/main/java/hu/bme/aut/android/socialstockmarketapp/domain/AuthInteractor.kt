package hu.bme.aut.android.socialstockmarketapp.domain

import hu.bme.aut.android.socialstockmarketapp.network.FirebaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun createUser(email: String, userName: String, password: String): String = withContext(Dispatchers.IO) {
        firebaseDataSource.createUser(email, userName, password)
    }

    suspend fun login(email: String, password: String): String = withContext(Dispatchers.IO){
        firebaseDataSource.login(email, password)
    }

    fun getCurrentUserName(): String?{
        return firebaseDataSource.getCurrentUserName()
    }

    fun getEmail(): String {
        return firebaseDataSource.getCurrentUserEmail()
    }
}