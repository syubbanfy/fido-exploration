package com.digiventure.fido_exploration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.digiventure.fido_exploration.navigation.NavGraph
import com.digiventure.fido_exploration.pages.register.RegisterViewModel
import com.digiventure.fido_exploration.pages.register.RegisterViewModelFactory
import com.digiventure.fido_exploration.ui.theme.FidoexplorationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var registerViewModelFactory: RegisterViewModelFactory

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModel = ViewModelProvider(this, registerViewModelFactory)[RegisterViewModel::class.java]

        setContent {
            FidoexplorationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.secondary
                ) {
                    NavGraph(
                        navHostController = rememberNavController(),
                        registerViewModel = registerViewModel
                    )
                }
            }
        }
    }
}