package hu.bme.aut.android.socialstockmarketapp.uitests

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import hu.bme.aut.android.socialstockmarketapp.navigation.StockScreen
import hu.bme.aut.android.socialstockmarketapp.ui.LoginActivity
import hu.bme.aut.android.socialstockmarketapp.ui.auth.login.LoginScreen
import hu.bme.aut.android.socialstockmarketapp.ui.auth.registration.RegistrationScreen
import hu.bme.aut.android.socialstockmarketapp.ui.companynews.CompanyNewsScreen
import hu.bme.aut.android.socialstockmarketapp.ui.cryptodetails.CryptoDetailScreen
import hu.bme.aut.android.socialstockmarketapp.ui.cryptostocklist.CryptoStockListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.followedstocks.FollowedStocksScreen
import hu.bme.aut.android.socialstockmarketapp.ui.friends.FriendListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stockadvice.StockAdviceScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stockconversation.StockConversationScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stockdetail.StockDetailScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stockgraph.StockGraphScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stocklist.StockListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stocknewsdetail.StockNewsDetailScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stocknewslist.StockNewsListScreen
import hu.bme.aut.android.socialstockmarketapp.ui.stocksocialsentiment.StockSocialSentimentScreen
import hu.bme.aut.android.socialstockmarketapp.util.EspressoIdlingResource
import kotlinx.coroutines.InternalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class UITests {


    private lateinit var navController: TestNavHostController

    @OptIn(InternalCoroutinesApi::class)
    @get:Rule
    val composeTestRule = createAndroidComposeRule(LoginActivity::class.java)


    @OptIn(ExperimentalAnimationApi::class, kotlinx.coroutines.InternalCoroutinesApi::class)
    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            MaterialTheme() {
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
                        ) { backStackEntry ->
                            val stockSymbol = backStackEntry.arguments?.getString("stockSymbol")
                            StockDetailScreen(navController, stockSymbol)
                        }
                        composable(
                            route = StockScreen.CryptoDetailScreen.route
                        ){ backStackEntry ->
                            val cryptoSymbol = backStackEntry.arguments?.getString("cryptoSymbol")
                            CryptoDetailScreen(navController, cryptoSymbol!!)
                        }
                        composable(
                            route = StockScreen.CompanyNewsScreen.route
                        ){ backStackEntry ->
                            val companySymbol = backStackEntry.arguments?.getString("companySymbol")
                            CompanyNewsScreen(navController, companySymbol!!)
                        }
                        composable(
                            route = StockScreen.StockSocialSentimentScreen.route
                        ){ backStackEntry ->
                            val companySymbol = backStackEntry.arguments?.getString("companySymbol")
                            StockSocialSentimentScreen(navController, companySymbol!!)
                        }
                        composable(
                            route = StockScreen.StockConversationScreen.route
                        ){ backStackEntry ->
                            val companySymbol = backStackEntry.arguments?.getString("companySymbol")
                            StockConversationScreen(navController, companySymbol!!)
                        }
                        composable(
                            route = StockScreen.StockGraphScreen.route
                        ){ backStackEntry ->
                            val companySymbol = backStackEntry.arguments?.getString("companySymbol")
                            StockGraphScreen(navController, companySymbol!!)
                        }
                        composable(
                            route = StockScreen.StockAdviceScreen.route
                        ){ backStackEntry ->
                            val companySymbol = backStackEntry.arguments?.getString("companySymbol")
                            StockAdviceScreen(navController, companySymbol!!)
                        }
                        composable(
                            route = StockScreen.FollowedStocksScreen.route
                        ) { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("userName")
                            FollowedStocksScreen(navController, userName!!)
                        }
                    }
                }
            }
        }
    }


    @OptIn(InternalCoroutinesApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
    @Test
    fun LoginTest() {
        composeTestRule.onNodeWithText("Login").performClick()
        onView(withText("One or more text fields are empty!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("Email").performTextInput("demetermate@gmail.com")
        Espresso.pressBack()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Login").performClick()
        onView(withText("One or more text fields are empty!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("Password").performTextInput("WrongPassword")
        Espresso.pressBack()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Login").performClick()
        onView(withText("The password is invalid or the user does not have a password.")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("WrongPassword").performTextInput("testtest")
        composeTestRule.onNodeWithText("Login").performClick()
        Thread.sleep(3500)
        Espresso.pressBack()
    }


    @OptIn(InternalCoroutinesApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
    @Test
    fun RegistrationTest() {
        composeTestRule.onNodeWithText("Registration").performClick()
        // Start the app
        composeTestRule.onNodeWithText("Register").performClick()
        onView(withText("One or more text fields are empty!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("Username").performTextInput("Minta User")
        Espresso.pressBack()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Register").performClick()
        onView(withText("One or more text fields are empty!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("Email").performTextInput("demetermate@gmail.com")
        Espresso.pressBack()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Register").performClick()
        onView(withText("One or more text fields are empty!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("Password").performTextInput("WrongPassword")
        Espresso.pressBack()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Register").performClick()
        onView(withText("Username already taken!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("Minta User").performTextClearance()
        composeTestRule.onNodeWithText("Username").performTextInput("Minta User1")
        Espresso.pressBack()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Register").performClick()
        onView(withText("The email address is already in use by another account.")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
    }

    @OptIn(InternalCoroutinesApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
    @Test
    fun FriendTest() {
        // Check number of matched nodes
        composeTestRule
            .onAllNodesWithContentDescription("Beatle").assertCountEquals(4)
// At least one matches
        composeTestRule
            .onAllNodesWithContentDescription("Beatle").assertAny(hasTestTag("Drummer"))
// All of them match
        composeTestRule
            .onAllNodesWithContentDescription("Beatle").assertAll(hasClickAction())



        composeTestRule.onNodeWithText("Sign In").performClick()
        Thread.sleep(3000)
        composeTestRule.onNodeWithContentDescription("TestMenu").performClick()
        composeTestRule.onNodeWithText("Friends").performClick()
        Thread.sleep(3500)
        composeTestRule.onNodeWithText("Add Friend").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Username").performTextInput("Not Existing user")
        composeTestRule.onNodeWithText("OK").performClick()
        onView(withText("User does not exist with this name!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Add Friend").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Username").performTextInput("Minta User")
        composeTestRule.onNodeWithText("OK").performClick()
        onView(withText("You cannot add yourself!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Add Friend").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Username").performTextInput("user2")
        composeTestRule.onNodeWithText("OK").performClick()
        onView(withText("The user is already your friend!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Add Friend").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Username").performTextInput("user1")
        composeTestRule.onNodeWithText("OK").performClick()
        onView(withText("The request is already pending!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Add Friend").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Username").performTextInput("user3")
        composeTestRule.onNodeWithText("OK").performClick()
        onView(withText("The request is already pending!")).inRoot(withDecorView(not(composeTestRule.activity.window.decorView))).check(matches(isDisplayed()))
    }
}