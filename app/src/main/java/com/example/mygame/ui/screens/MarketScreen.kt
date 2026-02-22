package com.example.mygame.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygame.data.constants.MarketScene
import com.example.mygame.ui.components.AreaButton
import com.example.mygame.ui.theme.GameText
import com.example.mygame.viewmodel.GameViewModel

@Composable
fun MarketScreen(viewModel: GameViewModel) {
    val currentScene = viewModel.state.currentMarketScene

    // On utilise AnimatedContent pour gérer toutes les transitions du marché
    AnimatedContent(
        targetState = currentScene,
        label = "MarketSceneTransition",
        transitionSpec = {
            // Effet de glissement latéral : plus immersif pour un marché
            if (targetState != MarketScene.MENU) {
                (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
            } else {
                (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
            }
        }
    ) { scene ->
        when (scene) {
            MarketScene.SELL -> SalesStandScreen(
                viewModel = viewModel,
                onBack = { viewModel.changeCurrentMarketScene(MarketScene.MENU) }
            )
            MarketScene.BUY -> ShopScreen(
                shop = viewModel.getShopById("shop1")!!,
                viewModel = viewModel,
                onBack = { viewModel.changeCurrentMarketScene(MarketScene.MENU) }
            )
            MarketScene.QUESTS -> QuestBoardScreen(
                viewModel = viewModel,
                onBack = { viewModel.changeCurrentMarketScene(MarketScene.MENU) }
            )
            MarketScene.PNJS -> PNJConversationScreen(
                viewModel = viewModel,
                onBack = { viewModel.changeCurrentMarketScene(MarketScene.MENU) }
            )
            else -> {
                // MENU PRINCIPAL DE LA PLACE
                MarketMenu(onSceneChange = { viewModel.changeCurrentMarketScene(it) })
            }
        }
    }
}

@Composable
private fun MarketMenu(onSceneChange: (MarketScene) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameText.AreaTitle("Place du Marché")
        Spacer(modifier = Modifier.height(24.dp))

        // On organise les boutons pour qu'ils soient plus visuels
        val menuOptions = listOf(
            Triple("Vendre mes produits", Icons.Default.ShoppingCart, MarketScene.SELL), // Icône plus adaptée
            Triple("Acheter des composants", Icons.Default.Add, MarketScene.BUY),
            Triple("Tableau des quêtes", Icons.Default.DateRange, MarketScene.QUESTS),
            Triple("Parler aux marchands", Icons.Default.Person, MarketScene.PNJS)
        )

        menuOptions.forEach { (text, icon, scene) ->
            AreaButton(
                label = text,
                icon = icon,
            ) {
                onSceneChange(scene)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    val viewModel = GameViewModel()
    MarketScreen(viewModel)
}