package com.example.mygame.logic

import com.example.mygame.data.GameRepository

object LogicEngine {

    fun applyTick(state: GameState, deltaTime: Double, repository: GameRepository): GameState {
        var currentState = state

        // On enchaîne les générateurs
        currentState = generateMana(currentState, deltaTime)
        currentState = updateActionProgress(currentState, deltaTime, repository)


        return currentState.copy(lastTickTimestamp = System.currentTimeMillis())
    }
    private fun generateMana(state: GameState, dt: Double): GameState {
        val baseRegen = 0.5 // Le mana remonte plus doucement
        val manaboost = state.manaboost
        val total = state.mana + state.subMana + (baseRegen * dt * manaboost)


        return state.copy(
            mana = total.toLong(),
            subMana = total % 1
        )
    }

    private fun updateActionProgress(state: GameState, deltaTime: Double, repository: GameRepository): GameState {
        // Si aucune action n'est sélectionnée, on reset la barre et on s'arrête là
        val actionId = state.currentActionId ?: return state.copy(actionProgress = 0.0)
        val entity = repository.getActionEntity(actionId) ?: return state.copy(actionProgress = 0.0)

        if (!entity.canProgress(state,repository)) {
            return state.copy(
                currentActionId = null,
                actionProgress = 0.0)
        }
        // 1. Déterminer la vitesse (Plus tard, on ira chercher action.baseSpeed)
        val speed = 0.2

        // 2. Calculer la nouvelle progression
        val newProgress = state.actionProgress + (speed * deltaTime)

        // 3. Vérifier si l'action est terminée
        return if (newProgress > 1.1) {
            // L'action est finie : on donne la récompense et on reset à 0.0
            val stateWithReward = entity.executeAction(state, repository)
            stateWithReward.copy(actionProgress = 0.0)
        } else {
            // L'action continue : on met juste à jour le pourcentage
            state.copy(actionProgress = newProgress)
        }
    }

}