package ru.amalkoott.advtapp.domain

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters

interface AppRemoteRepository {
    // получение новой подборки
    suspend fun get(parameters: RealEstateSearchParameters): List<Advert>?

    suspend fun get(json: JsonElement): List<Advert>?
    suspend fun checkServer(): JsonObject?
}
