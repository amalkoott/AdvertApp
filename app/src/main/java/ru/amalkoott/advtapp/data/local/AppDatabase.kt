package ru.amalkoott.advtapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.BlackList
import ru.amalkoott.advtapp.domain.typeconverter.AdvertsListConverter
import ru.amalkoott.advtapp.domain.typeconverter.DateConverter

// это БДшка
@Database(
    entities = [AdSet::class, Advert::class, BlackList::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(AdvertsListConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun notesDao(): AppDao

}