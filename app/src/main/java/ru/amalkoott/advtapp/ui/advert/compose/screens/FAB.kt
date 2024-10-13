package ru.amalkoott.advtapp.ui.advert.compose.screens

import android.content.Context
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import ru.amalkoott.advtapp.domain.entities.AdSet

@Composable
fun DrawFAB(
    selected: MutableState<AdSet?>,
    context: Context,
    setContextValue:(Context)-> Unit,
    onEditComplete:() -> Unit,
    onAddSetClicked:() -> Unit
    ){
    FloatingActionButton(
        onClick = {
            if(selected.value != null ){
                // сохраняем
                setContextValue(context)
                onEditComplete()

            }else{
                // Добавляем
                setContextValue(context)
                onAddSetClicked()
            }

        },
        contentColor = MaterialTheme.colorScheme.onTertiary,
        containerColor = MaterialTheme.colorScheme.secondary,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        content = {
            if(selected.value != null) Icon(Icons.Filled.Done, contentDescription = "Сохранить",
                tint = MaterialTheme.colorScheme.onTertiary)
            else Icon(Icons.Filled.Add, contentDescription = "Добавить",
                tint = MaterialTheme.colorScheme.onTertiary) }
    )
}