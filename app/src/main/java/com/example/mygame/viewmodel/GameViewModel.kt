package com.example.mygame.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mygame.data.ActionEntity
import com.example.mygame.data.GameRepository
import com.example.mygame.data.LootDrop
import com.example.mygame.data.ProductionArea
import com.example.mygame.data.Quest
import com.example.mygame.data.QuestState
import com.example.mygame.data.Recipe
import com.example.mygame.data.constants.ExplorationScene
import com.example.mygame.data.constants.MarketScene
import com.example.mygame.logic.GameState
import com.example.mygame.logic.LogicEngine
import kotlin.random.Random

class GameViewModel(
    private val repository: GameRepository = GameRepository()
) : ViewModel() {
    var state by mutableStateOf(GameState())
        private set


    // --- LOGIQUE DU CLICKER ---
    fun clickOrb() {
        state = state.copy(mana = state.mana + 1)
    }

    fun updateManaBoost(value: Double) {
        state = state.copy(manaboost = value)
    }

    // --- LOGIQUE DU REPOSITORY ---
    fun getResourceById(id: String) = repository.getResource(id)
    fun getRecipeById(id: String) = repository.getRecipe(id)
    fun getProductionZoneById(id: String) = repository.getProductionZone(id)
    fun getProductionAreaById(id: String) = repository.getProductionArea(id)
    fun getALLproductionAreas() = repository.getAllProductionAreas()
    fun getShopById(id: String) = repository.getShop(id)
    fun getAllSellableResources() = repository.getAllSellableResources()
    fun getEnemyById(id: String) = repository.getEnemy(id)
    fun getFloorById(id: String) = repository.getFloor(id)
    fun getDungeonById(id: String) = repository.getDungeon(id)


    fun updateGameTick() {
        val currentTime = System.currentTimeMillis()
        // On calcule l'écart en secondes (ex: 1000ms = 1.0s)
        val deltaTime = (currentTime - state.lastTickTimestamp).toDouble() / 1000.0

        if (deltaTime < 0.1) return
        // On délègue tout le calcul au moteur
        state = LogicEngine.applyTick(state, deltaTime, repository)
    }

    // --- LOGIQUE D'INVENTAIRE ---
    fun collectRessource(ressourceId: String, amount: Long = 1) {
        val newInventory = state.inventory.addResource(ressourceId, amount)
        state = state.copy(inventory = newInventory)
    }

    // --- LOGIQUE DE CRAFT ---
    fun craftItem(recipeId: String) {
        val recipe = repository.getRecipe(recipeId) ?: return

        // 1. Vérifier si on a assez de mana et de ressources
        if (state.mana < recipe.manaCost || !state.inventory.hasResources(recipe.ingredients.mapValues { it.value.toLong() })) return

        // 2. Retirer les ingrédients un par un
        var currentInv = state.inventory
        recipe.ingredients.forEach { (id, amount) ->
            currentInv = currentInv.removeResource(id, amount.toLong()) ?: currentInv
        }

        // 3. Ajouter le produit et mettre à jour le state global
        val finalInv = currentInv.addResource(recipe.productResourceId, recipe.productAmount.toLong())
        state = state.copy(
            mana = state.mana - recipe.manaCost,
            inventory = finalInv
        )
    }

    // --- LOGIQUE COMMERCIALE ---
    fun sellItem(resourceId: String, amount: Long = 1) {
        val resourceData = repository.getResource(resourceId) ?: return
        val value = resourceData.sellValue

        // Tentative de retrait de l'inventaire immuable
        val newInventory = state.inventory.removeResource(resourceId, amount)

        if (newInventory != null) {
            state = state.copy(
                inventory = newInventory,
                gold = state.gold + (value * amount)
            )
        }
    }

    fun purchaseResource(resourceId: String, amount: Long = 1) {
        val resource = repository.getResource(resourceId) ?: return
        val totalPrice = resource.buyValue * amount

        if (state.gold >= totalPrice) {
            val newInventory = state.inventory.addResource(resourceId, amount)
            state = state.copy(
                gold = state.gold - totalPrice,
                inventory = newInventory
            )
        }
    }

    // --- LOGIQUE DES QUÊTES ---
    fun handleQuestAction(quest: Quest) {
        val index = state.quests.indexOfFirst { it.id == quest.id }
        if (index == -1) return

        val currentQuest = state.quests[index]
        var currentInv = state.inventory
        var currentGold = state.gold

        val updatedQuests = state.quests.toMutableList()

        when (currentQuest.state) {
            QuestState.AVAILABLE -> {
                updatedQuests[index] = currentQuest.copy(state = QuestState.ACTIVE)
            }
            QuestState.ACTIVE -> {
                // Vérification des ressources
                if (!currentInv.hasResources(currentQuest.requiredResources.mapValues { it.value.toLong() })) return

                // Retrait des ressources
                currentQuest.requiredResources.forEach { (id, amount) ->
                    currentInv = currentInv.removeResource(id, amount.toLong()) ?: currentInv
                }

                currentGold += currentQuest.rewardGold
                updatedQuests[index] = currentQuest.copy(state = QuestState.COMPLETED)

                // Déblocage de la suivante
                currentQuest.nextQuestId?.let { nextId ->
                    val nextIndex = updatedQuests.indexOfFirst { it.id == nextId }
                    if (nextIndex != -1) {
                        updatedQuests[nextIndex] = updatedQuests[nextIndex].copy(state = QuestState.AVAILABLE)
                    }
                }
            }
            else -> return
        }

        // Mise à jour unique du state
        state = state.copy(
            quests = updatedQuests,
            inventory = currentInv,
            gold = currentGold
        )
    }

    fun CollectLoot(loots: List<LootDrop>) {
        var currentInv = state.inventory
        for (loot in loots) {
            if (Random.nextFloat() < loot.dropChance) {
                currentInv = currentInv.addResource(loot.resourceId, loot.amount.toLong())
            }
        }
        state = state.copy(inventory = currentInv)
    }

    // --- LOGIQUE DE PRODUCTION & MACHINES ---
    fun changeCurrentAction(id: String?) {
        if(state.currentActionId == null || state.currentActionId != id){
            state = state.copy(
                currentActionId = id,
                actionProgress = 0.0
            )
        }else{
            state = state.copy(
                currentActionId = null,
                actionProgress = 0.0
            )
        }
    }
    fun changeProductionZone(area: ProductionArea, zone: ActionEntity.ProductionZone) {
        val newMap = state.areaToProductionZone.toMutableMap()
        newMap[area.id] = zone.id

        // Note : On ne modifie plus area.iconResId ici !
        // L'UI utilisera le mapping pour afficher la bonne icône.
        state = state.copy(areaToProductionZone = newMap)
    }

    fun changeCurrentMachine(machine: ActionEntity.Machine?) {
        state = state.copy(currentWorkshopMachine = machine)
    }

    // --- GETTERS & DERIVED STATES ---
    val visibleQuests by derivedStateOf {
        state.quests.filter { quest ->
            val isVisible = quest.state != QuestState.LOCKED
            if (state.hideCompletedQuests) {
                isVisible && quest.state != QuestState.COMPLETED
            } else {
                isVisible
            }
        }
    }

    fun unlockedRecipes(recipeIds: List<String>): List<Recipe> {
        return recipeIds.mapNotNull { id ->
            repository.getRecipe(id)?.takeIf { it.isUnlocked }
        }
    }

    fun unlockProductionZone(zoneIds: List<String>): List<ActionEntity.ProductionZone> {
        return zoneIds.mapNotNull { id ->
            repository.getProductionZone(id)?.takeIf { it.isUnlocked }
        }
    }
    fun changeCurrentExplorationZone(exploration: ExplorationScene) {
        state = state.copy(currentExplorationScene = exploration)
    }
    fun changeCurrentMarketScene(market: MarketScene) {
        state = state.copy(currentMarketScene = market)
    }
    fun changeCurrentrecipe(machineId: String, recipeId: String) {
        state = state.copy(
            machineToRecipe = state.machineToRecipe + (machineId to recipeId)
        )
    }
}

