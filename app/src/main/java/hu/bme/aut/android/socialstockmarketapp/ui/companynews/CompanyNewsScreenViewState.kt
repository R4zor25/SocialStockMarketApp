package hu.bme.aut.android.socialstockmarketapp.ui.companynews

import io.finnhub.api.models.CompanyNews

data class CompanyNewsScreenViewState(val isLoading: Boolean = false)

sealed class CompanyNewsOneShotEvent {
    data class CompanyNewsReceived(val companyNews: List<CompanyNews>) : CompanyNewsOneShotEvent()
    object AcquireSymbol : CompanyNewsOneShotEvent()
}

sealed class CompanyNewsUiAction {
    object OnInit : CompanyNewsUiAction()
}