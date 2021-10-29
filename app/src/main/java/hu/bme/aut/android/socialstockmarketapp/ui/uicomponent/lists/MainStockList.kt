package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import io.finnhub.api.models.StockSymbol

@Composable
fun MainStockList(stockSymbolList: List<StockSymbol>, onRowItemClick: (String) -> Unit) {
    LazyColumn() {
        items(stockSymbolList) { stockSymbol ->
            StockListRowItem(onRowItemClick, stockSymbol)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun StockListRowItem(onRowItemClick: (String) -> Unit, stockSymbol: StockSymbol) {
    Surface(modifier = Modifier
        .clickable { onRowItemClick(stockSymbol.symbol.toString()) }
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .fillMaxWidth(),
        color = MyBlue,
        shape = RoundedCornerShape(40.dp)) {
        Row(
            modifier = Modifier
                .background(Color.Transparent, shape = RoundedCornerShape(40.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.background(Color.Transparent)) {
                Text(text = stockSymbol.description.toString() + " (${stockSymbol.symbol})", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp))
                Text(text = "Stock type : ${stockSymbol.type.toString()}", modifier = Modifier.padding(start = 12.dp))
                Text(text = "Currency : ${stockSymbol.currency.toString()}", modifier = Modifier.padding(start = 12.dp))
            }

        }
    }
}

@Composable
@Preview
fun StockListRowItemPreview() {
    StockListRowItem(onRowItemClick = {}, stockSymbol = StockSymbol("RABA AUTOMOTIVE HOLDING PLC", "", "RABA.BD", "Common Stock", "", "", "HUF"))
}
