package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import io.finnhub.api.models.CompanyNews
import io.finnhub.api.models.MarketNews
import java.time.Instant
import java.time.ZoneId

@Composable
fun CompanyNewsList(companyNewsList: List<CompanyNews>, onRowItemClick: (String) -> Unit, listState : LazyListState) {
    LazyColumn(state = listState) {
        items(companyNewsList) { companyNews ->
            CompanyNewsRowItem(companyNews, onRowItemClick)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CompanyNewsRowItem(companyNews: CompanyNews, onRowItemClick: (String) -> Unit) {
    Surface(modifier = Modifier
        .clickable { onRowItemClick(companyNews.url.toString()) }
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .fillMaxWidth(),
        color = MyBlue,
        shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)) {
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column() {
                Row() {
                    Text(text = companyNews.headline.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp))
                }
                Row {
                    Column(
                        modifier = Modifier
                            .background(Color.Transparent)
                    ) {
                        Image(
                            painter = rememberImagePainter(companyNews.image.toString()),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(100.dp)
                                .padding(start = 12.dp, top = 12.dp)
                        )
                        Text(text = "Category: " + companyNews.category.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp, top = 8.dp))
                        Text(text = "Source: " + companyNews.source.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp))
                        Text(text = Instant.ofEpochSecond(companyNews.datetime!!)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime().toString().replace("T", " "), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp))
                    }
                    Column() {
                        Text(text = companyNews.summary.toString(), fontSize = 16.sp, modifier = Modifier.padding(top = 12.dp, start = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun CompanyNewsRowItemPreview() {
    StockNewsRowItem(
        onRowItemClick = {}, stockNews = MarketNews(
            headline = "Square surges after reporting 64% jump in revenue, more customers using Cash App",
            image = "https://image.cnbcfm.com/api/v1/image/105569283-1542050972462rts25mct.jpg?v=1542051069",
            summary = "Shares of Square soared on Tuesday evening after posting better-than-expected quarterly results and strong growth in its consumer payments app.",
            category = "technology",
            source = "CNBC"
        )
    )
}