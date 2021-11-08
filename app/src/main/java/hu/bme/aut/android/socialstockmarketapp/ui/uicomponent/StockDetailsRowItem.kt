package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue

@Composable
fun StockDetailRowItem(title: String, onRowItemClick: () -> Unit) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(MyBlue)
        .clickable { onRowItemClick() }) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MyBlue), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Icon(imageVector = Icons.Filled.ArrowRight , contentDescription = "")
        }
    }

    
}

@Preview
@Composable
fun StockDetailsRowItemPreview(){
    StockDetailRowItem(title = "Company related news", onRowItemClick = {})
}