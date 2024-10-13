package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.amalkoott.advtapp.domain.Constants

@Composable
fun PrintHowItWorkScreen()
{
    val scrollState = rememberScrollState()
    val partModifier = Modifier.padding(vertical = 16.dp)
    val inpartModifier = Modifier.padding(vertical = 4.dp)
    Column(
        Modifier
            .padding(top = 68.dp)
            .padding(start = 32.dp, end = 32.dp, top = 16.dp)
            .verticalScroll(scrollState)) {

        TextPart(title = "AdSpider", text = Constants.ABOUT_APP, modifier = partModifier)

        Column(modifier = partModifier) {
            TextPart(title = "Подборка", text = Constants.ABOUT_SET, modifier = inpartModifier)
            TextInpart(title = "Название подборки", text = Constants.ABOUT_NAME, modifier = inpartModifier,)
            TextInpart(title = "Интервал обновления", text = Constants.ABOUT_UPDATING, modifier = inpartModifier,)
            TextInpart(title = "Описание", text = Constants.ABOUT_CAPTION, modifier = inpartModifier,)
            TextInpart(title = "Список объявлений", text = Constants.ABOUT_ADS, modifier = inpartModifier,)
            Spacer(modifier = Modifier.height(16.dp))
            TextPart(title = "Режим \"Я рядом\"", text = Constants.ABOUT_GEO, modifier = inpartModifier,)
        }
        TextPart(title = "Создание подборки", text = Constants.ABOUT_SET_CREATE, modifier = partModifier)

    }

}

@Composable
private fun TextPart(title: String, text: String, modifier: Modifier){
    Column(modifier = modifier) {
        Text( text = title, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text( text = text, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
@Composable
private fun TextInpart(title: String, text: String, modifier: Modifier){
    val visib = remember {
        mutableStateOf(false)
    }

    val offsetAnimation: Dp by animateDpAsState(
        if (visib.value) 0.dp else 20.dp,
        spring(stiffness = Spring.StiffnessLow), label = ""
    )

    TextButton(onClick = {visib.value = !visib.value}, modifier ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text( text = title,
                fontWeight = if (visib.value) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 4.dp))
            if(visib.value)
                Icon(Icons.Filled.KeyboardArrowUp,contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface)
            else
                Icon(Icons.Filled.KeyboardArrowDown,contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

    AnimatedVisibility(visible = visib.value, Modifier.offset(offsetAnimation) ) {
        Column() {
            Text( fontSize = 16.sp, text = text, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}