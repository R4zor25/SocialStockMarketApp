package hu.bme.aut.android.socialstockmarketapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.socialstockmarketapp.navigation.StockScreen
import hu.bme.aut.android.socialstockmarketapp.ui.auth.login.LoginScreen
import hu.bme.aut.android.socialstockmarketapp.ui.auth.registration.RegistrationScreen
import hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails.CryptoDetailScreen
import hu.bme.aut.android.socialstockmarketapp.ui.cryptostocklist.CryptoStockListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.friends.FriendListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.mystocklist.MyStockListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stockdetail.StockDetailScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stocklist.StockListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stocknewsdetail.StockNewsDetailScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stocknewslist.StockNewsListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.theme.SocialStockMarketAppTheme
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialStockMarketAppTheme() {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = StockScreen.LoginScreen.route
                    ) {
                        composable(
                            route = StockScreen.LoginScreen.route
                        ) {
                            LoginScreen(navController)
                        }
                        composable(
                            route = StockScreen.RegistrationScreen.route
                        ) {
                            RegistrationScreen(navController)
                        }
                        composable(
                            route = StockScreen.StockListScreen.route
                        ) {
                            StockListScreen(navController)
                        }
                        composable(
                            route = StockScreen.CryptoStockListScreen.route
                        ) {
                            CryptoStockListScreen(navController)
                        }
                        composable(
                            route = StockScreen.MyStockListScreen.route
                        ) {
                            MyStockListScreen(navController)
                        }
                        composable(
                            route = StockScreen.FriendListScreen.route
                        ) {
                            FriendListScreen(navController)
                        }
                        composable(
                            route = StockScreen.StockNewsListScreen.route
                        ) {
                            StockNewsListScreen(navController)
                        }
                        composable(
                            route = StockScreen.StockNewsDetailScreen.route
                        ) { backStackEntry ->
                            val newsUrl = backStackEntry.arguments?.getString("newsUrl") ?: "google.com"
                            StockNewsDetailScreen(navController, newsUrl)
                        }
                        composable(
                            route = StockScreen.StockDetailScreen.route
                        ) {
                            StockDetailScreen(navController)
                        }
                        composable(
                            route = StockScreen.CryptoDetailScreen.route
                        ){ backStackEntry ->
                            val cryptoSymbol = backStackEntry.arguments?.getString("cryptoSymbol")
                            CryptoDetailScreen(navController, cryptoSymbol)
                        }
                    }
                }
            }
        }
    }
}
