package com.oechslerbernardo.mongodbteste

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.oechslerbernardo.mongodbteste.data.repository.MongoRepositoryImpl
import com.oechslerbernardo.mongodbteste.navigation.root.NavGraph
import com.oechslerbernardo.mongodbteste.navigation.root.Routes
import com.oechslerbernardo.mongodbteste.presentation.home.HomeViewModel
import com.oechslerbernardo.mongodbteste.ui.theme.MongoDbTesteTheme
import com.oechslerbernardo.mongodbteste.util.Constants.APP_ID
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: HomeViewModel = hiltViewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState(initial = false)
            Log.d("ThemeSwitch", "isDarkTheme: $isDarkTheme")
            MongoDbTesteTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app: App = App.create(APP_ID)
                    // Determine if the user is logged in
                    val startDestination = if (app.currentUser != null) {
                        Routes.HomeGraphRoute.route
                    } else {
                        Routes.AuthGraphRoute.route
                    }
                    NavGraph(
                        navController = rememberNavController(),
                        startDestination = startDestination,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}