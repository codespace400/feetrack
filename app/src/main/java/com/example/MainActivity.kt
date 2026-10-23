package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.FeeTrackApp
import com.example.ui.FeeTrackViewModel
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.FeeTrackTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      FeeTrackTheme {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
          color = BackgroundSlate
        ) {
          val feeTrackViewModel: FeeTrackViewModel = viewModel()
          FeeTrackApp(viewModel = feeTrackViewModel)
        }
      }
    }
  }
}
