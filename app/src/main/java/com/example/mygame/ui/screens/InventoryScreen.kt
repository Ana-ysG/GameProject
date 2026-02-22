package com.example.mygame.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygame.viewmodel.GameViewModel
import com.example.mygame.data.Resource
import com.example.mygame.ui.theme.GameText

@Composable
fun InventoryScreen(viewModel: GameViewModel) {
    val state = viewModel.state
    // On filtre les items pour n'avoir que ceux présents (> 0)
    val ownedItems = remember(state.inventory.items) {
        state.inventory.items.filter { it.value > 0 }.toList()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        GameText.AreaTitle("Inventaire")
        Spacer(modifier = Modifier.height(10.dp))

        // Affichage des ressources avec un peu plus de style
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GameText.ZoneTitle("Mana : ${state.mana}", color = MaterialTheme.colorScheme.primary)
            GameText.ZoneTitle("Or : ${state.gold}", color = MaterialTheme.colorScheme.secondary)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // Utilisation de AnimatedContent pour basculer entre l'état vide et la liste
        AnimatedContent(
            targetState = ownedItems.isEmpty(),
            label = "InventoryEmptyTransition",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { isEmpty ->
            if (isEmpty) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    GameText.HelperText("Votre inventaire est vide")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // key permet à Compose de suivre l'item même s'il change de place
                    items(ownedItems, key = { it.first }) { (id, amount) ->
                        val resData = remember(id) { viewModel.getResourceById(id) }

                        InventoryItemRow(resData = resData, id = id, amount = amount)
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryItemRow(resData: Resource?, id: String, amount: Long) {
    // Petit effet d'apparition pour chaque ligne
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = {
                Text(resData?.name ?: id, fontWeight = FontWeight.Bold)
            },
            supportingContent = {
                Text(resData?.description ?: "Ressource mystérieuse", maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            leadingContent = {
                // Si tu as des icônes, c'est l'endroit parfait !
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            trailingContent = {
                Text("x$amount", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryScreenPreview() {
    val viewModel = remember { GameViewModel() }
    // On peut simuler l'ajout d'objets pour voir le rendu
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            InventoryScreen(viewModel)
        }
    }
}