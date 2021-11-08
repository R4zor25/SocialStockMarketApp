package hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails

import io.finnhub.api.models.CryptoCandles

data class CryptoDetailScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class CryptoDetailOneShotEvent{
    data class StockCandlesDataReceived(val cryptoCandles: CryptoCandles): CryptoDetailOneShotEvent()
    object ShowToastMessage: CryptoDetailOneShotEvent()
    object AcquireSymbol: CryptoDetailOneShotEvent()
}

sealed class CryptoDetailUiAction{
    class OnInit(): CryptoDetailUiAction()
    class RefreshGraphData(val resolution: String, val startDateTimeStamp: Long, val endDateTimeStamp: Long): CryptoDetailUiAction()
}