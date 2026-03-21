package com.example.mygame.data

class GameRepository {

    // --- ACCÈS AUX RESSOURCES ---
    fun getResource(id: String): Resource? {
        return GameDatabase.getResourceById(id)
    }

    fun getAllSellableResources(): List<Resource> {
        return GameDatabase.resourceList.filter { it.sellValue > 0 }
    }

    // --- ACCÈS AUX RECETTES ---
    fun getRecipe(id: String): Recipe? {
        return GameDatabase.getRecepie(id)
    }

    fun getRecipesForIds(ids: List<String>): List<Recipe> {
        return ids.mapNotNull { getRecipe(it) }
    }

    // --- ACCÈS AUX ZONES ET MACHINES ---
    fun getProductionZone(id: String): ActionEntity.ProductionZone? {
        return GameDatabase.getProductionZoneById(id)
    }

    fun getProductionArea(id: String): ProductionArea? {
        return GameDatabase.productionAreas.find { it.id == id }
    }
    fun getAllProductionAreas(): List<ProductionArea> {
        return GameDatabase.productionAreas
    }
    fun getActionEntity(id: String): ActionEntity? {
        return GameDatabase.getActionEnityById(id)
    }

    // --- ACCÈS COMBAT ---
    fun getEnemy(id: String): Enemy? {
        return GameDatabase.getEnemy(id)
    }
    fun getFloor(id: String): DungeonFloor? {
        return GameDatabase.getFloor(id)
    }
    fun getDungeon(id: String): Dungeon? {
        return GameDatabase.getDungeon(id)
    }


    // --- LOGIQUE DE BOUTIQUE ---
    fun getShop(id: String): Shop? {
        // Si tu as plusieurs shops dans GameDatabase
        return if (id == "shop1") GameDatabase.shop1 else null
    }
}