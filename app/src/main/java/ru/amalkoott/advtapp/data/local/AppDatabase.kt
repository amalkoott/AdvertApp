package ru.amalkoott.advtapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.typeconverter.AdvertsListConverter
import ru.amalkoott.advtapp.domain.typeconverter.DateConverter

// это БДшка
@Database(
    entities = [AdSet::class, Advert::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(AdvertsListConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun notesDao(): AppDao

}