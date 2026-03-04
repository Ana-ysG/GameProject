package com.example.mygame.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygame.data.ActionEntity
import com.example.mygame.data.ProductionArea
import com.example.mygame.ui.PopUp.SelectionDialog
import com.example.mygame.ui.components.ActionZoneComposant
import com.example.mygame.ui.components.BackButton
import com.example.mygame.ui.components.VerticalGameCard
import com.example.mygame.ui.theme.GameText
import com.example.mygame.viewmodel.GameViewModel


@Composable
fun ProductionScreen(
    area: ProductionArea,
    viewModel: GameViewModel,
    onBack: () -> Unit
){
    val currentZoneId = viewModel.state.areaToProductionZone[area.id] ?: area.defaultArea

    BackButton(onBack)
    ActionZoneComposant(
        zone = area,
        actionId = currentZoneId,
        viewModel = viewModel,
        modifier = Modifier.padding(4.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            ProductionZoneContent(
                area = area,
                viewModel = viewModel
            )
        }

    }
}

@Composable
fun ProductionZoneContent(
    area: ProductionArea,
    viewModel: GameViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    // Récupération de la zone actuelle
    val currentZoneId = viewModel.state.areaToProductionZone[area.id] ?: area.defaultArea
    val zone = remember(currentZoneId) {
        viewModel.getProductionZoneById(currentZoneId) ?: error("Zone $currentZoneId introuvable")
    }

    // On prépare les données d'affichage des loots
    val lootItems = remember(zone) {
        zone.lootTable.map { loot ->
            val resource = viewModel.getResourceById(loot.resourceId)!!
            Triple(resource.name, resource.icon, (loot.dropChance * 100).toInt())
        }
    }

    AnimatedContent(
        targetState = zone,
        label = "ZoneTravelTransition",
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) + scaleIn() togetherWith fadeOut(animationSpec = tween(500))
        }
    ) { targetZone ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            GameText.ZoneTitle(
                text = targetZone.name,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.weight(1f))
            // Navigation Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    IconButton(
                        onClick = {viewModel.changeCurrentAction(targetZone.id)}
                    ){
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    IconButton(
                        onClick = {showDialog = true}
                    ){
                        Icon(
                            imageVector = targetZone.icon,
                            contentDescription = "Selection",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                VerticalGameCard(
                   // height = 150.dp
                ) {
                    lootItems.forEach { (name, icon, percent) ->
                        GameText.Standardtitle(
                            text = "($percent%)",
                            icon = icon
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

        }
    }


    if(showDialog){
        SelectionDialog(
            title = "Select production zone...",
            options = viewModel.unlockProductionZone(area.availableActionsIds),
            onSelect = { selectedZone ->
                showDialog = false
                viewModel.changeProductionZone(area, selectedZone as ActionEntity.ProductionZone)
            },
            onDismiss = {showDialog = false }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProductionZoneScreenPreview() {
    val viewModel = GameViewModel()
    ProductionScreen(viewModel.getProductionAreaById("A")!!, viewModel, {})
}













