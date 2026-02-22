package com.example.mygame.ui.PopUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygame.data.LootDrop
import com.example.mygame.data.ProductionZone
import com.example.mygame.data.SelectableEntity
import com.example.mygame.ui.components.SelectionGrid
import com.example.mygame.ui.theme.GameText.PopUpTitle

/*
SelectionGrid(
                options = options,
                onSelect = onSelect as (SelectableEntity) -> Unit
            )
 */

@Composable
fun SelectionDialog(
    title: String,
    options: List<SelectableEntity>,
    onSelect: (SelectableEntity) -> Unit, // Retrait du @Composable ici
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { PopUpTitle(text = title) },
        text = {
            // Utilisation de ton nouveau composant générique
            SelectionGrid(
                options = options,
                onSelect = { entity ->
                    onSelect(entity) // On exécute l'action
                    // Optionnel : onDismiss() si tu veux fermer après sélection
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SelectionDialogPreview() {
    val productionZones = listOf(
        ProductionZone(
            id = "wood_production",
            name = "Forêt des Murmures",
            isUnlocked = true,
            previousTierId = "meadow_production",
            nextTierId = "deep_forest_production", // Ce sera la zone "bloquée"
            lootTable = listOf(LootDrop("green_herb", 1, 1.0),LootDrop("commun_wood", 1, 0.50))
        ),
        ProductionZone(
            id = "meadow_production",
            name = "Plaine verdoiante",
            isUnlocked = true,
            nextTierId = "wood_production", // Ce sera la zone "bloquée"
            lootTable = listOf(LootDrop("green_herb", 1, 1.0),LootDrop("commun_wood", 1, 0.50))
        ),
    )
    // 1. On crée le conteneur de fond (l'écran bleu)
    SelectionDialog(
        title = "Voyager vers...",
        options = productionZones, // Liste mixte de ProductionZone et ShopZone
        onSelect = { selectedZone -> {}
        },
        onDismiss = { }
    )
}