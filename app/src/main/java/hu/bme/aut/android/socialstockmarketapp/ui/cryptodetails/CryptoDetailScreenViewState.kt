package hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails

import io.finnhub.api.models.CryptoCandles

data class CryptoDetailScreenViewState(val isLoading: Boolean = false, val dataAvailable: Boolean = false, val redrawNeeded: Boolean = false)

sealed class CryptoDetailOneShotEvent {
    data class StockCandlesDataReceived(val cryptoCandles: CryptoCandles) : CryptoDetailOneShotEvent()
    object AcquireSymbol : CryptoDetailOneShotEvent()
}

sealed class CryptoDetailUiAction {
    object OnInit : CryptoDetailUiAction()
    class RefreshGraphData(val resolution: String, val startDateTimeStamp: Long, val endDateTimeStamp: Long) : CryptoDetailUiAction()
    class RedrawGraphData() : CryptoDetailUiAction()
}