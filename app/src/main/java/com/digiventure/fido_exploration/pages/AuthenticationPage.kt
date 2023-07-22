package com.digiventure.fido_exploration.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.digiventure.fido_exploration.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationPage(navHostController: NavHostController,) {
    Scaffold(
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                ) {
                    Button(onClick = { navHostController.navigate(Route.RegisterPage.routeName) }) {
                        Text(text = "Go to Register Page")
                    }
                }
            }
        }
    )
}