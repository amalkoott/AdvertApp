package ru.amalkoott.advtapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.amalkoott.advtapp.data.local.AppDao
import ru.amalkoott.advtapp.data.local.AppDatabase
import ru.amalkoott.advtapp.data.remote.ServerAPI
import ru.amalkoott.advtapp.data.remote.ServerRequestsRepository
import ru.amalkoott.advtapp.domain.preference.AppPreferences
import ru.amalkoott.advtapp.domain.worker.StartWorker
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer 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"
                    )
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideInternetConnection(httpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            //.baseUrl("http://192.168.56.1:8080") // avd
            //.baseUrl("http://10.0.2.2:8080") // genymotion
            .baseUrl("http://185.178.47.135:8080")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun provideServerApi(retrofit: Retrofit): ServerRequestsRepository{
        return ServerRequestsRepository(retrofit.create(ServerAPI::class.java))
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,"app_database"
        ).fallbackToDestructiveMigration()
            .build()
        return db
    }

    @Singleton
    @Provides
    fun provideAppDao(db: AppDatabase): AppDao{
        return db.notesDao()
    }

    @Singleton
    @Provides
    fun provideStartWorker(@ApplicationContext context: Context):StartWorker{
        return StartWorker(context)
    }

    @Singleton
    @Provides
    fun provideAppPreferences(@ApplicationContext context: Context):AppPreferences{
        return AppPreferences(context)
    }
}