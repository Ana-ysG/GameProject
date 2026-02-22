package com.example.mygame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygame.viewmodel.GameViewModel

@Composable
fun StatueScreen(viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "L'Autel Ancien", fontSize = 28.sp)
        Text(text = "Une divinité semble attendre une offrande...", fontSize = 14.sp)

        Spacer(modifier = Modifier.height(40.dp))

        // Affichage du Mana en grand
        Text(text = "${viewModel.state.mana}", fontSize = 48.sp, color = MaterialTheme.colorScheme.primary)
        Text(text = "Mana", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(60.dp))

        // L'Orbe (Le bouton principal)
        Button(
            onClick = { viewModel.clickOrb() },
            modifier = Modifier.size(180.dp),
            shape = CircleShape // Un bouton tout rond
        ) {
            Text("Prier", fontSize = 20.sp)
        }
    }
}