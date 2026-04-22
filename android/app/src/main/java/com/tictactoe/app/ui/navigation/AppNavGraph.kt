package com.tictactoe.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tictactoe.app.ui.auth.AuthViewModel
import com.tictactoe.app.ui.auth.LoginScreen
import com.tictactoe.app.ui.auth.RegisterScreen
import com.tictactoe.app.ui.game.GameScreen
import com.tictactoe.app.ui.history.HistoryScreen
import com.tictactoe.app.ui.rooms.CreateRoomScreen
import com.tictactoe.app.ui.rooms.RoomListScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object RoomList : Screen("rooms")
    object CreateRoom : Screen("create_room")
    object Game : Screen("game/{roomId}") {
        fun createRoute(roomId: Int) = "game/$roomId"
    }
    object History : Screen("history")
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()

    val startDestination = if (authState.isLoggedIn) Screen.RoomList.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.RoomList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.RoomList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.RoomList.route) {
            RoomListScreen(
                onNavigateToCreateRoom = { navController.navigate(Screen.CreateRoom.route) },
                onNavigateToGame = { roomId ->
                    navController.navigate(Screen.Game.createRoute(roomId))
                },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CreateRoom.route) {
            CreateRoomScreen(
                onRoomCreated = { roomId ->
                    // Pop CreateRoom off the stack so its ViewModel is destroyed,
                    // then navigate to Game. Back from Game → fresh RoomList.
                    navController.navigate(Screen.Game.createRoute(roomId)) {
                        popUpTo(Screen.CreateRoom.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Game.route,
            arguments = listOf(navArgument("roomId") { type = NavType.IntType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getInt("roomId") ?: return@composable
            GameScreen(
                roomId = roomId,
                onBack = {
                    navController.navigate(Screen.RoomList.route) {
                        popUpTo(Screen.RoomList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
    }
}
