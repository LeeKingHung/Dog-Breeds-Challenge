package com.example.dogbreedschallenge.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.example.dogbreedschallenge.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {

		// Edge-To-Edge config
		val navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
		enableEdgeToEdge(navigationBarStyle = navigationBarStyle)

		super.onCreate(savedInstanceState)

		setContent {

			AppTheme {
				RootContent()
			}

		}

	}

}

@Composable
private fun RootContent() {

}