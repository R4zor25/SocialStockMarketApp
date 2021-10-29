package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun SpinnerView(dropDownList: MutableList<String>, onSpinnerItemSelected : (String) -> Unit, spinnerTitle: String) {
    var sampleName: String by remember { mutableStateOf(dropDownList[0]) }
    var expanded by remember { mutableStateOf(false) }
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(targetState = transitionState, label = "transition")
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = 300)
    }, label = "rotationDegree") {
        if (expanded) 180f else 0f
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = spinnerTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier
                        .clickable {
                            expanded = !expanded
                        }, horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = sampleName,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(end = 8.dp, start = 8.dp)
                                    .height(30.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Spinner",
                                modifier = Modifier.rotate(arrowRotationDegree)
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {
                                dropDownList.forEach { data ->
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            sampleName = data
                                            onSpinnerItemSelected(data)
                                        }
                                    ) {
                                        Text(text = data)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}