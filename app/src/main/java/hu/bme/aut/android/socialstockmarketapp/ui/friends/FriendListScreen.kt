package hu.bme.aut.android.socialstockmarketapp.ui.friends

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialstockmarketapp.R
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.AddFriendDialog
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists.FriendList
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@OptIn(ExperimentalComposeUiApi::class)
@InternalCoroutinesApi
@Composable
fun FriendListScreen(navController: NavController) {
    val friendListScreenViewModel = hiltViewModel<FriendListScreenViewModel>()
    SocialStockMarketAppTheme {
        Box {
            MainCardFriendList(navController, friendListScreenViewModel)
        }
    }
}

@ExperimentalComposeUiApi
@InternalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun FriendListScreenPreview() {
    Box {
        MainCardFriendList(
            navController = NavController(LocalContext.current),
            viewModel = hiltViewModel<FriendListScreenViewModel>()
        )
    }
}


@ExperimentalComposeUiApi
@InternalCoroutinesApi
@Composable
fun MainCardFriendList(navController: NavController, viewModel: FriendListScreenViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current
    var showCustomDialogWithResult by remember { mutableStateOf(false) }
    var friends by rememberSaveable { mutableStateOf(arrayListOf<String>()) }
    var pendingRequests by rememberSaveable { mutableStateOf(arrayListOf<String>()) }
    var outgoingRequests by rememberSaveable { mutableStateOf(arrayListOf<String>()) }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                   is FriendListOneShotEvent.FriendListReceived -> {
                        friends = viewModel.friendList
                        pendingRequests = viewModel.pendingFriendList
                        outgoingRequests = viewModel.outgoingFriendList
                    }
                    FriendListOneShotEvent.ShowToastMessage -> {
                        Toast.makeText(context, viewState.value.infoMessage, Toast.LENGTH_LONG).show()
                    }
                    else -> { }
                }
            }
            .collect()
    }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { showCustomDialogWithResult = !showCustomDialogWithResult },
                text = { Text(text = stringResource(R.string.add_friend)) },
                icon = { Icon(Icons.Filled.Add, "") })
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (showCustomDialogWithResult) {
                AddFriendDialog(
                    onDismiss = { showCustomDialogWithResult = !showCustomDialogWithResult },
                    onNegativeClick = { showCustomDialogWithResult = !showCustomDialogWithResult },
                    onPositiveClick = { userName ->
                        showCustomDialogWithResult = !showCustomDialogWithResult
                        viewModel.onAction(FriendListUiAction.SendFriendRequest(userName))
                    }
                )
            }
            if (viewState.value.isLoading)
                CircularIndeterminateProgressBar(isDisplayed = true)
            else {
                Text(text = stringResource(R.string.friends), color = Color.Black, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp), fontSize = 18.sp)
                Row(modifier = Modifier.weight(0.3f)) {
                    FriendList(
                        items = friends,
                        isPending = false,
                        alreadyFriend = true,
                        onAcceptPending = { viewModel.onAction(FriendListUiAction.AcceptPending(it)) },
                        onRefusePending = { viewModel.onAction(FriendListUiAction.RefusePending(it)) },
                        onDeleteFriend = { viewModel.onAction(FriendListUiAction.RemoveFriend(it)) },
                        openFollowedStocks = { navController.navigate("followedstocks_screen/$it") }
                    )
                }
            }
            if (viewState.value.isLoading)
                CircularIndeterminateProgressBar(isDisplayed = true)
            else {
                Text(text = stringResource(R.string.incoming_friend_requests), color = Color.Black, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp), fontSize = 18.sp)
                Row(modifier = Modifier.weight(0.3f)) {
                    FriendList(items = pendingRequests,
                        isPending = true,
                        alreadyFriend = false,
                        onAcceptPending = { viewModel.onAction(FriendListUiAction.AcceptPending(it)) },
                        onRefusePending = { viewModel.onAction(FriendListUiAction.RefusePending(it)) },
                        onDeleteFriend = { viewModel.onAction(FriendListUiAction.RemoveFriend(it)) },
                        openFollowedStocks = { navController.navigate("followedstocks_screen/$it") })
                }
            }
            if (viewState.value.isLoading)
                CircularIndeterminateProgressBar(isDisplayed = true)
            else {
                Text(text = stringResource(R.string.pending_friend_requests), color = Color.Black, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp), fontSize = 18.sp)
                Row(modifier = Modifier.weight(0.3f)) {
                    FriendList(items = outgoingRequests,
                        isPending = false,
                        alreadyFriend = false,
                        onAcceptPending = { viewModel.onAction(FriendListUiAction.AcceptPending(it)) },
                        onRefusePending = { viewModel.onAction(FriendListUiAction.RefusePending(it)) },
                        onDeleteFriend = { viewModel.onAction(FriendListUiAction.DeleteOutgoingRequest(it)) },
                        openFollowedStocks = { navController.navigate("followedstocks_screen/$it") })
                }
            }
        }
    }
}