package hu.bme.aut.android.socialstockmarketapp.ui.friends

data class FriendListScreenViewState(val isLoading: Boolean = false, var infoMessage: String?)

sealed class FriendListOneShotEvent {
    object FriendListReceived : FriendListOneShotEvent()
    object ShowToastMessage: FriendListOneShotEvent()
}

sealed class FriendListUiAction {
    class LoadFriends : FriendListUiAction()
    class RemoveFriend(val userName: String): FriendListUiAction()
    class SendFriendRequest(val userName: String): FriendListUiAction()
    class AcceptPending(val userName: String): FriendListUiAction()
    class RefusePending(val userName: String): FriendListUiAction()
    class DeleteOutgoingRequest(val userName: String): FriendListUiAction()
}