package com.example.mygame.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygame.data.Quest
import com.example.mygame.data.QuestState
import com.example.mygame.ui.components.BackButton
import com.example.mygame.ui.components.GameCard
import com.example.mygame.ui.components.ResourceRequirementText
import com.example.mygame.ui.theme.GameText
import com.example.mygame.viewmodel.GameViewModel
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun QuestBoardScreen(viewModel: GameViewModel, onBack: () -> Unit) {
    val state = viewModel.state
    val filteredQuests = viewModel.visibleQuests

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton(onBack)
            GameText.AreaTitle("Tableau des Quêtes")
        }

        // --- OPTIMISATION : Contrôle du Switch ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Masquer les quêtes terminées", modifier = Modifier.weight(1f))
            Switch(
                checked = state.hideCompletedQuests,
                // Correction : On utilise une fonction du ViewModel pour modifier le state immuable
                onCheckedChange = { viewModel.state.hideCompletedQuests = it  }
            )
        }

        // --- ANIMATION : Liste de quêtes ---
        AnimatedContent(
            targetState = filteredQuests.isEmpty(),
            label = "EmptyQuestTransition"
        ) { isEmpty ->
            if (isEmpty) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Aucune quête disponible", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(items = filteredQuests, key = { it.id }) { quest ->
                        // On ajoute une animation de sortie/entrée pour chaque carte
                        Box(modifier = Modifier.animateItem()) {
                            QuestCard(quest = quest, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestCard(quest: Quest, viewModel: GameViewModel) {
    val state = viewModel.state

    // 1. Calcul optimisé des conditions (une seule fois par recomposition)
    val hasRequirements = remember(quest, state.inventory) {
        quest.requiredResources.all { (id, qty) ->
            state.inventory.getAmount(id) >= qty
        }
    }

    GameCard() {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GameText.QuestTitle(text = quest.title)
                Spacer(modifier = Modifier.weight(1f))
                // Badge d'état
                Badge(containerColor = when(quest.state) {
                    QuestState.ACTIVE -> MaterialTheme.colorScheme.primary
                    QuestState.COMPLETED -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.outline
                }) {
                    Text(quest.state.name)
                }
            }

            GameText.QuestDescription(text = quest.description, modifier = Modifier.padding(vertical = 4.dp))

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Objectifs
            GameText.QuestText(text = "Objectif :")
            quest.requiredResources.forEach { (id, amount) ->
                val icon = remember(id) {
                    viewModel.getResourceById(id)?.icon ?: Icons.Default.Build
                }
                ResourceRequirementText(
                    icon = icon,
                    current = viewModel.state.inventory.getAmount(id),
                    required = amount.toLong(),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Récompenses
            GameText.QuestText(text = "Récompenses :")
            Row {
                if (quest.rewardGold > 0) {
                    Text(text = "${quest.rewardGold} Or ", color = MaterialTheme.colorScheme.secondary)
                }
                quest.rewardResources.forEach { (id, amount) ->
                    val name = viewModel.getResourceById(id)?.name ?: id
                    Text(text = "+$amount $name ", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            val isActionEnabled = when(quest.state) {
                QuestState.AVAILABLE -> true
                QuestState.ACTIVE -> hasRequirements
                else -> false
            }


            // Bouton d'action unique selon l'état
            Button(
                onClick = { viewModel.handleQuestAction(quest) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isActionEnabled
            ) {
                Text(when(quest.state) {
                    QuestState.AVAILABLE -> "Accepter la quête"
                    QuestState.ACTIVE -> "Livrer et Terminer"
                    else -> "Terminée"
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestCardPreview() {
    val viewModel = GameViewModel()
    QuestBoardScreen(viewModel,{})
}
