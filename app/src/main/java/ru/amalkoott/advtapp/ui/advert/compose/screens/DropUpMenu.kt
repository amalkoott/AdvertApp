package ru.amalkoott.advtapp.ui.advert.compose.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun DropUpMenu(items: Map<String,String>){
    val context = LocalContext.current
    var menuOpen by remember { mutableStateOf(false) }
    val offsetAnimation: Dp by animateDpAsState(
        if (menuOpen) 5.dp else 300.dp,
        spring(stiffness = Spring.StiffnessLow), label = ""
    )

    val btnModifier = Modifier
        .width(200.dp)
        .height(50.dp)

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { menuOpen = !menuOpen }, btnModifier,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                disabledContainerColor = Color.Blue, // todo пофиксить цвета
                disabledContentColor = Color.White,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp
            )
        ) {
            if(menuOpen) Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Close")
            else Text(text = "Смотреть на сайте")
        }
        AnimatedVisibility(
            visible = menuOpen,
            modifier = Modifier
                .offset(y = offsetAnimation)
        ) {
            Column {
                items.entries.forEach {
                    Button(
                        onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.value))
                        context.startActivity(browserIntent)},
                        btnModifier.padding(bottom = 6.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp
                        )) {
                        Text(text = it.key)
                    }
                }

            }
        }
    }

}