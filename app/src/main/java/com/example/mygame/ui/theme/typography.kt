package com.example.mygame.ui.theme

import android.R.attr.fontWeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object GameText {
    // Le titre tout en haut de l'écran (ex: "FORÊT MYSTIQUE")
    @Composable
    fun AreaTitle(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(30.dp))
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
            )
        }
    }

    // Le titre d'un bloc ou d'une carte (ex: "Enclume de Fer")
    @Composable
    fun ZoneTitle(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(20.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = modifier
            )
        }
    }

    @Composable
    fun QuestTitle(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(16.dp))
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = modifier)

        }
    }
    @Composable
    fun QuestText(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null) {
        if (icon != null) Icon(icon, null, modifier = Modifier.size(16.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier,
            color = color
        )
    }
    @Composable
    fun QuestDescription(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null) {
        if (icon != null) Icon(icon, null, modifier = Modifier.size(16.dp))
        Text(
            text =text,
            fontSize = 14.sp,
            modifier = modifier,
            color = color)
    }

    @Composable
    fun Standardtitle(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null){
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(20.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = modifier
            )
        }
    }

    // Pour les chiffres importants (ex: "1,240 Gold")
    @Composable
    fun ResourceAmount(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = color,
                modifier = modifier
            )
        }
    }

    // Petites descriptions (ex: "Produit 2 bois/sec")
    @Composable
    fun HelperText(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                modifier = modifier
            )
        }
    }
    @Composable
    fun PopUpTitle(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified, icon: ImageVector? = null){
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = color,
                modifier = modifier
            )
        }
    }
}