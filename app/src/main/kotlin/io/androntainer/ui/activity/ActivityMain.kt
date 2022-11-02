package io.androntainer.ui.activity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.androntainer.ui.screen.ScreenHome

/**
 * Jetpack Compose Layout
 */
@Composable
fun ActivityMain(
    title: String,
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
) {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                ScreenHome(
                    title = title,
                    targetAppName = targetAppName,
                    targetAppPackageName = targetAppPackageName,
                    targetAppDescription = targetAppDescription,
                    targetAppVersionName = targetAppVersionName,
                    NavigationOnClick = NavigationOnClick,
                    MenuOnClick = MenuOnClick,
                    SearchOnClick = SearchOnClick,
                    SheetOnClick = SheetOnClick,
                    AppsOnClick = AppsOnClick,
                    SelectOnClick = SelectOnClick,
                    onNavigateToApps = { navController.navigate("apps") },
                )
            }
            composable("apps") {

            }
        }

    }
}