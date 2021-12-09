package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendList(
    items: ArrayList<String>,
    isPending: Boolean,
    alreadyFriend: Boolean,
    onAcceptPending: (String) -> Unit,
    onRefusePending: (String) -> Unit,
    onDeleteFriend: (String) -> Unit,
    openFollowedStocks: (String) -> Unit,
) {
    LazyColumn {
        items(items) { item ->
            FriendListRowItem(userName = item, isPending, alreadyFriend, onAcceptPending, onRefusePending, onDeleteFriend, openFollowedStocks)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
@Preview
fun FriendListPreview() {
    FriendList(arrayListOf<String>("Friend1", "Friend2", "Friend3", "Friend4"), false, true, {}, {}, {}, {})
}

@Composable
fun FriendListRowItem(
    userName: String,
    isPending: Boolean,
    alreadyFriend: Boolean,
    onAcceptPending: (String) -> Unit,
    onRefusePending: (String) -> Unit,
    onDeleteFriend: (String) -> Unit,
    openFollowedStocks: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .background(MyBlue, shape = RoundedCornerShape(40.dp))
            .fillMaxWidth()
            .height(50.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.People, contentDescription = "", modifier = Modifier.padding(12.dp, 0.dp), tint = Color.Black)
        Text(text = userName, fontSize = 18.sp, fontWeight = FontWeight(700), color = Color.Black)
        if (isPending) {
            Spacer(modifier = Modifier.padding(start = 20.dp))
            Button(modifier = Modifier
                .height(40.dp),
                onClick = { onAcceptPending(userName) },
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Green
                ),
                content = {
                    Text(text = "Accept", color = Color.Black)
                })
            Spacer(modifier = Modifier.padding(start = 20.dp))
            Button(modifier = Modifier
                .height(40.dp),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red
                ),
                onClick = { onRefusePending(userName) }, content = {
                    Text(text = "Decline", color = Color.Black)
                })
        } else {
            if(alreadyFriend){
                Column(horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(end = 12.dp, start = 20.dp)) {
                    Button(modifier = Modifier
                        .height(40.dp),
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.LightGray
                        ),
                        onClick = { openFollowedStocks(userName) }, content = {
                            Text(text = "Followed Stocks", color = Color.Black)
                        })
                }
            }
            Column(horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth().padding(end = 12.dp)) {
                Button(modifier = Modifier
                    .height(40.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red
                    ),
                    onClick = { onDeleteFriend(userName) }, content = {
                        Text(text = "Delete", color = Color.Black)
                    })
            }
        }
    }
}
