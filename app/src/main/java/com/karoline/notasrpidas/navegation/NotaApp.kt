package com.karoline.notasrpidas.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.karoline.notasrpidas.screens.AddNoteScreen
import com.karoline.notasrpidas.screens.EditNoteScreen
import com.karoline.notasrpidas.screens.MainScreen


@Composable
fun NotasApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController = navController)
        }
        composable("add") {
            AddNoteScreen(navController = navController)
        }
        composable("edit/{notaId}") { backStackEntry ->
            val notaId = backStackEntry.arguments?.getString("notaId") ?: ""
            EditNoteScreen(navController, notaId)
        }
    }
}
