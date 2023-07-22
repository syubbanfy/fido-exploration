package com.digiventure.fido_exploration.navigation

sealed class Route(val routeName: String) {
    object AuthenticationPage: Route(routeName = "authentication_page")
    object RegisterPage: Route(routeName = "register_page")
}