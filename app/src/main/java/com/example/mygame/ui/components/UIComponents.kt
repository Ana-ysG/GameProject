package com.example.mygame.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mygame.ui.theme.GameText

@Composable
fun EnvironmentDisplay(imageRes: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(), // La carte s'adapte à son contenu au lieu de tout prendre
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray) // Fond de la carte
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            // ContentScale.Fit garantit que l'image entière est visible sans être déformée
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(250.dp)          // Prend toute la largeur disponible
                .height(350.dp)           // Fixe la hauteur pour stabiliser l'UI
                .padding(24.dp)           // Marge autour pour ne pas coller aux bords
                .clip(RoundedCornerShape(16.dp)) // Optionnel : arrondit les bords de l'image
        )
    }
}

@Composable
fun ResourceRequirementText(
    icon: ImageVector,    // On passe l'icône ici
    current: Long,
    required: Long,
) {
    val isMet = current >= required
    val color = if (isMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        // Affichage de l'icône
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Affichage du ratio
        GameText.ResourceAmount(text = "$current/$required", color = color)

    }
}


@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    backgroundImage: Int? = null, // Paramètre optionnel (ex: R.drawable.forêt)
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // 1. L'image de fond (si elle existe)
            if (backgroundImage != null) {
                Image(
                    painter = painterResource(id = backgroundImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Remplit toute la carte
                    modifier = Modifier.matchParentSize() // Prend toute la taille de la Box
                )
                // 2. Un voile sombre pour la lisibilité du texte
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
            }

            // 3. Le contenu textuel
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

@Composable
fun VerticalGameCard(
    modifier: Modifier = Modifier,
    backgroundImage: Int? = null,
    height: Dp = 240.dp,
    width: Dp = 160.dp, // Largeur par défaut
    content: @Composable ColumnScope.() -> Unit
) {
    GameCard(
        modifier = modifier
            .width(width) // On impose la largeur
            .height(height), // On impose une hauteur pour le format portrait
        backgroundImage = backgroundImage,
        content = content
    )
}

@Composable
fun ResourceInfo(label: String, value: String, icon: ImageVector? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) Icon(icon, null, modifier = Modifier.size(16.dp))
        GameText.HelperText("$label : ")
        GameText.HelperText(value, color = MaterialTheme.colorScheme.primary)
    }
}

@Preview(showBackground = true)
@Composable
fun CardsDemoPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        ResourceInfo(label = "Or", value = "1000", icon = Icons.Default.Build)
        GameText.ResourceAmount("Test", color = Color.Red, icon = Icons.Default.Build)

        Text("Variante Horizontale (Large) :")
        GameCard(backgroundImage = android.R.drawable.ic_dialog_map) {
            Text("Forêt Mystérieuse", color = Color.White, style = MaterialTheme.typography.titleLarge)
            Text("Difficulté : ⭐⭐", color = Color.White)
            Text("Difficulté : ⭐⭐", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Variante Verticale (Portrait) :")
        Row {
            VerticalGameCard(backgroundImage = android.R.drawable.ic_menu_gallery) {
                Text("Guerrier", color = Color.White)
                Spacer(Modifier.weight(1f))
                Text("LVL 10", color = Color.Cyan)
            }
            VerticalGameCard {
                Text("Mage")
                Spacer(Modifier.weight(1f))
                Text("LVL 12", color = Color.Magenta)
            }
        }
    }
}
