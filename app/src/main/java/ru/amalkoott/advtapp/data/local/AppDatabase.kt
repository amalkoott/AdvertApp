package ru.amalkoott.advtapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.amalkoott.advtapp.domain.entities.AdSet
import ru.amalkoott.advtapp.domain.entities.Advert
import ru.amalkoott.advtapp.domain.entities.BlackList
import ru.amalkoott.advtapp.domain.typeconverter.AdvertsListConverter
import ru.amalkoott.advtapp.domain.typeconverter.DateConverter

@Database(
    entities = [AdSet::class, Advert::class, BlackList::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(AdvertsListConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun notesDao(): AppDao

}