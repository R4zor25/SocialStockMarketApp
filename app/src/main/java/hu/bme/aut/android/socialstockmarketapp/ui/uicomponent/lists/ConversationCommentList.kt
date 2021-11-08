package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialstockmarketapp.domain.model.ConversationComment
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationCommentList(
    comments: MutableList<ConversationComment>,
    listState: LazyListState
) {
    LazyColumn(state = listState, modifier = Modifier.padding(20.dp)) {
        items(comments) { comment ->
            ConversationCommentRowItem(comment)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
@Preview
fun ConversationCommentRowItemPreview() {
    ConversationCommentRowItem(
        ConversationComment("Minta User",
        "EZ asdajrhgaioluserhfushmrg  ghiujterghn iousvbutir bjnoiurtb juirtjb oriutejb"
        , "2021-01-01 15:22")
    )
}

@Composable
fun ConversationCommentRowItem(
    comment: ConversationComment
) {
    Column(modifier = Modifier
        .background(MyBlue, shape = RoundedCornerShape(20.dp))
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(

        ) {
            Text(text = comment.userName, fontSize = 18.sp,
                fontWeight = FontWeight(700), modifier = Modifier.padding(start = 12.dp))
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        ) {
            Text(text = comment.date, fontSize = 18.sp, fontWeight = FontWeight(700))
        }
    }
        Row(){
            Text(text = comment.message, fontSize = 18.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.padding(start = 12.dp))
        }
}
    }