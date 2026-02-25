package com.example.mygame.logic

import com.example.mygame.data.ActionEntity
import com.example.mygame.data.GameDatabase
import com.example.mygame.data.Quest
import com.example.mygame.data.constants.ExplorationScene
import com.example.mygame.data.constants.MarketScene

data class GameState(
    // Ressources de base
    val mana: Long = 0L,
    val subMana: Double = 0.0,
    val gold: Long = 100L,
    val subGold: Double = 0.0,
    val lastTickTimestamp: Long = System.currentTimeMillis(),
    val inventory: InventoryData = InventoryData(),
    //val production: ProductionManager = ProductionManager(), // Data class pour le manager

    // Progression
    val currentClassName: String = "Apprenti",
    val level: Long = 1L,

    // État des Quêtes
    val quests: List<Quest> = GameDatabase.questList, // Liste immuable
    var hideCompletedQuests: Boolean = false,

    // Navigation et État des machines
    val currentWorkshopMachine: ActionEntity.Machine? = null,
    val currentMarketScene: MarketScene = MarketScene.MENU,
    val currentExplorationScene: ExplorationScene = ExplorationScene.MENU,

    // Correspondance Machine ID -> Recette ID ou Zone ID
    val areaToProductionZone: Map<String, String> = emptyMap(),
    val machineToRecipe: Map<String, String> = emptyMap(),

    //gestion de l'action en cours
    val currentActionId: String? = null,
    val actionProgress: Double = 0.0,
)


