package hu.bme.aut.android.socialstockmarketapp.ui.companynews

import io.finnhub.api.models.CompanyNews

data class CompanyNewsScreenViewState(val isLoading: Boolean = false, var errorText: String?)

sealed class CompanyNewsOneShotEvent{
    data class CompanyNewsReceived(val companyNews : List<CompanyNews>): CompanyNewsOneShotEvent()
    object ShowToastMessage: CompanyNewsOneShotEvent()
}

sealed class CompanyNewsUiAction{
    class OnInit(): CompanyNewsUiAction()
    class SpinnerSelected(val spinnerName: String): CompanyNewsUiAction()
}