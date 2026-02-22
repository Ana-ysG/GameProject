package com.example.mygame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygame.data.GameDatabase
import com.example.mygame.viewmodel.GameViewModel

@Composable
fun WorkshopScreen(viewModel: GameViewModel) {
    // On observe la machine sélectionnée stockée dans le ViewModel
    val selectedMachine = viewModel.state.currentWorkshopMachine

    if (selectedMachine == null) {
        // --- SCÈNE 1 : LISTE DES MACHINES ---
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Atelier", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                // On filtre pour n'afficher que les machines débloquées
                val unlockedMachines = GameDatabase.machines.filter { it.isUnlocked }

                items(unlockedMachines) { machine ->
                    Button(
                        // ACTION : On met à jour le ViewModel pour "sauvegarder" le choix
                        onClick = { viewModel.changeCurrentMachine(machine)},
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text(machine.name)
                    }
                }
            }
        }
    } else {
        // --- SCÈNE 2 : ÉCRAN DE LA MACHINE ---
        // On affiche l'écran de la machine avec le bouton retour qui remet à null
        MachineScreen(
            machine = selectedMachine,
            viewModel = viewModel,
            onBack = { viewModel.changeCurrentMachine(null)}
        )
    }
}

