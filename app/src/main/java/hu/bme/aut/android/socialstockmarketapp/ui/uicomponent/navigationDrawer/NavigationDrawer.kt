package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent.navigationDrawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.socialstockmarketapp.navigation.NavigationDrawerItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(navController: NavController, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // List of navigation items
        NavigationDrawerItems.NavigationDrawerItems.forEach { item ->

            NavigationDrawerRowItem(item, false) {
                coroutineScope.launch { scaffoldState.drawerState.close() }
                navController.navigate(item.route) }

        }
    }
}

@Preview
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    NavigationDrawer(NavController(LocalContext.current), scaffoldState, scope)
}