package ru.amalkoott.advtapp.domain.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.amalkoott.advtapp.domain.entities.Advert

class AdvertsListConverter {
    @TypeConverter
    fun fromString(value: String?): List<Advert>? {
        val listType = object : TypeToken<List<Advert>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Advert>?): String? {
        return Gson().toJson(list)
    }
}