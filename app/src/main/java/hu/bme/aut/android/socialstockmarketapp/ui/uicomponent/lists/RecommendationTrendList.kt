package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import io.finnhub.api.models.RecommendationTrend

@Composable
fun RecommendationTrendList(recommendationTrendList: List<RecommendationTrend>, listState : LazyListState) {
    LazyColumn(state = listState) {
        items(recommendationTrendList) { recommendationTrend ->
            RecommendationTrendRowItem(recommendationTrend)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RecommendationTrendRowItem(recommendationTrend: RecommendationTrend) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 12.dp)
    ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Surface(shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp), color = MyBlue) {
                    Text(
                        recommendationTrend.period.toString(), fontSize = 19.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp), color = Color.Black
                    )
                }
            }
            Column(
                modifier = Modifier
                    .background(MyBlue)
                    .fillMaxWidth()
            ) {
                Row() {
                    Column() {
                        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                            Text(text = "Hold: ", color = Color.Black, fontSize = 16.sp)
                            Text(text = " ${recommendationTrend.hold}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                            Text(text = "Buy: ", color = Color.Black, fontSize = 16.sp)
                            Text(text = " ${recommendationTrend.buy}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                            Text(text = "Strong buy: ", color = Color.Black, fontSize = 16.sp)
                            Text(text = " ${recommendationTrend.strongBuy}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                    Column() {
                        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                            Text(text = "Sell: ", color = Color.Black, fontSize = 16.sp)
                            Text(text = " ${recommendationTrend.sell}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                            Text(text = "Strong sell: ", color = Color.Black, fontSize = 16.sp)
                            Text(text = " ${recommendationTrend.strongSell}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
        }