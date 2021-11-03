package hu.bme.aut.android.socialstockmarketapp.ui.generalstockinformation

import io.finnhub.api.models.BasicFinancials

data class GeneralStockInformationScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class GeneralStockInformationOneShotEvent{
    data class BasicFinancialsDataReceived(val basicFinancials : BasicFinancials): GeneralStockInformationOneShotEvent()
    object ShowToastMessage: GeneralStockInformationOneShotEvent()
    object AcquireSymbol: GeneralStockInformationOneShotEvent()
}

sealed class GeneralStockInformationUiAction{
    class OnInit():GeneralStockInformationUiAction()
}