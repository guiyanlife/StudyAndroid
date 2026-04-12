package com.github.studyandroid.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.studyandroid.app.ui.user.detail.UserDetailScreen
import com.github.studyandroid.app.ui.user.list.UserListScreen

/**
 * 导航宿主
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = UserListDestination,
        modifier = modifier
    ) {
        composable<UserListDestination> {
            UserListScreen(onNavigateToDetail = { userId ->
                navController.navigate(UserDetailDestination(userId))
            })
        }
        composable<UserDetailDestination> {
            UserDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
