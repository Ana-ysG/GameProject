package com.example.mygame.ui.PopUp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun IconActionButton(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    // État pour savoir si la popup est ouverte
    var showDialog by remember { mutableStateOf(false) }

    // 1. L'icône sur laquelle on clique
    IconButton(
        onClick = { showDialog = true },
        modifier = Modifier
            .padding(4.dp)
    ) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.onPrimaryContainer)
    }

    // 2. La Popup (Dialog) qui apparaît par-dessus
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            // Le design de la carte popup
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // On insère le contenu personnalisé ici
                    content()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bouton pour fermer la popup
                    TextButton(onClick = { showDialog = false }) {
                        Text("Fermer")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDialogContent() {
    MaterialTheme {
        // On simule le contenu de la Card pour voir le design directement
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "titre",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // On insère le contenu personnalisé ici
                //content()
                Button(onClick = {}) { Text("content") }

                Spacer(modifier = Modifier.height(16.dp))

                // Bouton pour fermer la popup
                TextButton(onClick = { }){//showDialog = false }) {
                    Text("Fermer")
                }
            }
        }
    }
}