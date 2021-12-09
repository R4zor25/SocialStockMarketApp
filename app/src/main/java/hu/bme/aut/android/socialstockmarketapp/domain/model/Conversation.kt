package hu.bme.aut.android.socialstockmarketapp.domain.model

//Helper model class for Firebase Datastore
data class Conversation(val stockSymbol: String = "", val Comments: MutableList<HashMap<String, String>> = mutableListOf())