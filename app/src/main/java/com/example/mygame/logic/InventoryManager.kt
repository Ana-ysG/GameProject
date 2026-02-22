package com.example.mygame.logic

import androidx.compose.runtime.mutableStateMapOf

data class InventoryData(
    // Une Map standard (pas de mutableStateMap ici, c'est le GameState qui gère le State)
    val items: Map<String, Long> = emptyMap()
) {
    // Ajouter des ressources : renvoie un nouvel InventoryData
    fun addResource(resourceId: String, amount: Long): InventoryData {
        val currentAmount = items[resourceId] ?: 0L
        return this.copy(items = items + (resourceId to currentAmount + amount))
    }

    // Retirer des ressources : renvoie le nouvel inventaire OU null si pas assez de stock
    fun removeResource(resourceId: String, amount: Long): InventoryData? {
        val currentAmount = items[resourceId] ?: 0L
        if (currentAmount < amount) return null

        return this.copy(items = items + (resourceId to currentAmount - amount))
    }

    // Vérifier la quantité
    fun getAmount(resourceId: String): Long = items[resourceId] ?: 0L

    // Vérifier si on a assez pour une liste d'ingrédients (Utile pour le craft !)
    fun hasResources(requirements: Map<String, Long>): Boolean {
        return requirements.all { (id, qty) -> getAmount(id) >= qty }
    }
}
