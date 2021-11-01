package hu.bme.aut.android.socialstockmarketapp.ui.stockdetail

import io.finnhub.api.models.CompanyProfile2

data class StockDetailScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockDetailOneShotEvent{
    data class CompanyInfoReceived(val companyProfile2: CompanyProfile2): StockDetailOneShotEvent()
    object AcquireSymbol: StockDetailOneShotEvent()
    object ShowToastMessage: StockDetailOneShotEvent()
}

sealed class StockDetailUiAction{
    class OnInit(): StockDetailUiAction()
}