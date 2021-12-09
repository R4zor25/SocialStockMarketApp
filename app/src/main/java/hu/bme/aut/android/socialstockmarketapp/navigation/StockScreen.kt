package hu.bme.aut.android.socialstockmarketapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

//All Composble Screen with it's navigation route
sealed class StockScreen (val route: String){
    object LoginScreen: StockScreen("login_screen")
    object RegistrationScreen: StockScreen("registration_screen")
    object StockListScreen: StockScreen("stocklist_screen")
    object CryptoStockListScreen: StockScreen("cryptostocklist_screen")
    object FriendListScreen: StockScreen("friendlist_screen")
    object StockNewsListScreen: StockScreen("stocknewslist_screen")
    object StockNewsDetailScreen: StockScreen("stocknewsdetail_screen/{newsUrl}")
    object StockDetailScreen: StockScreen("stockdetail_screen/{stockSymbol}")
    object CryptoDetailScreen: StockScreen("cryptodetail_screen/{cryptoSymbol}")
    object CompanyNewsScreen: StockScreen("companynews_screen/{companySymbol}")
    object StockSocialSentimentScreen: StockScreen("stocksocialsentiment_screen/{companySymbol}")
    object StockConversationScreen: StockScreen("stockconversation_screen/{companySymbol}")
    object StockGraphScreen: StockScreen("stockgraph_screen/{companySymbol}")
    object FollowedStocksScreen: StockScreen("followedstocks_screen/{userName}")
    object StockAdviceScreen: StockScreen("stockadvice_screen/{companySymbol}")
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
            label = "Crypto Currency",
            icon = Icons.Filled.Money,
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
    // value for distinguishing whose friendlist we need to get
    val empty= " "
    val NavigationDrawerItems = listOf(
        BottomNavItem(
            label = "Friends",
            icon = Icons.Filled.People,
            route = StockScreen.FriendListScreen.route
        ),
        BottomNavItem(
            label = "Followed Stocks",
            icon = Icons.Filled.Money,
            route = "followedstocks_screen/$empty"
        ),
        BottomNavItem(
            label = "Log out",
            icon = Icons.Filled.Logout,
            route = StockScreen.LoginScreen.route
        ),
    )
}