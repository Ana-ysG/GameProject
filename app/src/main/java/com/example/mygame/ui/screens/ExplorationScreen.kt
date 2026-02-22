package com.example.mygame.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygame.data.GameDatabase
import com.example.mygame.data.constants.ExplorationScene
import com.example.mygame.data.constants.ResIds
import com.example.mygame.ui.components.AreaButton
import com.example.mygame.ui.theme.GameText
import com.example.mygame.viewmodel.GameViewModel

@Composable
fun ExplorationScreen(viewModel: GameViewModel) {
    // On extrait l'état pour plus de lisibilité
    val currentScene = viewModel.state.currentExplorationScene

    // Utilisation d'une animation de transition pour rendre le changement d'écran moins brutal
    AnimatedContent(
        targetState = currentScene,
        label = "ExplorationTransition"
    ) { scene ->
        when (scene) {
            ExplorationScene.COLLECT -> {
                // On récupère la zone de collecte (index 0)
                val collectArea = remember { viewModel.getALLproductionAreas().firstOrNull() }

                if (collectArea != null) {
                    ProductionScreen(
                        area = collectArea,
                        viewModel = viewModel,
                        onBack = { viewModel.changeCurrentExplorationZone(ExplorationScene.MENU) }
                    )
                }
            }
            else -> {
                // MENU PRINCIPAL D'EXPLORATION
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameText.AreaTitle("Exploration")

                    Spacer(modifier = Modifier.height(24.dp))

                    // Liste des boutons d'exploration (facile à étendre)
                    AreaButton(
                        "Collecter des ressources",
                        Icons.Default.Menu,
                    ){
                        viewModel.changeCurrentExplorationZone(ExplorationScene.COLLECT)
                    }

                    // On pourrait ajouter ici : Chasse, Pêche, Mines, etc.
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true, device = "id:pixel_7")
fun ExplorationScreenPreview() {
    // Simuler un état spécifique pour la preview si besoin
    val viewModel = remember { GameViewModel() }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ExplorationScreen(viewModel)
        }
    }
}