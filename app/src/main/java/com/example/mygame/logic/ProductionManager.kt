package com.example.mygame.logic

import com.example.mygame.data.GameDatabase

open class ProductionManager {

    open fun IsProductionZoneUnlocked(zoneId: String): Boolean {
        val zone = GameDatabase.getProductionZoneById(zoneId) ?: return false
        return zone.isUnlocked
    }

}

fun Long.toGameFormat(): String = if (this >= 1000) "${this / 1000}k" else this.toString()
fun Int.toGameFormat(): String = if (this >= 1000) "${this / 1000}k" else this.toString()