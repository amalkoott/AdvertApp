package ru.amalkoott.advtapp.ui.advert.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.domain.AdSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropMenu(onDeleteSet: ()-> Unit, onUpdateSet: ()-> Unit, setContext: (Context)-> Unit){
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(onClick = {
        coroutineScope.launch {
            expanded = !expanded
        }
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Localized description",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
    DropdownMenu(
        containerColor = MaterialTheme.colorScheme.surface,
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Удалить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить асинхронную операцию, например, вызов удаления
                    Toast.makeText(context, "Подборка удалена", Toast.LENGTH_SHORT).show()
                    setContext(context)
                    onDeleteSet()
                }
            }
        )
        DropdownMenuItem(
            text = { Text("Обновить") },
            onClick = {
                coroutineScope.launch {
                    Toast.makeText(context, "Пробуем обновить...", Toast.LENGTH_SHORT).show()
                    setContext(context)
                    onUpdateSet()
                }
            }
        )
        val tooltipState = rememberTooltipState(isPersistent = true)
        val scope = rememberCoroutineScope()
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                RichTooltip(
                    colors = RichTooltipColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionContentColor =  MaterialTheme.colorScheme.tertiary
                    ),
                    title = { Text("Объявление рядом") },
                    action = {
                        TextButton(
                            onClick = { scope.launch { tooltipState.dismiss() } }
                        ) { Text("Подробнее", color =  MaterialTheme.colorScheme.tertiary) }
                    }
                ) {
                    Text("Узнайте, что вы находитесь рядом с объявлениями из этой подборки!")
                }
            },
            state = tooltipState
        ) {
            DropdownMenuItem(
                text = { Text("Я рядом",)  },

                onClick = {
                    coroutineScope.launch {
                        Toast.makeText(context, "Режим \"Я рядом\" включен для подборки!", Toast.LENGTH_SHORT).show()
                        setContext(context)
                        onUpdateSet()
                    }
                }
            )
        }

        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    Text(text = "Сейчас {вкл} для этой подборки", fontSize = 14.sp)
                }
            },
            state = rememberTooltipState()
        ) {
            DropdownMenuItem(
                text = { Text("Уведомления") },
                onClick = {
                    coroutineScope.launch {
                        Toast.makeText(context, "Уведомления включены/отключены!", Toast.LENGTH_SHORT).show()
                        setContext(context)
                        onUpdateSet()
                    }
                }
            )
        }

    }
}


@Composable
fun DropMenu(onDeleteAd: ()-> Unit){
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(onClick = {
        coroutineScope.launch {
            expanded = !expanded
        }
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Localized description",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
    DropdownMenu(
        containerColor = MaterialTheme.colorScheme.surface,
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Удалить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить асинхронную операцию, например, вызов удаления
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                    onDeleteAd()
                }
            }
        )
    }
}
