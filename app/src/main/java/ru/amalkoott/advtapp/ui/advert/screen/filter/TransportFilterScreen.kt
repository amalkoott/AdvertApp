package ru.amalkoott.advtapp.ui.advert.screen.filter

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun TransportFilter() {
    val context = LocalContext.current
    Toast.makeText(context, "Скоро будет добавлено!", Toast.LENGTH_SHORT).show()
}