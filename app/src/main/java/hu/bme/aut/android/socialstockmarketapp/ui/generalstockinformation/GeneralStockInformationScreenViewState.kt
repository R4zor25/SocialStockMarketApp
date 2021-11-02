package hu.bme.aut.android.socialstockmarketapp.ui.generalstockinformation

import io.finnhub.api.models.SocialSentiment

data class GeneralStockInformationScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class GeneralStockInformationOneShotEvent{
    data class GeneralStockInformationReceived(val socialSentiment : SocialSentiment): GeneralStockInformationOneShotEvent()
    object ShowToastMessage: GeneralStockInformationOneShotEvent()
}

sealed class GeneralStockInformationUiAction{
    class OnInit():GeneralStockInformationUiAction()
}