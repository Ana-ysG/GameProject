package com.example.mygame.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mygame.ui.components.BackButton
import com.example.mygame.viewmodel.GameViewModel

@Composable
fun PNJConversationScreen(viewModel: GameViewModel, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        BackButton(onBack)
        Text("Il n'y a personne pour l'instant")
    }


}