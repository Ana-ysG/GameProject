package com.example.mygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygame.data.GameDatabase
import com.example.mygame.data.Machine
import com.example.mygame.ui.screens.ExplorationScreen
import com.example.mygame.ui.PopUp.IconActionButton
import com.example.mygame.ui.PopUp.SelectionDialog
import com.example.mygame.ui.screens.InventoryScreen
import com.example.mygame.ui.screens.MachineScreen
import com.example.mygame.ui.screens.MarketScreen
import com.example.mygame.ui.screens.StatueScreen
import com.example.mygame.ui.screens.WorkshopScreen
import com.example.mygame.viewmodel.GameViewModel
import kotlin.collections.set

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gameViewModel: GameViewModel = viewModel()

            var currentTab by remember { mutableIntStateOf(0) }
            val selectedMachine = remember(currentTab) { mutableStateOf<Machine?>(null) }

            val showMachineSelectionDialog = currentTab == 1 && gameViewModel.state.currentWorkshopMachine == null

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentTab == 0,
                            onClick = { currentTab = 0 },
                            label = { Text("Autel") },
                            icon = { Icon(Icons.Default.Star, "Autel") }
                        )
                        NavigationBarItem(
                            selected = currentTab == 1,
                            onClick = {
                                if (currentTab == 1) {
                                    selectedMachine.value = null // Force la réouverture de la pop-up
                                } else {
                                    currentTab = 1
                                }
                            },
                            label = { Text("Atelier") },
                            icon = { Icon(Icons.Default.Build, "Atelier") }
                        )
                        NavigationBarItem(
                            selected = currentTab == 2,
                            onClick = { currentTab = 2 },
                            label = { Text("Jardin") },
                            icon = { Icon(Icons.Default.PlayArrow, "Jardin") }
                        )
                        NavigationBarItem(
                            selected = currentTab == 3,
                            onClick = { currentTab = 3 },
                            label = { Text("Sac") },
                            icon = { Icon(Icons.Default.List, "Inventaire") }
                        )
                        NavigationBarItem(
                            selected = currentTab == 4, // Nouvel index
                            onClick = { currentTab = 4 },
                            label = { Text("Marché") },
                            icon = { Icon(Icons.Default.ShoppingCart, "Vendre") } // Utilise ShoppingCart
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (currentTab) {
                        0 -> StatueScreen(gameViewModel)
                        1 -> {
                            // Si aucune machine n'est sélectionnée localement, on montre la pop-up
                            if (selectedMachine.value == null) {
                                SelectionDialog(
                                    title = "Quelle machine utiliser ?",
                                    options = GameDatabase.machines,
                                    onSelect = { machine ->
                                        selectedMachine.value = machine as Machine
                                    },
                                    onDismiss = { currentTab = 0 } // Retour si annulation
                                )
                            } else {
                                // Si une machine est sélectionnée, on affiche son écran
                                MachineScreen(
                                    machine = selectedMachine.value!!,
                                    viewModel = gameViewModel,
                                    onBack = { selectedMachine.value = null } // Revient à la sélection
                                )
                            }
                        }
                        2 -> ExplorationScreen(gameViewModel)
                        3 -> InventoryScreen(gameViewModel)
                        4 -> MarketScreen(gameViewModel)
                    }
                }
            }
            IconActionButton(
                icon = Icons.Default.Settings,
                title = "Menu"
            ) {
                // Voici ce qu'il y aura dans la popup
                Button(
                    onClick = { /* Ta logique */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sauvegarder")
                }
                Button(
                    onClick = { /* Ta logique */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Quitter")
                }
            }
            if(showMachineSelectionDialog){
                SelectionDialog(
                    title = "Voyager vers...",
                    options = GameDatabase.machines,
                    onSelect = { selectedMachine ->
                        gameViewModel.changeCurrentMachine(selectedMachine as Machine)
                    },
                    // Si on ferme le dialogue, on retourne à l'onglet principal (Autel)
                    onDismiss = { gameViewModel.changeCurrentMachine(null) }
                )
            }
        }
    }
}

