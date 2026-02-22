package com.example.mygame.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.mygame.data.SelectableEntity
import com.example.mygame.ui.PopUp.IconActionButton

@Composable
fun ElementSelectionGrid(
    options: List<SelectableEntity>,
    // On passe l'entité complète à la lambda du bouton
    button: @Composable (item: SelectableEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(options) { entity ->
            // La grille passe l'entité actuelle au "moule" du bouton
            button(entity)
        }
    }
}

@Composable
fun ActionGrid(
    options: List<SelectableEntity>,
    popupContent: @Composable (SelectableEntity) -> Unit // On ajoute ce paramètre
) {
    ElementSelectionGrid(
        options = options,
        button = { item ->
            IconActionButton(
                icon = item.icon,
                title = item.name,
                content = { popupContent(item) } // On injecte le contenu ici
            )
        }
    )
}

@Composable
fun SelectionGrid(
    options: List<SelectableEntity>,
    onSelect: (SelectableEntity) -> Unit
) {
    ElementSelectionGrid(
        options = options,
        button = { item ->
            IconButton(
                onClick = { onSelect(item) },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    modifier = Modifier.size(48.dp),
                )
            }
        }
    )
}