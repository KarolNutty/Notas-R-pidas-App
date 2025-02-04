package com.karoline.notasrpidas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import com.karoline.notasrpidas.navegation.NotasApp
import com.karoline.notasrpidas.ui.theme.NotasRápidasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) // Inicializa o Firebase

        setContent {

            NotasRápidasTheme {
                NotasApp()

            }
        }
    }
}

