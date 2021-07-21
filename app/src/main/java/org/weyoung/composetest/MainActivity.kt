package org.weyoung.composetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.lachlanmckee.hilt.compose.navigation.factory.addNavigation
import net.lachlanmckee.hilt.compose.navigation.factory.hiltNavGraphNavigationFactories
import org.weyoung.composetest.ui.theme.ComposeTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                JetpackNavigationHiltApp()
            }
        }
    }
}

@Composable
fun JetpackNavigationHiltApp() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            val context = LocalContext.current
            NavHost(navController, startDestination = "home") {
                hiltNavGraphNavigationFactories(context).addNavigation(this, navController)
            }
        },
        topBar = {
            TopAppBar{
                IconButton(
                    onClick = {
                        scope.launch { scaffoldState.drawerState.open() }
                    }
                ) {
                    Icon(Icons.Filled.Menu,"Hamburger")
                }
            }
        },
        drawerContent = {
            Column {
                NavigationButton(
                    navController = navController,
                    route = "home",
                    label = "Home",
                    drawerState = scaffoldState.drawerState
                )
                NavigationButton(
                    navController = navController,
                    route = "feature1",
                    label = "Particle",
                    drawerState = scaffoldState.drawerState
                )
                NavigationButton(
                    navController = navController,
                    route = "feature2",
                    label = "Counter",
                    drawerState = scaffoldState.drawerState
                )
            }
        }
    )
}

@Composable
fun NavigationButton(
    navController: NavController,
    route: String,
    label: String,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    TextButton(
        onClick = {
            navController.navigate(route){
                popUpTo("home")
                launchSingleTop = true
            }
            scope.launch {
                drawerState.close()
            }
        },
        content = {
            Text(label, fontSize = 30.sp)
        }
    )
}