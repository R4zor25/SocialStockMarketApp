package hu.bme.aut.android.socialstockmarketapp.ui.stockadvice

import io.finnhub.api.models.CompanyNews

data class StockAdviceScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class StockAdviceOneShotEvent{
    data class CompanyNewsReceived(val companyNews : List<CompanyNews>): StockAdviceOneShotEvent()
    object ShowToastMessage: StockAdviceOneShotEvent()
}

sealed class StockAdviceUiAction{
    class OnInit(): StockAdviceUiAction()
    class SpinnerSelected(val spinnerName: String): StockAdviceUiAction()
}