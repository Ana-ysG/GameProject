package com.example.mygame.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import com.example.mygame.R
import kotlin.collections.find

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
        ActionEntity.Machine(
            id = "cauldron",
            name = "Chaudron en Fonte",
            isUnlocked = true,
            availableActionsIds = listOf("craft_wand")
        ),
        ActionEntity.Machine(
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

    // Les Ennemis de la "Forêt Débutante"
    val enemySlime = Enemy(
        id = "en_slime_green",
        name = "Slime Gluant",
        hp = 30,
        attack = 5,
        defense = 2,
        loot = listOf(
            LootDrop("gold", 5, 1.0),       // 100% de chance d'avoir de l'or
            LootDrop("slime_gel", 1, 0.3)   // 30% de chance d'avoir du gel
        )
    )

    val enemyWolf = Enemy(
        id = "en_forest_wolf",
        name = "Loup Affamé",
        hp = 60,
        attack = 12,
        defense = 4,
        loot = listOf(
            LootDrop("gold", 15, 1.0),
            LootDrop("wolf_fur", 1, 0.15)  // Rare : 15%
        )
    )

    // Un Ennemi plus costaud pour l'étage 2
    val enemyGoblin = Enemy(
        id = "en_goblin_scout",
        name = "Éclaireur Gobelin",
        hp = 100,
        attack = 18,
        defense = 8,
        loot = listOf(
            LootDrop("gold", 40, 1.0),
            LootDrop("iron_ore", 2, 0.4)
        )
    )
    val floorForestEntry = DungeonFloor(
        id = "floor_forest_1",
        name = "Orée du Bois",
        description = "L'herbe est haute et les slimes pullulent.",
        isUnlocked = true, // Le tout premier étage est ouvert par défaut
        enemiesIds = listOf("en_slime_green"), // Uniquement des slimes pour s'échauffer
        clearRewardGold = 100
    )

    val floorForestDeep = DungeonFloor(
        id = "floor_forest_2",
        name = "Forêt Sombre",
        description = "Les loups rôdent entre les arbres centenaires.",
        isUnlocked = false,
        enemiesIds = listOf("en_slime_green", "en_forest_wolf"), // Mix de monstres
        clearRewardGold = 250
    )

    val floorGoblinCamp = DungeonFloor(
        id = "floor_forest_3",
        name = "Campement Gobelin",
        description = "Attention aux embuscades !",
        isUnlocked = false,
        enemiesIds = listOf("en_forest_wolf", "en_goblin_scout"),
        clearRewardGold = 600
    )

    val forestDungeon = Dungeon(
        id = "dungeon_forest",
        name = "Forêt des Murmures",
        isUnlocked = true,
        floors = listOf("floor_forest_1", "floor_forest_2", "floor_forest_3"),
        firstClearBonusId = "class_warrior" // Débloque une nouvelle classe de héros à la fin !
    )

    private val enemies = listOf(enemySlime, enemyWolf, enemyGoblin).associateBy { it.id }
    private val floors = listOf(floorForestEntry, floorForestDeep, floorGoblinCamp).associateBy { it.id }
    private val dungeons = listOf(forestDungeon).associateBy { it.id }


    fun getResourceById(id: String) = resourceList.find { it.id == id }
    fun getIconForRessource(id : String) = getResourceById(id)?.icon ?: Icons.Default.Build
    fun getRecepie(id: String) = recipes.find { it.id == id }
    fun getProductionZoneById(id: String) = productionZones.find { it.id == id }
    fun getResouceName(id: String) = getResourceById(id)?.name ?: "Inconnu"
    fun getProductionAreaById(id: String) = productionAreas.find { it.id == id }
    fun getEnemy(id: String) = enemies[id]
    fun getFloor(id: String) = floors[id]
    fun getDungeon(id: String) = dungeons[id]

    fun getActionEnityById(id: String) = machines.find { it.id == id } ?: productionZones.find { it.id == id }
}
