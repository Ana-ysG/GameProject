package com.example.mygame.data
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mygame.logic.GameState
import kotlin.random.Random

enum class ResourceType { NATURE, MAGIC, MINERAL, SPECIAL }
enum class Rarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY }
enum class QuestState { LOCKED, AVAILABLE, ACTIVE, COMPLETED }
enum class QuestOrigin { QUEST_BOARD, PNJ, DUNGEON }

interface SelectableEntity {
    val id: String
    val name: String
    val icon: ImageVector
    val isUnlocked: Boolean
}

interface ActionZone : SelectableEntity {
    val iconResId: Int
    val availableActionsIds: List<String>
}

sealed interface ActionEntity : SelectableEntity {
    fun executeAction(state : GameState, repository: GameRepository): GameState
    fun canProgress(state: GameState, repository: GameRepository): Boolean
    data class Machine(
        override val id: String,
        override val name: String,
        override val icon : ImageVector = Icons.Default.Build,
        override val iconResId: Int = android.R.drawable.ic_menu_report_image,
        override var isUnlocked: Boolean = false,
        override val availableActionsIds: List<String> // Liste des IDs de recettes que cette machine peut faire
    ): ActionEntity, ActionZone {
        override fun executeAction(state: GameState, repository: GameRepository): GameState {
            val recipe = state.machineToRecipe[id]?.let { repository.getRecipe(it) } ?: return state

            // 1. Vérifier si on a assez de mana et de ressources
            if (!canProgress(state, repository)) return state

            // 2. Retirer les ingrédients un par un
            var currentInv = state.inventory
            recipe.ingredients.forEach { (id, amount) ->
                currentInv = currentInv.removeResource(id, amount.toLong()) ?: currentInv
            }

            // 3. Ajouter le produit et mettre à jour le state global
            val finalInv = currentInv.addResource(recipe.productResourceId, recipe.productAmount.toLong())
            var newstate = state.copy(
                mana = state.mana - recipe.manaCost,
                inventory = finalInv
            )
            return newstate
        }
        override fun canProgress(state: GameState, repository: GameRepository): Boolean {
            val recipe = state.machineToRecipe[id]?.let { repository.getRecipe(it) } ?: return false
            return state.mana >= recipe.manaCost && state.inventory.hasResources(recipe.ingredients.mapValues { it.value.toLong() })
        }
    }
    data class ProductionZone(
        override val id: String,
        override val name: String,
        override val icon : ImageVector = Icons.Default.Build,
        val iconResId: Int = android.R.drawable.ic_menu_report_image,
        override var isUnlocked: Boolean = false,
        var tier: Int = 1,
        val lootTable: List<LootDrop>, // Ce qu'on trouve dans cette zone
        val previousTierId: String? = null, // ID de la zone précédente (si elle existe)
        val nextTierId: String? =null // ID de la zone suivante (si elle existe)
    ): ActionEntity {
        override fun executeAction(state: GameState, repository: GameRepository): GameState {
            var currentInv = state.inventory
            for (loot in lootTable) {
                if (Random.nextFloat() < loot.dropChance) {
                    currentInv = currentInv.addResource(loot.resourceId, loot.amount.toLong())
                }
            }
            return state.copy(inventory = currentInv)
        }
        override fun canProgress(state: GameState, repository: GameRepository): Boolean {
            return true
        }
    }
}

data class Resource(
    override val id: String,
    override val name: String,
    val description: String,
    val type: ResourceType,
    override var isUnlocked: Boolean = true,
    val rarity: Rarity = Rarity.COMMON,
    override val icon: ImageVector = Icons.Default.Build,
    val sellValue: Int = 0,
    val buyValue: Int = 0,
): SelectableEntity

data class Recipe(
    override val id: String,
    override val name: String,
    val productResourceId: String,
    val productAmount: Int = 1,
    val ingredients: Map<String, Int>, // ID de ressource -> Quantité
    val manaCost: Long,
    override var isUnlocked: Boolean = false
): SelectableEntity {
    override val icon: ImageVector
        get() = GameDatabase.getIconForRessource(productResourceId)
}

data class Shop(
    override val id: String,
    override val name: String,
    val description: String,
    override val icon: ImageVector = Icons.Default.Build,
    override val iconResId: Int = android.R.drawable.ic_menu_report_image,
    override var isUnlocked: Boolean = false,
    override val availableActionsIds: List<String>
): ActionZone
data class ProductionArea(
    override val id: String,
    override val name: String,
    override val icon : ImageVector = Icons.Default.Build,
    override var iconResId: Int = android.R.drawable.ic_menu_report_image,
    override var isUnlocked: Boolean = false,
    override var availableActionsIds : List<String>,
    var defaultArea : String = availableActionsIds[0]
): ActionZone
data class LootDrop(
    val resourceId: String,
    val amount: Int,
    val dropChance: Double // De 0.0 à 1.0
)

data class Enemy(
    val id: String,
    val name: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val iconResId: Int = android.R.drawable.ic_menu_report_image,
    val loot: List<LootDrop>
)

data class Dungeon(
    val id: String,
    val name: String,
    val description: String,
    val iconResId: Int = android.R.drawable.ic_menu_report_image,
    var isUnlocked: Boolean = false,
    val enemiesIds: List<String>, // Liste des monstres à battre
    val bossId: String,
    val clearRewardGold: Int,
    val firstClearBonusId: String? // Récompense spéciale la première fois (ex: une Classe)
)

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    var state: QuestState = QuestState.LOCKED,
    val origin: QuestOrigin = QuestOrigin.QUEST_BOARD,
    val requiredResources: Map<String, Int> = emptyMap(),
    val rewardGold: Int = 0,
    val rewardResources: Map<String, Int> = emptyMap(),
    val nextQuestId: String? = null
)