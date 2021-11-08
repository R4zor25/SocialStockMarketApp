package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FollowedStocksList(stockSymbols: List<String>, onGridItemClicked: (String) -> Unit){
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 100.dp)) {
        items(stockSymbols) { stockSymbol ->
            FollowedStocksRowItem(stockSymbol, onGridItemClicked)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun FollowedStocksRowItem(stockSymbol: String, onGridItemClicked: (String) -> Unit) {
    Surface(modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .clickable { onGridItemClicked(stockSymbol.toString()) },
        color = MyBlue,
        shape = RoundedCornerShape(40.dp)) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stockSymbol.toString(), fontSize = 16.sp, modifier =  Modifier.padding(horizontal = 8.dp, vertical =  8.dp))
        }
    }
}