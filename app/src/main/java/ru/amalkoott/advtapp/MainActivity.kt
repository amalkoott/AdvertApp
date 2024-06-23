package ru.amalkoott.advtapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import ru.amalkoott.advtapp.di.AppModule
import ru.amalkoott.advtapp.domain.Constants
import ru.amalkoott.advtapp.domain.preferenceTools.AppPreferences
import ru.amalkoott.advtapp.ui.advert.screen.AdSetScreen
import ru.amalkoott.advtapp.ui.advert.view.AppViewModel
import ru.amalkoott.advtapp.ui.theme.AdvtAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val preferences = mapOf(
                Constants.APP_THEME to AppModule.provideAppPreferences(LocalContext.current).getAccessToken.collectAsState(initial = true),
                Constants.APP_NOTIFICATIONS to AppModule.provideAppPreferences(LocalContext.current).getNotificationToken.collectAsState(initial = true),
                Constants.APP_DAILY_NOTIFICATIONS to AppModule.provideAppPreferences(LocalContext.current).getDailyNotificationToken.collectAsState(initial = true),
                Constants.APP_PUSHES to AppModule.provideAppPreferences(LocalContext.current).getPushesToken.collectAsState(initial = true),
            )
            val token = AppModule.provideAppPreferences(LocalContext.current).getAccessToken.collectAsState(initial = true)
            val theme by remember { mutableStateOf(preferences[Constants.APP_THEME])}
            AdvtAppTheme(darkTheme = !theme!!.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdSetScreen(appViewModel,token)
                }
            }
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                appViewModel.onBackClick()
            }
        })
    }
}
//@TODO splash screen https://www.google.com/search?q=splash+screen+compose+android&oq=splash+screen+comp&gs_lcrp=EgZjaHJvbWUqDAgCEAAYFBiHAhiABDIHCAAQABiABDIGCAEQRRg5MgwIAhAAGBQYhwIYgAQyBwgDEAAYgAQyCAgEEAAYFhgeMggIBRAAGBYYHjIKCAYQABgPGBYYHjIKCAcQABgPGBYYHjIICAgQABgWGB4yCAgJEAAYFhge0gEIOTQxOWowajeoAgCwAgA&sourceid=chrome&ie=UTF-8#fpstate=ive&vld=cid:57e4cdbe,vid:VTRz-8DPowM,st:0

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdvtAppTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .padding(top = 60.dp)
                .padding(6.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(3) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                        .clickable {
                            // появляется выбранная подборка, клик - вывод списка объявлений подборки
                            //selected.value = note
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .background(color = Color.Green)
                                .fillMaxWidth(fraction = 1f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(text = "set.name")
                            Text(text = "6")
                        }
                        Icon(Icons.Filled.Menu,
                            modifier = Modifier
                                .padding(start = 100.dp),
                            contentDescription = "Настройки подборки")

                    }
                    Text(text = "Объявлений: " + "set.adverts.count().toString()")
                    Row {
                        Text(text = "Последнее обновление: " + "set.last_update.toString()")
                    }


                }
            }
        }
    }
}
