package com.karoline.notasrpidas.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.karoline.notasrpidas.data.Nota
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var notas by remember { mutableStateOf<List<Nota>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    val topBarColor = Color(0xFFc2b6f0)
    val fabColor = Color(0xFFae87df)

    LaunchedEffect(Unit) {
        try {
            val result = db.collection("notas").get().await()
            notas = result.documents.map { document ->
                val id = document.id
                val title = document.getString("title") ?: ""
                val description = document.getString("description") ?: ""
                val isDone = document.getBoolean("isDone") ?: false

                Nota(id, title, description, isDone)
            }
        } catch (e: Exception) {
            Log.w("Firestore", "Erro ao carregar notas: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notas App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add") },
                modifier = Modifier.padding(16.dp),
                containerColor = fabColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Nota")
            }
        },
        content = { paddingValues ->

            val backgroundColor = MaterialTheme.colorScheme.background
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(backgroundColor)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(backgroundColor)
                ) {
                    items(notas) { nota ->
                        NotaCard(
                            nota = nota,
                            onEdit = {
                                navController.navigate("edit/${nota.id}")
                            },
                            onDelete = {
                                val notaRef = db.collection("notas").document(nota.id)
                                notaRef.delete()
                                    .addOnSuccessListener {
                                        notas = notas.filter { it.id != nota.id }
                                        Log.d("Firestore", "Nota excluída com sucesso.")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Firestore", "Erro ao excluir nota", e)
                                    }
                            },
                            onCheckChange = { isChecked ->
                                val notaRef = db.collection("notas").document(nota.id)
                                notaRef.update("isDone", isChecked)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Status da nota atualizado com sucesso.")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Firestore", "Erro ao atualizar status da nota", e)
                                    }
                            }
                        )
                    }
                }
            }
        }
    )
}

