package ru.amalkoott.advtapp

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.amalkoott.advtapp.data.local.AppDatabase
import ru.amalkoott.advtapp.data.local.AppRepositoryDB
import ru.amalkoott.advtapp.data.remote.ServerAPI
import ru.amalkoott.advtapp.data.remote.ServerRequestsRepository
import ru.amalkoott.advtapp.domain.AppUseCase
import ru.amalkoott.advtapp.ui.advert.screen.AdSetScreen
import ru.amalkoott.advtapp.ui.advert.view.AppViewModel
import ru.amalkoott.advtapp.ui.theme.AdvtAppTheme

class MainActivity : ComponentActivity() {
    var httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer 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"
                )
                .build()
            chain.proceed(request)
        }
        .build()


    var retrofit = Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8080/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    var api = ServerRequestsRepository(retrofit.create(ServerAPI::class.java))
    private val database: AppDatabase by lazy{
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,"app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    private val notesRepo by lazy { AppRepositoryDB(database.notesDao()) }
    private  val notesUseCase by lazy { AppUseCase(notesRepo,api) }
    private val appViewModel: AppViewModel by viewModels{
        viewModelFactory {
            initializer {
                AppViewModel(notesUseCase)
            }
        }
    }
    //private val appViewModel = AppViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvtAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdSetScreen(appViewModel)
                }
            }
        }
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
