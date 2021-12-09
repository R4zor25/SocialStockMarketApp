package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue

@Composable
fun StockPriceLineGraph(lines: MutableList<DataPoint>, valueText: String) {
    var price by remember { mutableStateOf(0f) }
    Column() {
        LineGraph(
            plot = LinePlot(
                listOf(
                    LinePlot.Line(
                        lines.toList(),
                        LinePlot.Connection(color = Color.Red),
                        LinePlot.Intersection(color = MyBlue, radius = 2.dp),
                        LinePlot.Highlight(color = Color.Yellow),
                    )
                ),
                grid = LinePlot.Grid(Color.Magenta, steps = 10),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .height(300.dp),
            onSelection = { xLine, points ->
                price = points[0].y
            }
        )
        Text(text = "$valueText: $price", color = Color.Black)
    }
}