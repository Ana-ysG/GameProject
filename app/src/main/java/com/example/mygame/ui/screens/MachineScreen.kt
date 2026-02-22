package com.example.mygame.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygame.data.Machine
import com.example.mygame.data.Recipe
import com.example.mygame.ui.PopUp.SelectionDialog
import com.example.mygame.ui.components.ActionZoneComposant
import com.example.mygame.ui.components.ResourceRequirementText
import com.example.mygame.ui.components.VerticalGameCard
import com.example.mygame.viewmodel.GameViewModel
import kotlin.collections.component1
import kotlin.collections.component2


@Composable
fun MachineScreen(
    machine: Machine,
    viewModel: GameViewModel,
    onBack: () -> Unit
){
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(machine.id) {
        if (viewModel.state.machineToRecipe[machine.id] == null) {
            viewModel.changeCurrentrecipe(machine.id, machine.availableActionsIds.first())
        }
    }
    val recipeId = viewModel.state.machineToRecipe[machine.id] ?: machine.availableActionsIds.first()
    val currentRecipe = remember(recipeId) { viewModel.getRecipeById(recipeId)!! }

    ActionZoneComposant(
        zone = machine,
        modifier = Modifier
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Column() {
                Spacer(modifier = Modifier.height(30.dp))
                IconButton(
                    onClick = {viewModel.craftItem(recipeId)}
                ){
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                AnimatedContent(
                    targetState = currentRecipe.icon,
                    label = "RecipeIconTransition"
                ) { icon ->
                    IconButton(
                        onClick = {showDialog = true}
                    ){
                        Icon(
                            imageVector = icon,
                            contentDescription = "Selection",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            //CraftCard(currentRecipe, viewModel)
            Box() {
                AnimatedContent(
                    targetState = currentRecipe,
                    label = "CraftCardTransition",
                    transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut() }
                ) { targetRecipe ->
                    CraftCard(targetRecipe, viewModel)
                }
            }

        }
    }
    if(showDialog){
        SelectionDialog(
            title = "Select recepie...",
            options = viewModel.unlockedRecipes(machine.availableActionsIds),
            onSelect = { selectedZone ->
                showDialog = false
                viewModel.changeCurrentrecipe(machine.id, selectedZone.id)
            },
            onDismiss = {showDialog = false }
        )
    }
}
@Composable
fun CraftCard(
    recipe: Recipe,
    viewModel: GameViewModel
) {
    val state = viewModel.state

    // Calcul de l'état du craft
    val hasMana = state.mana >= recipe.manaCost
    val hasIngredients = remember(recipe, state.inventory) {
        recipe.ingredients.all { (id, required) ->
            state.inventory.getAmount(id) >= required
        }
    }
    val canCraft = hasMana && hasIngredients

    VerticalGameCard(
    ){
        // Ligne Mana
        ResourceRequirementText(Icons.Default.Build, viewModel.state.mana, recipe.manaCost)

        // Lignes Ingrédients
        recipe.ingredients.forEach { (id, required) ->
            val res = remember(id) { viewModel.getResourceById(id) }
            val currentAmount = state.inventory.getAmount(id)
            ResourceRequirementText(
                icon = res?.icon ?: Icons.Default.Warning,
                current = currentAmount,
                required = required.toLong(),
            )
        }

    }

}

@Composable
@Preview(showBackground = true)
fun MachineScreenPreview() {
    val viewModel = GameViewModel()
    val selectedMachine = Machine(
        id = "cauldron",
        name = "Chaudron en Fonte",
        isUnlocked = true,
        availableActionsIds = listOf("craft_wand") // La baguette se fait au chaudron
    )
    MachineScreen(
        machine = selectedMachine,
        viewModel = viewModel,
        onBack = { viewModel.changeCurrentMachine(null) }
    )
}