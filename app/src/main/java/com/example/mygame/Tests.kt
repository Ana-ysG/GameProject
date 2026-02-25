package com.example.mygame

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mygame.data.ActionEntity
import com.example.mygame.data.LootDrop
import com.example.mygame.ui.PopUp.SelectionDialog


@Preview(showBackground = true)
@Composable
fun PlaceholderPreview() {
    val productionZones = listOf(
        ActionEntity.ProductionZone(
            id = "wood_production",
            name = "Forêt des Murmures",
            isUnlocked = true,
            previousTierId = "meadow_production",
            nextTierId = "deep_forest_production", // Ce sera la zone "bloquée"
            lootTable = listOf(LootDrop("green_herb", 1, 1.0),LootDrop("commun_wood", 1, 0.50))
        ),
        ActionEntity.ProductionZone(
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


