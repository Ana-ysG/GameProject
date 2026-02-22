package com.example.mygame.ui.components

import android.R.attr.name
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygame.data.ActionZone
import com.example.mygame.ui.screens.CraftCard
import com.example.mygame.ui.theme.GameText
import java.time.temporal.TemporalQueries.zone


@Composable
fun ActionZoneComposant(
    zone : ActionZone,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
){
    ZoneComposant(
        name = zone.name,
        iconResId = zone.iconResId,
        modifier = modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                LinearProgressIndicator(
                    progress = { 0.4f },
                    modifier = Modifier.fillMaxSize(),
                    color =  MaterialTheme.colorScheme.primary,
                    trackColor =  MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Butt
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier,
                //.fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                Spacer(modifier = Modifier.weight(1f))
                GameText.ZoneTitle("Lv 1")
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(18.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    LinearProgressIndicator(
                        progress = { 0.4f },
                        modifier = Modifier.fillMaxSize(),
                        color =  MaterialTheme.colorScheme.primary,
                        trackColor =  MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Butt
                    )
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth(0.8f),
                content = content,
            )

        }
    }
}

@Composable
fun ZoneComposant(
    name: String,
    iconResId : Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer(modifier = modifier.height(16.dp))
        GameText.AreaTitle(text = name, color = MaterialTheme.colorScheme.primary)

        EnvironmentDisplay(iconResId)

        Column(
            modifier = modifier.fillMaxWidth(0.8f),
            content = content,
            horizontalAlignment = Alignment.CenterHorizontally
        )
    }
}