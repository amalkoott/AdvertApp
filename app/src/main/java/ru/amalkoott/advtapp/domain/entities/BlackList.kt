package ru.amalkoott.advtapp.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

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