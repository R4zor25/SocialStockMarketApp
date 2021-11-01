package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
    fun TopBar(title: String = "", buttonIcon: ImageVector?, scope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = {
            Text(
                text = title, fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = {  scope.launch { scaffoldState.drawerState.open() }}
                ){
                buttonIcon?.let { Icon(it, contentDescription = "") }
            }
        },
        backgroundColor = MyBlue,
        contentColor = Color.Black
    )
}

@Preview
@Composable
fun TopBarPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    TopBar("Navigation bar", Icons.Filled.Menu, scope, scaffoldState )
}