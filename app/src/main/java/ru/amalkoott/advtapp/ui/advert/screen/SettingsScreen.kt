package ru.amalkoott.advtapp.ui.advert.screen

import android.view.animation.AlphaAnimation
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.R
import ru.amalkoott.advtapp.di.AppModule

@Composable
fun PrintSettings(openBlackList:()->Unit,token: State<Boolean>){
    // какие настройки будем хранить?
    // - тема (темная/светлая)
    val context = LocalContext.current
   // val token = AppModule.provideAppPreferences(LocalContext.current).getAccessToken.collectAsState(initial = true)
    val theme = remember { mutableStateOf(token.value) }
    val isNotifyOn = remember { mutableStateOf(true) }
    val isNotifyOnDay = remember { mutableStateOf(false) }
    val isPushOn = remember { mutableStateOf(false) }

    Column(Modifier.padding(top = 68.dp, start = 32.dp, end = 32.dp)) {
        // todo смена тем
        Row(Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween)
        {

            Column(
                Modifier
                    .weight(6f)
                    .padding(end = 16.dp)) {
                Row(
                  horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Тема")
                    /*
                    Text(text = if (theme.value) "Светлая" else "Темная",
                        modifier = Modifier.padding(start = 24.dp,top = 4.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp)
                    */
                }
                Text(text = "Установите светлую или темную тему",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp)
            }

            Switch(
                checked = theme.value,
                onCheckedChange = {
                    theme.value = it
                    CoroutineScope(Dispatchers.IO).launch {
                        AppModule.provideAppPreferences(context).setTheme(it)
                    }
                },
                colors = SwitchColors(
                    checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    checkedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    checkedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkedThumbColor = MaterialTheme.colorScheme.onSurface,

                    uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,

                    disabledCheckedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledCheckedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledCheckedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledCheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,

                    disabledUncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledUncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledUncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledUncheckedTrackColor = MaterialTheme.colorScheme.surface,),
                thumbContent = if (theme.value) {
                    {
                        Icon(painter = painterResource(R.drawable.ic_light_mode), contentDescription = "Светлая тема", tint = MaterialTheme.colorScheme.primaryContainer)
                    }
                } else {
                    { Icon(painter = painterResource(R.drawable.ic_dark_mode), contentDescription = "Темная тема") }
                }
            )
        }

        val notifyOnOff = "${if (isNotifyOn.value) "Отключает" else "Включает"} все уведомления приложения"
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column(
                    Modifier
                        .weight(6f)
                        .padding(end = 16.dp)) {
                    Text(text = "Уведомления",modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
                    Text(text = notifyOnOff,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp)
                }
                Switch(
                    checked = isNotifyOn.value,
                    colors = SwitchColors(
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        checkedThumbColor = MaterialTheme.colorScheme.onSurface,

                        uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,

                        disabledCheckedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledCheckedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledCheckedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledCheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,

                        disabledUncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledUncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledUncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledUncheckedTrackColor = MaterialTheme.colorScheme.surface,),
                    onCheckedChange = {
                        isNotifyOn.value = it
                    },
                    modifier = Modifier.weight(1f)
                )
            }

        }

        Column() {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(
                    Modifier
                        .weight(6f)
                        .padding(end = 16.dp)) {
                    Text(text = "Уведомления в интервале", modifier = Modifier.padding(end = 16.dp,top = 4.dp, bottom = 4.dp))
                    if (isNotifyOnDay.value){
                        Row {
                            Text(text = "oje")
                            Text(text = "jfiw")
                        }
                    }
                    Text(text = "Уведомления будут приходить только в указанное время суток",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp)
                }

                Switch(
                    checked = isNotifyOnDay.value,
                    colors = SwitchColors(
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedThumbColor = MaterialTheme.colorScheme.onSurface,

                        uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,

                        disabledCheckedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledCheckedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledCheckedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledCheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,

                        disabledUncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledUncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledUncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledUncheckedTrackColor = MaterialTheme.colorScheme.surface,),
                    onCheckedChange = {
                        isNotifyOnDay.value = it
                    },
                    modifier = Modifier.weight(1f)
                )
            }

        }

        Column() {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(
                    Modifier
                        .weight(6f)
                        .padding(end = 16.dp)) {
                    Text(text = "Push-уведомления", modifier = Modifier.padding(end = 16.dp,top = 4.dp, bottom = 4.dp))
                    Text(text = "Сохраняйте все уведомления",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp)
                }

                Switch(
                    checked = isPushOn.value,

                    colors = SwitchColors(
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        checkedThumbColor = MaterialTheme.colorScheme.onSurface,

                        uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,

                        disabledCheckedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledCheckedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledCheckedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledCheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,

                        disabledUncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledUncheckedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledUncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledUncheckedTrackColor = MaterialTheme.colorScheme.surface,),
                    onCheckedChange = {
                        isPushOn.value = it
                    },
                    modifier = Modifier.weight(1f)
                )
            }

        }


        Column(
            Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .clickable { openBlackList() }) {
            Text(text = "Черный список", modifier = Modifier.padding(end = 16.dp,top = 4.dp, bottom = 4.dp))
            Text(text = "Удаленные из подборок объявления",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp)
        }



    }

    // - уведомления:
    //  - включить/отключить уведомления приложения (для конкретных подборок включение/отключение увед настраивается внутри подборки, при этом пуши сохраняются),
    //  - уведомление в определенное время (присылать уведомления в определенное время суток для всех подборок)
    //  - отключить пуши (уведомления не сохраняются (если оповещения включены - на устройство приходит))
    // - черный список (просмотр и удаление из него)
}

/*
@Composable
private fun SettingSwitchItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    @StringRes title: Int,
    @StringRes description: Int,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val contentAlpha = if (enabled) Alpha.high else ContentAlpha.disabled

            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                modifier = Modifier.alpha(contentAlpha)
            )
            Text(
                text = stringResource(id = description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(contentAlpha)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled
        )
    }
}


*/