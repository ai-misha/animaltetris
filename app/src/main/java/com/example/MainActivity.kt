package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.GameScreen
import com.example.ui.GameViewModel
import com.example.ui.theme.LocalGameColors
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
                viewModel.pauseGame()
            }
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            MyApplicationTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LocalGameColors.current.bg
                ) {
                    GameScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseGame()
    }

    override fun onStop() {
        super.onStop()
        viewModel.pauseGame()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

