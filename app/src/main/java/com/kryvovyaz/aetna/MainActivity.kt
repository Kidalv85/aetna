package com.kryvovyaz.aetna

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.kryvovyaz.aetna.screens.AetnaTheme
import com.kryvovyaz.aetna.screens.CharacterScreen
import com.kryvovyaz.aetna.viewmodel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: CharacterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Vlad","activity on create")
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        actionBar?.hide()
        installSplashScreen()
        viewModel.onSearchQueryChange("rick")

        setContent {
            AetnaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Using viewModel() factory function from Compose
                    CharacterScreen(
                        text = viewModel.state,
                        onTopBarIconClick = { finish() }

                    )
                }
            }
        }
    }
}