package com.karoline.notasrpidas.data

import androidx.compose.ui.geometry.Offset

data class Nota(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false,
    val userId: String = "",
)

 {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "description" to description,
            "isDone" to isDone,
            "userId" to userId
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Nota {
            return Nota(
                title = map["title"] as? String ?: "",
                description = map["description"] as? String ?: "",
                isDone = map["isDone"] as? Boolean ?: false,
                userId = map["userId"] as? String ?: ""
            )
        }
    }
}
