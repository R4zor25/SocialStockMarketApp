package hu.bme.aut.android.socialstockmarketapp.domain

import hu.bme.aut.android.socialstockmarketapp.network.FirebaseDataSource
import javax.inject.Inject

class FriendInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun getFriendsForCurrentUser(): List<String> {
        return firebaseDataSource.getFriendsForCurrentUser()
    }

    suspend fun removeFriend(userName: String) {
        firebaseDataSource.removeFriendForCurrentUser(userName)
    }

    suspend fun validateExists(userName: String): Boolean {
        return firebaseDataSource.validateExists(userName)
    }

    suspend fun getPendingFriendsForCurrentUser(): List<String> {
        return firebaseDataSource.getIncomingFriendsForCurrentUser()
    }

    suspend fun removePending(userName: String) {
        firebaseDataSource.removePendingFrom(userName)
    }

    suspend fun addFriend(userName: String) {
        firebaseDataSource.addFriendForCurrentUser(userName)
    }

    suspend fun sendFriendRequest(userName: String) {
        firebaseDataSource.sendFriendRequestTo(userName)
    }

    suspend fun deleteOutgoingRequestTo(userName: String) {
        firebaseDataSource.removePendingTo(userName)

    }

    suspend fun getOutgoingForCurrentUser(): List<String> {
        return firebaseDataSource.getOutgoingForCurrentUser()
    }

    fun getCurrentUser(): String? {
        return firebaseDataSource.getCurrentUserName()
    }

    suspend fun isFriend(userName: String): Boolean {
        return firebaseDataSource.isFriend(userName)

    }

    suspend fun existsPending(userName: String): Boolean {
        return firebaseDataSource.existsPending(userName)
    }
}