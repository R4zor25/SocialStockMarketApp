package hu.bme.aut.android.socialstockmarketapp.ui.stockconversation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.AuthInteractor
import hu.bme.aut.android.socialstockmarketapp.domain.ConversationInteractor
import hu.bme.aut.android.socialstockmarketapp.domain.model.ConversationComment
import io.finnhub.api.apis.DefaultApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StockConversationScreenViewModel @Inject constructor(
    private val conversationInteractor: ConversationInteractor,
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<StockConversationScreenViewState> = MutableStateFlow(StockConversationScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<StockConversationOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    val apiClient = DefaultApi()

    var companySymbol: String = ""


    init {
        coroutineScope.launch {
            _oneShotEvents.send(StockConversationOneShotEvent.AcquireSymbol)
        }
    }

    fun onAction(stockConversationUiAction: StockConversationUiAction) {
        when (stockConversationUiAction) {
            is StockConversationUiAction.OnInit -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    val conversationComments = conversationInteractor.getConversationForStock(companySymbol)
                    _oneShotEvents.send(StockConversationOneShotEvent.CommentsReceived(conversationComments))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
            is StockConversationUiAction.SendMessage -> {
                coroutineScope.launch(Dispatchers.IO) {
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    conversationInteractor.sendConversationComment(
                        ConversationComment(
                            authInteractor.getCurrentUserName().toString(),
                            stockConversationUiAction.message,
                            SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()),
                        ),
                        companySymbol
                    )
                    val conversationComments = conversationInteractor.getConversationForStock(companySymbol)
                    _oneShotEvents.send(StockConversationOneShotEvent.CommentsReceived(conversationComments))
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            }
        }
    }
}