package com.example.ui_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.feature_user.UserDetailViewModel
import com.example.ui_compose.screens.user_list.UserDetailScreen
import com.example.ui_compose.screens.user_list.UserListScreen
import com.yourname.github.ui.compose.theme.GitHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        UserListScreen(onUserClick = { login ->
                            navController.navigate("detail/$login")
                        })
                    }
                    composable(
                        "detail/{${UserDetailViewModel.USER_NAME}}",
                        arguments = listOf(navArgument(UserDetailViewModel.USER_NAME) {
                            type = NavType.StringType
                        }
                        )) {
                        UserDetailScreen()
                    }
                }
            }
        }
    }
}