package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

        val aboutApp = "Приложение Advert App создано для отслеживания объявлений с разных площадок, таких как Авито, ЦИАН, Домлкик и прочие. Теперь вам не нужно посещать каждый сайт в надежде найти лучшее объявление, вам достаточно открыть Advert App!"
        TextPart(title = "Advert App", text = aboutApp, modifier = partModifier)

        Column(modifier = partModifier) {
            val aboutSet = "Подборкой в приложении называется набор объявлений, которые были найдены на отслеживаемых сайтах по вашим параметрам поиска." +
                    "У подборки есть несколько характеристик:"
            TextPart(title = "Подборка", text = aboutSet, modifier = inpartModifier)

            val aboutName = "Это название задается вами. Рекомендуем именовать подборку коротко и понятно, потому что в дальнейшем для взаимодействия с ней используется имя. Вы всегда можете изменить название существующей подборки в меню просмотра."
            val aboutUpdating = "Подборка будет обновляться один раз в указанный временной интервал. Чем больше временной интервал, тем меньше энергопотребление приложения. В связи с внутренними настройками Android-систем некоторые фоновые процессы могут прекращаться до их окончания. Поэтому не рекомендуем использовать временные интервалы 5 и 10 минут в режиме энергосбережения - обновление подборок с такой частотой не гарантирована!"
            val aboutCaption = "Описание формируется системой после создания подборки на основе указанных фильтров, поэтому вы не можете его изменить."
            val aboutAds = "Все найденные по фильтрам объявления будут отображаться в этом разделе подборки. Объявления могут исчезать из списка после очередного обновления подборки - это значит, что публикация была снята. Вы можете добавить объявление в список избранного, для этого нажмите серечко. Чтобы удалить объявление из подборки - нажмите на корзинку - объявление поместится в черный список подборки и при обновлении будет игнорироваться. Редактировать черный список можно в настройках."
            val aboutGeo = "Режим предназначен для отслеживания нахождения пользователя в радиусе адреса, указанном в объявлении. Если режми установлен для всей подборки, то отслеживаются адреса из всех ее объявлений. Помните! Отслеживание местоположения повышает энергопотребление! Поэтому вы всегда можете редактировать работу режима \"Я рядом\" для конкретной публикации, для этого нажмите знак Гео."

            TextInpart(title = "Название подборки", text = aboutName, modifier = inpartModifier,)
            TextInpart(title = "Интервал обновления", text = aboutUpdating, modifier = inpartModifier,)
            TextInpart(title = "Описание", text = aboutCaption, modifier = inpartModifier,)
            TextInpart(title = "Список объявлений", text = aboutAds, modifier = inpartModifier,)
            Spacer(modifier = Modifier.height(16.dp))
            TextPart(title = "Режим \"Я рядом\"", text = aboutGeo, modifier = inpartModifier,)
        }
        val aboutSetCreate = "В меню создания подборки указываются параметры поиска, по которым в дальнейшем будет обновлять будущая подборка. Помните, что вы не сможете сохранить подборку без названия."
        TextPart(title = "Создание подборки", text = aboutSetCreate, modifier = partModifier)

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