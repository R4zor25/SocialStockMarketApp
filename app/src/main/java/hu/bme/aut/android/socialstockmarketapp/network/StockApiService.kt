package hu.bme.aut.android.socialstockmarketapp.network

import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import javax.inject.Inject

class StockApiService @Inject constructor(
) {

    val API_KEY = "c5o81hqad3i92b40uth0"

    val sandbox_key = "sandbox_c5o81hqad3i92b40uthg"


    //ApiClient.apiKey["token"] = "c5o81hqad3i92b40uth0"
    val apiClient = DefaultApi()

    init {
        ApiClient.apiKey["token"] = "c5o81hqad3i92b40uth0"
    }

}
/*
    private val alphaVantageAPI: AlphaVantageAPI by lazy { retrofit.create(AlphaVantageAPI::class.java) }

    private val retrofit: Retrofit by lazy { Retrofit.Builder().baseUrl("https://www.alphavantage.co").addConverterFactory(MoshiConverterFactory.create()).build() }


    suspend fun getIntraDay(symbol: String, interval: String, adjusted: Boolean?, outputSize: String?)
    = alphaVantageAPI.getIntraDay(symbol = symbol, interval = interval,adjusted = adjusted, outputSize = outputSize)

    suspend fun save(email: String, passwd: String) {
        Log.d("TAGGA", "Siker: $email")
        delay(1000)
    }

 */