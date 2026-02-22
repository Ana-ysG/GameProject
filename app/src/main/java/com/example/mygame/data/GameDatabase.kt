package com.example.mygame.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import com.example.mygame.R

object GameDatabase {
    // Ressources
    val resourceList = listOf(
        Resource("green_herb", "Herbe Verte", "Une herbe médicinale", ResourceType.NATURE, sellValue = 3, buyValue = 5),
        Resource("commun_wood", "Bois Commun", "Bois commun", ResourceType.NATURE, sellValue = 8, buyValue = 10),
        Resource("magic_wand", "Baguette Magique", "Canalise le mana", ResourceType.MAGIC, sellValue = 50)
    )

    // Recettes
    val recipes = listOf(
        Recipe(
            id = "craft_wand",
            name = "Baguette Magique",
            productResourceId = "magic_wand",
            ingredients = mapOf("green_herb" to 2),
            manaCost = 20,
            isUnlocked = true
        ),
        Recipe(
            id = "test1",
            name = "bois",
            productResourceId = "commun_wood",
            ingredients = mapOf("green_herb" to 5),
            manaCost = 5,
            isUnlocked = true
        )
    )

    val machines = listOf(
        Machine(
            id = "cauldron",
            name = "Chaudron en Fonte",
            isUnlocked = true,
            availableActionsIds = listOf("craft_wand")
        ),
        Machine(
            id = "mac2",
            name = "machine 1",
            isUnlocked = true,
            availableActionsIds = listOf("craft_wand","test1")
        )
    )
    val questList = listOf(
        Quest(
            id = "q1",
            title = "Premier Pas d'Artisan",
            description = "Le forgeron du village a besoin de bois pour ses fourneaux.",
            state = QuestState.AVAILABLE, // La première quête est disponible
            requiredResources = mapOf("wood" to 5),
            rewardGold = 20,
            rewardResources = mapOf("mana_shard" to 1), // Récompense spéciale
        ),
        Quest(
            id = "q2",
            title = "Herboristerie",
            description = "L'apothicaire cherche des herbes fraîches pour ses potions.",
            state = QuestState.AVAILABLE,
            requiredResources = mapOf("green_herb" to 10),
            rewardGold = 50,
            nextQuestId = "q3"
        ),
        Quest(
            id = "q3",
            title = "Commande Urgente",
            description = "Le village a besoin de baguettes pour la fête des lumières.",
            state = QuestState.LOCKED,
            requiredResources = mapOf("magic_wand" to 3),
            rewardGold = 150,
        ),
    )

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
            lootTable = listOf(LootDrop("green_herb", 1, 1.0))
        ),
    )

    val productionAreas = listOf(
        ProductionArea(
            id = "A",
            name = "Zone A",
            iconResId = android.R.drawable.ic_menu_report_image,
            isUnlocked = true,
            availableActionsIds = listOf("wood_production", "meadow_production")
        )
    )

    val shop1 = Shop(
        id = "shop1",
        name = "Boutique du village",
        description = "Boutique du village",
        isUnlocked = true,
        availableActionsIds = listOf("green_herb","wood")
    )

    fun getResourceById(id: String) = resourceList.find { it.id == id }
    fun getIconForRessource(id : String) = getResourceById(id)?.icon ?: Icons.Default.Build
    fun getRecepie(id: String) = recipes.find { it.id == id }
    fun getProductionZoneById(id: String) = productionZones.find { it.id == id }
    fun getResouceName(id: String) = getResourceById(id)?.name ?: "Inconnu"
}
