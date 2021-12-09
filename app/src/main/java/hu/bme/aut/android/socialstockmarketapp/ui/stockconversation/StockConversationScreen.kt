package hu.bme.aut.android.socialstockmarketapp.ui.stockconversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.domain.model.ConversationComment
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.TopBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.ConversationCommentList
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer.NavigationDrawer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun StockConversationScreen(navController: NavController, companySymbol: String){
    val stockConversationScreenViewModel = hiltViewModel<StockConversationScreenViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val viewState = stockConversationScreenViewModel.viewState.collectAsState()
    var comments by rememberSaveable { mutableStateOf(mutableListOf<ConversationComment>()) }
    var message by rememberSaveable { mutableStateOf("")}
    val localFocusManager = LocalFocusManager.current

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
                    else -> { }
                }
            }
            .collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.stock_conversation), buttonIcon = Icons.Filled.Menu, scope, scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            Row(modifier = Modifier
                .background(Color.White)
                .padding(bottom = 12.dp)
                ) {
                OutlinedTextField(modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(), value = message, onValueChange = { message = it },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = MyBlue,
                        unfocusedIndicatorColor = MyBlue,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        trailingIconColor = Color.Black
                    ),
                    label = { Text(text = stringResource(R.string.message), color = Color.Black) },
                    trailingIcon = {
                        if(message.isNotBlank()) {
                            IconButton(onClick = {
                                if (message.isNotBlank()) {
                                    stockConversationScreenViewModel.onAction(StockConversationUiAction.SendMessage(message))
                                    message = ""
                                    localFocusManager.clearFocus()
                                }
                            })
                            {
                                Icon(imageVector = Icons.Filled.Send, contentDescription = stringResource(R.string.send))
                            }
                        }
                    })
            }
        },
        content = {
            Surface(color = Color.White) {
                if (!viewState.value.isLoading) {
                    Column() {
                        Row(modifier = Modifier.padding(bottom = 50.dp)) {
                            ConversationCommentList(comments, listState)
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize().background(Color.White), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularIndeterminateProgressBar(isDisplayed = true)
                    }
                }
            }
        }
    )
}