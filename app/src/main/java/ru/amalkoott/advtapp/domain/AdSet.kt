package ru.amalkoott.advtapp.domain

import android.util.Log
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import java.time.LocalDate

@Entity
class AdSet(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = "undefined_name",
    var adverts: List<Advert>? = null,
    var update_interval: Int? = null,
    var caption: String? = "empty_caption",
    var category: SetCategory? = null,
    var last_update: LocalDate? = null
) {
    fun getSearchParameters(): RealEstateSearchParameters{
        val parameters = RealEstateSearchParameters()
        val gson = Gson()
        val jelem: JsonElement = gson.fromJson<JsonElement>(this.caption, JsonElement::class.java)
        val json = jelem.asJsonObject

        try {
            parameters.category = json["category"]?.toString()
            parameters.city = json["city"]?.toString()
            parameters.dealType = json["dealType"]?.toString()
            parameters.livingType = json["livingType"]?.toString()
            parameters.priceType = json["priceType"]?.toString()
            // TODO дописать для остальных атрибутов ...

        }catch (e:Exception){
            Log.w("RemoteUpdateSetError",e.message!!)
        }
        return parameters
    }
}

data class AdSetWithAdverts(
    @Embedded val adSet: AdSet,
    @Relation(
        parentColumn = "id", // из таблицы AdSet
        entityColumn = "adSetId" // из таблицы Advert
    )
    val adverts: List<Advert>
)