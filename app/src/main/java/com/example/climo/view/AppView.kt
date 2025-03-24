package com.example.climo.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.climo.R
import com.example.climo.alerts.view.AlertView
import com.example.climo.favourites.view.FavouritesView
import com.example.climo.home.view.HomeView
import com.example.climo.settings.view.SettingsView
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.RobotoRegular
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimoApp() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isSplashScreen = currentRoute == NavigationRoutes.Splash::class.qualifiedName
    Scaffold(
        topBar = {
            if (!isSplashScreen) {
                TopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = colorResource(R.color.white),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    modifier = Modifier.height(60.dp),
                )
            }
        }
    ) { paddingValues ->
        ModalNavigationDrawer(
            drawerContent = { if (!isSplashScreen) DrawerContent(navController, drawerState) },
            drawerState = drawerState,
            gesturesEnabled = !isSplashScreen
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GradientBackground)
                    .padding(paddingValues)
            ) {
                NavigationGraph(navController)
            }
        }
    }
}

@Composable
private fun DrawerContent(navController: NavHostController,drawerState: DrawerState){
    ModalDrawerSheet(modifier = Modifier
        .padding(top = 20.dp)
        .background(color = colorResource(R.color.white))) {
        val scope = rememberCoroutineScope()
        Image(
            painter = painterResource(R.drawable.nav_photo),
            contentDescription = stringResource(R.string.nav_photo),
            modifier = Modifier.size(280.dp)
        )
        Column(modifier = Modifier.padding(top=20.dp,start=10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)){

            Row(){
                Image(
                        painter = painterResource(R.drawable.home_icon),
                        contentDescription = stringResource(R.string.home),
                        modifier = Modifier
                            .size(50.dp)
                            .padding(10.dp)
                )
                Text(
                        text = stringResource(R.string.home),
                        color = colorResource(R.color.black),
                        fontSize = 20.sp,
                        fontFamily = RobotoRegular,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                navController.navigate(NavigationRoutes.Home)
                                scope.launch { drawerState.close() }
                            }
                )
            }

            Row(){
                Image(
                    painter = painterResource(R.drawable.fav_icon),
                    contentDescription = stringResource(R.string.favourites),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(10.dp)
                )
                Text(
                    text = stringResource(R.string.favourites),
                    color = colorResource(R.color.black),
                    fontSize = 20.sp,
                    fontFamily = RobotoRegular,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            navController.navigate(NavigationRoutes.Favourites)
                            scope.launch { drawerState.close() }
                        }
                )
            }

            Row(){
                Image(
                    painter = painterResource(R.drawable.alerts_icon),
                    contentDescription = stringResource(R.string.alerts),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(10.dp)
                )
                Text(
                    text = stringResource(R.string.alerts),
                    color = colorResource(R.color.black),
                    fontSize = 20.sp,
                    fontFamily = RobotoRegular,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            navController.navigate(NavigationRoutes.Alerts)
                            scope.launch { drawerState.close() }
                        }
                )
            }

            Row(){
                Image(
                    painter = painterResource(R.drawable.settings_icon),
                    contentDescription = stringResource(R.string.settings),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(10.dp)
                )
                Text(
                    text = stringResource(R.string.settings),
                    color = colorResource(R.color.black),
                    fontSize = 20.sp,
                    fontFamily = RobotoRegular,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            navController.navigate(NavigationRoutes.Settings)
                            scope.launch { drawerState.close() }
                        }
                )
            }
        }
    }
}

@Composable
private fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationRoutes.Splash) {
        composable<NavigationRoutes.Splash> { SplashScreenView(navController) }
        composable<NavigationRoutes.Home> { HomeView() }
        composable<NavigationRoutes.Settings> { SettingsView() }
        composable<NavigationRoutes.Alerts> { AlertView() }
        composable<NavigationRoutes.Favourites> { FavouritesView() }
    }
}
