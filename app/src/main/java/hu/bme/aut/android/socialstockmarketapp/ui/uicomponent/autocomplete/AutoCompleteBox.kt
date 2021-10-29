package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.autocomplete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val AutoCompleteBoxTag = "AutoCompleteBox"

@ExperimentalAnimationApi
@Composable
fun <T : AutoCompleteEntity> AutoCompleteBox(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    content: @Composable AutoCompleteScope<T>.() -> Unit
) {
    val autoCompleteState = remember { mutableStateOf(AutoCompleteState(startItems = items)) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        autoCompleteState.value.content()
        AnimatedVisibility(visible = autoCompleteState.value.isSearching) {
            LazyColumn(
                modifier = Modifier.autoComplete(autoCompleteState.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(autoCompleteState.value.filteredItems) { item ->
                    Box(modifier = Modifier.clickable { autoCompleteState.value.selectItem(item) }) {
                        itemContent(item)
                    }
                }
            }
        }
    }
}

private fun Modifier.autoComplete(
    autoCompleteItemScope: AutoCompleteDesignScope
): Modifier = composed {
    val baseModifier = if (autoCompleteItemScope.shouldWrapContentHeight)
        wrapContentHeight()
    else
        heightIn(0.dp, autoCompleteItemScope.boxMaxHeight)

    baseModifier
        .testTag(AutoCompleteBoxTag)
        .fillMaxWidth(autoCompleteItemScope.boxWidthPercentage)
        .border(
            border = autoCompleteItemScope.boxBorderStroke,
            shape = autoCompleteItemScope.boxShape
        )
}