package com.digiventure.fido_exploration.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.digiventure.fido_exploration.pages.AuthenticationPage
import com.digiventure.fido_exploration.pages.register.RegisterPage
import com.digiventure.fido_exploration.pages.register.RegisterViewModel

@Composable
fun NavGraph(navHostController: NavHostController, registerViewModel: RegisterViewModel) {
    NavHost(
        navController = navHostController,
        startDestination = Route.AuthenticationPage.routeName
    ) {
        composable(Route.AuthenticationPage.routeName) {
            AuthenticationPage(navHostController = navHostController)
        }
        composable(Route.RegisterPage.routeName) {
            RegisterPage(navHostController = navHostController, viewModel = registerViewModel)
        }
    }
}