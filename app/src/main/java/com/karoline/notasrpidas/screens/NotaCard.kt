package com.karoline.notasrpidas.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.karoline.notasrpidas.data.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun NotaCard(
    nota: Nota,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit,
    onCheckChange: (Boolean) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {

                onEdit(nota.id)
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = nota.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = nota.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = nota.isDone,
                    onCheckedChange = { isChecked ->

                        onCheckChange(isChecked)

                        val notaRef = db.collection("notas").document(nota.id)
                        notaRef.update("isDone", isChecked)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Status da nota atualizado com sucesso.")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Erro ao atualizar o status da nota", e)
                            }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurface,
                        checkmarkColor = MaterialTheme.colorScheme.surface
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {

                    val notaRef = db.collection("notas").document(nota.id)
                    notaRef.delete()
                        .addOnSuccessListener {
                            onDelete()
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Erro ao excluir nota", e)
                        }
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        }
    }
}

