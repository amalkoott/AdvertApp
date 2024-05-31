package ru.amalkoott.advtapp.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


// мы не можем не создавать BlackList тк при удалении объявления оно удаляется из БД -- его надо помещать куда-то
// когда удаляется объявление - оно добавляется в BlackList
// при обновлении подборки мы сможем проверить объявление через сохраненный в BlackList idSet
@Entity
class BlackList(
    @PrimaryKey(autoGenerate = false)
    var id: Long? = null,
    val name: String? = "undefined_name",
    val description: String? = "empty_description",
    val price: String? = null,
    val priceInfo: String? = null,
    val location: String? = "indefined_location",
    val address: String? = "undefined_address",
    val lat: Double? = null,
    val lon: Double? = null,
    val url: String? = "undefined_URL",
    val imagesURL: String? = "empty_imageURL",
    val additionalParam: String?,//AdditionalParameters? = null,
    var adSetId: Long?, // ID связанного AdSpet

    val published: String? = null,
    val updated: String? = null,

    var isGeoOn: Boolean = false,
    var isFavourite: Boolean = false,
    var hash: String? = null
) {
    val images: Array<String>?
        get() {
            val result = imagesURL
            if (result != null) return imagesURL!!.split(" ").toTypedArray()
            else return null
        }
}

/*
// один к одному - связь черного списка с подборкой
data class SetAndBlackList(
    @Embedded val set: AdSet,
    @Relation(
        parentColumn = "id",
        entityColumn = "setId"
    )
    val blackList: BlackList
)

// один ко многим - черный список с объявлениями
data class BlackListWithAdverts(
    @Embedded val blackList: BlackList,
    @Relation(
        parentColumn = "id", // из таблицы AdSet
        entityColumn = "adSetId" // из таблицы Advert
    )
    val adverts: List<Advert>
)
*/