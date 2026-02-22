package com.example.mygame.ui.components

import android.R.attr.enabled
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StandardButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit){
    Button(
        enabled = enabled,
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text)
    }
}

@Composable
fun AutoActionButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit){
    Button(
        enabled = enabled,
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text)
    }
}
@Composable
fun BackButton(onBack: () -> Unit) {
    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Retour") }
}
@Composable
fun AreaButton(label: String, icon: ImageVector,enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(12.dp))
        Text(label)
    }
}