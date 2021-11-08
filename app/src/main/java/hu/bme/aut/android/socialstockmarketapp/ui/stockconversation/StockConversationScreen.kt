package hu.bme.aut.android.socialstockmarketapp.ui.stockconversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.domain.model.ConversationComment
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.ConversationCommentList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun StockConversationScreen(navController: NavController, companySymbol: String){
    val stockConversationScreenViewModel = hiltViewModel<StockConversationScreenViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val viewState = stockConversationScreenViewModel.viewState.collectAsState()
    var comments by rememberSaveable { mutableStateOf(mutableListOf<ConversationComment>()) }
    var message by rememberSaveable { mutableStateOf("")}

    LaunchedEffect("key") {
        stockConversationScreenViewModel.oneShotEvent
            .onEach {
                when (it) {
                    StockConversationOneShotEvent.AcquireSymbol -> {
                        stockConversationScreenViewModel.companySymbol = companySymbol
                        stockConversationScreenViewModel.onAction(StockConversationUiAction.OnInit())
                    }
                    is StockConversationOneShotEvent.CommentsReceived -> {
                        comments = it.conversationComments
                    }
                    else -> {

                    }
                }
            }
            .collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = { TopBar("Conversation about $companySymbol Stock", buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.weight(0.1f)) {
                    TextField(value = message, onValueChange = {message = it})
                }
                Row(modifier = Modifier.weight(0.1f)) {
                    Button(onClick = { stockConversationScreenViewModel.onAction(StockConversationUiAction.SendMessage(message)) }) {
                    }
                }
                Row(modifier = Modifier.weight(0.8f)) {
                    ConversationCommentList(comments, listState)
                }
            }
        }
    )
}