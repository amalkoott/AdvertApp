package ru.amalkoott.advtapp.ui.advert

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrintSettings(){
    Text(text = "Settings", modifier = Modifier.padding(top = 60.dp))
}