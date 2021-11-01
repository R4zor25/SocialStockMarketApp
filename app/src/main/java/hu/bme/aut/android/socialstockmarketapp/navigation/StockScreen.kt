package hu.bme.aut.android.socialstockmarketapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class StockScreen (val route: String){
    object LoginScreen: StockScreen("login_screen")
    object RegistrationScreen: StockScreen("registration_screen")
    object StockListScreen: StockScreen("stocklist_screen")
    object CryptoStockListScreen: StockScreen("cryptostocklist_screen")
    object MyStockListScreen: StockScreen("mystocklist_screen")
    object FriendListScreen: StockScreen("friendlist_screen")
    object StockNewsListScreen: StockScreen("stocknewslist_screen")
    object StockNewsDetailScreen: StockScreen("stocknewsdetail_screen/{newsUrl}")
    object StockDetailScreen: StockScreen("stockdetail_screen/{stockSymbol}")
    object CryptoDetailScreen: StockScreen("cryptodetail_screen/{cryptoSymbol}")
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)

object BottomNavItems {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Stocks",
            icon = Icons.Filled.Home,
            route = StockScreen.StockListScreen.route
        ),
        BottomNavItem(
            label = "CryptoCurrency",
            icon = Icons.Filled.Search,
            route = StockScreen.CryptoStockListScreen.route
        ),
        BottomNavItem(
            label = "News",
            icon = Icons.Filled.Book,
            route = StockScreen.StockNewsListScreen.route
        )
    )
}

object NavigationDrawerItems {
    val NavigationDrawerItems = listOf(
        BottomNavItem(
            label = "Friends",
            icon = Icons.Filled.People,
            route = StockScreen.FriendListScreen.route
        ),
        BottomNavItem(
            label = "Log out",
            icon = Icons.Filled.Logout,
            route = StockScreen.LoginScreen.route
        )
    )
}