package ru.amalkoott.advtapp.ui.advert.screen.filterScreen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ServiceFilter(){
    val context = LocalContext.current
    Toast.makeText(context, "Это обязательно будет добавлено!", Toast.LENGTH_SHORT).show()
}