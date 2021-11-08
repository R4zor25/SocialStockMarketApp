package hu.bme.aut.android.socialstockmarketapp.domain.model

data class Conversation(val userName: String = "", val Comments: MutableList<HashMap<String, String>> = mutableListOf())