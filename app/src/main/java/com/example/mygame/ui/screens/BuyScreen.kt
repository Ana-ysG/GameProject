package com.example.mygame.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygame.data.Shop
import com.example.mygame.ui.components.ActionGrid
import com.example.mygame.ui.components.BackButton
import com.example.mygame.ui.components.ZoneComposant
import com.example.mygame.ui.theme.GameText
import com.example.mygame.viewmodel.GameViewModel


@Composable
fun ShopScreen(shop: Shop, viewModel: GameViewModel, onBack: () -> Unit) {
    // Optimisation : On ne recalcule la liste que si les IDs du shop changent
    val storeItems = remember(shop.availableActionsIds) {
        shop.availableActionsIds.mapNotNull { viewModel.getResourceById(it) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(4.dp)) {
        BackButton(onBack)

        ZoneComposant(
            name = shop.name,
            iconResId = shop.iconResId,
            modifier = Modifier,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GameText.ZoneTitle(
                    text = "Or : ${viewModel.state.gold}",
                    color = MaterialTheme.colorScheme.secondary
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                ActionGrid(
                    options = storeItems,
                    popupContent = { resource ->
                        BuyCard(resourceId = resource.id, viewModel = viewModel)
                    }
                )
            }
        }
    }
}

@Composable
fun BuyCard(
    resourceId: String,
    viewModel: GameViewModel
) {
    val resource = remember(resourceId) { viewModel.getResourceById(resourceId) }
    val unitPrice = resource?.buyValue ?: 0
    val currentGold = viewModel.state.gold

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Prix unitaire : $unitPrice Or",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Liste des quantités pour générer les boutons proprement
            val quantities = listOf(1L, 10L, 100L)

            quantities.forEach { amount ->
                val totalCost = unitPrice * amount
                val canAfford = currentGold >= totalCost

                Button(
                    onClick = { viewModel.purchaseResource(resourceId, amount) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Acheter $amount unité${if (amount > 1) "s" else ""} ($totalCost Or)")
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun BuyScreenPreview() {
    // On peut simuler un état initial pour la preview
    val viewModel = GameViewModel()

    val fakeShop = Shop(
        id = "shop1",
        name = "Marchand",
        description = "Vente de ressources de base",
        isUnlocked = true,
        iconResId = android.R.drawable.ic_menu_report_image, // Icône de test
        availableActionsIds = listOf("green_herb", "wood")
    )

    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ShopScreen(shop = fakeShop, viewModel = viewModel, onBack = {})
        }
    }
}