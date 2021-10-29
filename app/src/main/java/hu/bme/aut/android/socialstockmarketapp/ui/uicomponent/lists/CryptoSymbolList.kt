package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import io.finnhub.api.models.CryptoSymbol

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CryptoSymbolList(cryptoSymbolList: List<CryptoSymbol>, onGridItemClicked: (String) -> Unit, listState: LazyListState ){
    LazyVerticalGrid(state = listState, cells = GridCells.Adaptive(minSize = 100.dp)) {
        items(cryptoSymbolList) { cryptoSymbol ->
            CryptoSymbolGridItem(cryptoSymbol, onGridItemClicked)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun CryptoSymbolGridItem(cryptoSymbol: CryptoSymbol, onGridItemClicked: (String) -> Unit){
Surface(modifier = Modifier
    .clickable { onGridItemClicked(cryptoSymbol.symbol.toString()) }
    .padding(horizontal = 8.dp, vertical = 8.dp),
    color = MyBlue,
    shape = RoundedCornerShape(40.dp)) {

    Column() {
        Text(text = cryptoSymbol.displaySymbol.toString(), fontSize = 16.sp, modifier =  Modifier.padding(horizontal = 8.dp, vertical =  8.dp))
    }
}
}


@Composable
@Preview
fun CryptoSymbolGridItemPreview() {
    CryptoSymbolGridItem(
        onGridItemClicked = {}, cryptoSymbol = CryptoSymbol(
            description = "Binance ETHBTC",
            displaySymbol = "ETH/BTC",
            symbol = "ETHBTC"
        )
    )
}