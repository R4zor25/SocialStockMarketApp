package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hu.bme.aut.android.socialstockmarketapp.navigation.BottomNavItems
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue

@Composable
fun BottomNavigationBar(navController: NavController) {
  
    BottomNavigation(
        
        // set background color
        backgroundColor = MyBlue
    ) {
  
        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()
  
        // observe current route to change the icon 
          // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route
  
        // Bottom nav items we declared
        BottomNavItems.BottomNavItems.forEach { navItem ->
  
            // Place the bottom nav items
            BottomNavigationItem(
                    
                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,
  
                // navigate on click
                onClick = {
                    navController.navigate(navItem.route){
                        navController.popBackStack()
                        popUpTo("login_screen"){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                
                // Icon of navItem
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                },
                
                // label
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = false
            )
        }
    }
}