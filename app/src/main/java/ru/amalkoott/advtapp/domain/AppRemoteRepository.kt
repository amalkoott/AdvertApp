package ru.amalkoott.advtapp.domain

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.domain.entities.Advert

interface AppRemoteRepository {
    suspend fun get(parameters: RealEstateSearchParameters): List<Advert>?
    suspend fun get(json: JsonElement): List<Advert>?
    suspend fun checkServer(): JsonObject?
}
