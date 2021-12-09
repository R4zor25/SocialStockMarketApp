package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

import io.finnhub.api.models.CompanyProfile2
import io.finnhub.api.models.Quote

data class StockDetailScreenViewState(val isLoading: Boolean = false, val isDataAvailable: Boolean = false)

sealed class StockDetailOneShotEvent{
    data class CompanyInfoReceived(val companyProfile2: CompanyProfile2, val contains: Boolean): StockDetailOneShotEvent()
    data class QuoteInfoReceived(val quote: Quote): StockDetailOneShotEvent()
    object AcquireSymbol: StockDetailOneShotEvent()
    data class ShowToastMessage(val toastMessage: String): StockDetailOneShotEvent()
    object FollowSuccessful : StockDetailOneShotEvent()
    object UnfollowSuccessful : StockDetailOneShotEvent()
}

sealed class StockDetailUiAction{
    class OnInit(): StockDetailUiAction()
}