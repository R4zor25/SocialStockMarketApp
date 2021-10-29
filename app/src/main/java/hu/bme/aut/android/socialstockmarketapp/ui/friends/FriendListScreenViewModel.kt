package hu.bme.aut.android.socialstockmarketapp.ui.friends

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialstockmarketapp.domain.AuthInteractor
import hu.bme.aut.android.socialstockmarketapp.domain.FriendInteractor
import hu.bme.aut.android.socialstockmarketapp.network.StockApiService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListScreenViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val stockApiService: StockApiService,
    private val friendInteractor: FriendInteractor
) : ViewModel() {

    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<FriendListScreenViewState> = MutableStateFlow(FriendListScreenViewState(infoMessage = ""))
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<FriendListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    var friendList = ArrayList<String>()

    var pendingFriendList = ArrayList<String>()

    var outgoingFriendList = ArrayList<String>()


    init {
        onAction(FriendListUiAction.LoadFriends())
    }




    fun onAction(friendListUiAction: FriendListUiAction) {
        when (friendListUiAction) {
            is FriendListUiAction.LoadFriends -> {
                coroutineScope.launch {
                    _viewState.value =  _viewState.value.copy(isLoading = true)
                    friendList = friendInteractor.getFriendsForCurrentUser() as ArrayList<String>
                    pendingFriendList = friendInteractor.getPendingFriendsForCurrentUser() as ArrayList<String>
                    outgoingFriendList = friendInteractor.getOutgoingForCurrentUser() as ArrayList<String>
                    _oneShotEvents.send(FriendListOneShotEvent.DataReceived)
                    _viewState.value =  _viewState.value.copy(isLoading = false)
                }
            }
            is FriendListUiAction.SendFriendRequest -> {
                coroutineScope.launch {
                    _viewState.value =  _viewState.value.copy(isLoading = true)
                    val exists = friendInteractor.validateExists(friendListUiAction.userName)
                    val currentUser = friendInteractor.getCurrentUser()
                    val existsPending = friendInteractor.existsPending(friendListUiAction.userName)
                    when {
                        currentUser == friendListUiAction.userName -> {
                            viewState.value.infoMessage = "You cannot add yourself!"
                            _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage)
                        }
                        containsUserName(friendListUiAction.userName, friendList) -> {
                            viewState.value.infoMessage = "The user is already your friend!"
                            _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage)
                        }
                        existsPending -> {
                            viewState.value.infoMessage = "The request is already pending!"
                            _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage)
                        }
                        exists -> {
                            friendInteractor.sendFriendRequest(friendListUiAction.userName)
                            viewState.value.infoMessage = "Friend Request sent!"
                            _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage)
                            onAction(FriendListUiAction.LoadFriends())
                        }
                        else -> {
                            viewState.value.infoMessage = "User does not exist with this name!"
                            _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage)
                        }
                    }
                    _viewState.value =  _viewState.value.copy(isLoading = false)
                }
            }
            is FriendListUiAction.RemoveFriend -> {
                coroutineScope.launch {
                    _viewState.value =  _viewState.value.copy(isLoading = true)
                    friendInteractor.removeFriend(userName = friendListUiAction.userName)
                    pendingFriendList = friendInteractor.getPendingFriendsForCurrentUser() as ArrayList<String>
                    _oneShotEvents.send(FriendListOneShotEvent.DataReceived)
                    _viewState.value =  _viewState.value.copy(isLoading = false)
                }
            }
            is FriendListUiAction.AcceptPending -> {
                coroutineScope.launch {
                    _viewState.value =  _viewState.value.copy(isLoading = true)
                    friendInteractor.removePending(userName = friendListUiAction.userName)
                    friendInteractor.addFriend(userName = friendListUiAction.userName)
                    pendingFriendList = friendInteractor.getPendingFriendsForCurrentUser() as ArrayList<String>
                    friendList = friendInteractor.getFriendsForCurrentUser() as ArrayList<String>
                    _oneShotEvents.send(FriendListOneShotEvent.DataReceived)
                    _viewState.value =  _viewState.value.copy(isLoading = false)
                }
            }
            is FriendListUiAction.RefusePending -> {
                coroutineScope.launch {
                    _viewState.value =  _viewState.value.copy(isLoading = true)
                    friendInteractor.removePending(userName = friendListUiAction.userName)
                    pendingFriendList = friendInteractor.getPendingFriendsForCurrentUser() as ArrayList<String>
                    _oneShotEvents.send(FriendListOneShotEvent.DataReceived)
                    _viewState.value =  _viewState.value.copy(isLoading = false)
                }
            }
            is FriendListUiAction.DeleteOutgoingRequest -> {
                coroutineScope.launch {
                    _viewState.value =  _viewState.value.copy(isLoading = true)
                    friendInteractor.deleteOutgoingRequestTo(userName = friendListUiAction.userName)
                    outgoingFriendList = friendInteractor.getOutgoingForCurrentUser() as ArrayList<String>
                    _oneShotEvents.send(FriendListOneShotEvent.DataReceived)
                    _viewState.value =  _viewState.value.copy(isLoading = false)
                }
            }
        }
    }

    private fun containsUserName(userName: String ,list :MutableList<String>): Boolean{
        for(item in list){
            if(item == userName)
                return true
        }
        return false
    }
}