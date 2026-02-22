package com.example.mygame.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygame.logic.toGameFormat
import com.example.mygame.ui.components.ActionGrid
import com.example.mygame.ui.components.BackButton
import com.example.mygame.ui.components.ResourceRequirementText
import com.example.mygame.ui.components.ZoneComposant
import com.example.mygame.ui.theme.GameText
import com.example.mygame.viewmodel.GameViewModel

@Composable
fun SalesStandScreen(viewModel: GameViewModel, onBack: () -> Unit) {
    val state = viewModel.state

    // Optimisation : On ne filtre la liste des ressources vendables qu'une fois
    val sellableItems = remember {
        viewModel.getAllSellableResources()
    }
    BackButton(onBack)
    ZoneComposant(
        name = "Marché de la Cité",
        iconResId = android.R.drawable.ic_menu_report_image,
        modifier = Modifier.padding(4.dp),
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameText.ZoneTitle("Or : ${state.gold.toGameFormat()}", color = MaterialTheme.colorScheme.secondary)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Utilisation de ActionGrid
            ActionGrid(
                options = sellableItems,
                popupContent = { resource ->
                    SellCard(resourceId = resource.id, viewModel = viewModel)
                }
            )
        }
    }
}

@Composable
fun SellCard(
    resourceId: String,
    viewModel: GameViewModel
) {
    val state = viewModel.state
    val resource = remember(resourceId) { viewModel.getResourceById(resourceId) }
    val stock = state.inventory.getAmount(resourceId)
    val unitValue = resource?.sellValue ?: 0

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ResourceRequirementText(
                icon = resource?.icon ?: Icons.Default.Build,
                current = stock,
                required = 0L,
            )

            Text(text = "Prix de vente : ${unitValue.toGameFormat()} Or / unité")

            Spacer(modifier = Modifier.height(10.dp))

            val quantities = listOf(1L, 10L, 100L)

            quantities.forEach { amount ->
                val hasEnough = stock >= amount
                val totalGain = unitValue * amount

                Button(
                    onClick = { viewModel.sellItem(resourceId, amount) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    enabled = hasEnough,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                ) {
                    // On anime le texte pour qu'il s'adapte si le stock baisse
                    Text(
                        text = if (hasEnough) "Vendre ${amount.toGameFormat()} (+ ${totalGain.toGameFormat()} Or)" else "Stock insuffisant (${amount.toGameFormat()})"
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SalesStandScreenPreview() {
    val viewModel = GameViewModel()
    SalesStandScreen(viewModel = viewModel, onBack = {})
}

