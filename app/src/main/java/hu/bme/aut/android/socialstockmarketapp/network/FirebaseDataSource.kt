package hu.bme.aut.android.socialstockmarketapp.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor() {
    private var db = Firebase.firestore
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storage = Firebase.storage.reference

    fun getCurrentUserEmail(): String{
        return firebaseAuth.currentUser?.email!!
    }

    fun getCurrentUserName(): String? {
        return firebaseAuth.currentUser?.displayName
    }

    suspend fun createUser(email: String, userName: String, password: String): String {
        var message: String = ""
        val isTaken = isUserNameTaken(userName)
        if(isTaken){
            return "Username already taken"
        }
        try{
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val firebaseUser = result.user
                    val profileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(userName)
                        .build()
                    firebaseUser?.updateProfile(profileChangeRequest)


                    addUserToDatabase(userName, email)
                    message = "Registration successful!"
                }
                .addOnFailureListener { exception ->
                    message = exception.localizedMessage

                }.await()

        }
        catch (e: Exception){
        }

        return message
    }
    private suspend fun isUserNameTaken(userName: String): Boolean {
        val query = db.collection("Users")
            .whereEqualTo("username", userName).get().await()
        return !query.isEmpty

    }

    private fun addUserToDatabase(userName: String?, email: String) {
        val data = hashMapOf(
            "username" to userName,
            "friends" to emptyList<String>(),
            "email" to email
        )
        db.collection("Users").add(data)
    }

    private suspend fun getUserEmail(userName: String): String{
        val query = db.collection("Users")
            .whereEqualTo("username", userName).get().await()
        if(query.isEmpty)
            throw RuntimeException("No such user")
        return query.documents[0]?.get("email") as String
    }

    suspend fun login(email: String, password: String): String {
        var message: String = ""
        try{
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    message = "Login successful!"
                }
                .addOnFailureListener { exception ->
                    message = exception.localizedMessage ?: "sasasa"
                }.await()
        }
        catch (e: Exception){
            return e.localizedMessage
        }

        return message
    }

    suspend fun getFriendsForCurrentUser(): List<String> {
        val userName = firebaseAuth.currentUser?.displayName
        val query = db.collection("Users")
            .whereEqualTo("username", userName).get().await()

        return query.documents[0]?.get("friends") as List<String>

    }

    suspend fun removeFriendForCurrentUser(userName: String) {
        val currUser = firebaseAuth.currentUser?.displayName
        deleteFriendFromSideOf(currUser!!, userName)
        deleteFriendFromSideOf(userName, currUser)
    }

    private suspend fun deleteFriendFromSideOf(userName: String, deleteeUserName: String){
        val userDoc = db.collection("Users")
            .whereEqualTo("username", userName)
            .get()
            .await()
            .documents[0]

        val friends = userDoc["friends"] as MutableList<String>
        friends.remove(deleteeUserName);

        db.collection("Users")
            .document(userDoc.id).update("friends", friends.toList())
            .await()
    }

    suspend fun addFriendForCurrentUser(userName: String) {
        val currUser = firebaseAuth.currentUser?.displayName
        addFriendFromSideOf(currUser!!, userName)
        addFriendFromSideOf(userName, currUser)
    }

    private suspend fun addFriendFromSideOf(userName: String, userToBeAdded: String) {
        val userDoc = db.collection("Users")
            .whereEqualTo("username", userName)
            .get()
            .await()
            .documents[0]

        val friends = userDoc["friends"] as MutableList<String>
        friends.add(userToBeAdded);

        db.collection("Users")
            .document(userDoc.id).update("friends", friends.toList())
            .await()
    }

    suspend fun getIncomingFriendsForCurrentUser(): List<String> {
        val userName = firebaseAuth.currentUser?.displayName
        val query = db.collection("FriendRequests")
            .whereEqualTo("to", userName).get().await()
        val list = mutableListOf<String>()
        for(doc in query){
            list.add(doc["from"] as String)
        }
        return list
    }

    suspend fun removePendingFrom(userName: String) {
        val currUser = firebaseAuth.currentUser?.displayName
        val id = db.collection("FriendRequests")
            .whereEqualTo("to", currUser)
            .whereEqualTo("from", userName)
            .get().await().documents[0].id

        db.collection("FriendRequests").document(id).delete().await()
    }

    fun sendFriendRequestTo(userName: String) {
        val currUser = firebaseAuth.currentUser?.displayName
        val data = hashMapOf(
            "to" to userName,
            "from" to currUser
        )
        db.collection("FriendRequests").add(data)
    }

    suspend fun removePendingTo(userName: String) {
        val currUser = firebaseAuth.currentUser?.displayName
        val id = db.collection("FriendRequests")
            .whereEqualTo("to", userName)
            .whereEqualTo("from", currUser)
            .get().await().documents[0].id

        db.collection("FriendRequests").document(id).delete().await()
    }

    suspend fun getOutgoingForCurrentUser(): List<String> {
        val userName = firebaseAuth.currentUser?.displayName
        val query = db.collection("FriendRequests")
            .whereEqualTo("from", userName).get().await()

        val list = mutableListOf<String>()
        for(doc in query){
            list.add(doc["to"] as String)
        }
        return list
    }

    suspend fun isFriend(userName: String) : Boolean{
        val currUser = firebaseAuth.currentUser?.displayName
        val query = db.collection("Users")
            .whereEqualTo("username", currUser)
            .whereArrayContains("friends", userName)
            .get().await()
        if(query.isEmpty)
            return false
        return true
    }

    suspend fun existsPending(userName: String): Boolean {
        val currUser = firebaseAuth.currentUser?.displayName
        val query1 = db.collection("FriendRequests")
            .whereEqualTo("from", currUser)
            .whereEqualTo("to", userName)
            .get().await()
        val query2 = db.collection("FriendRequests")
            .whereEqualTo("to", currUser)
            .whereEqualTo("from", userName)
            .get().await()
        if(query1.isEmpty && query2.isEmpty)
            return false
        return true

    }

    suspend fun validateExists(userName: String): Boolean {
        val userDoc = db.collection("Users")
            .whereEqualTo("username", userName)
            .get()
            .await()
        if(userDoc.isEmpty) return false
        return true
    }
}