package ru.amalkoott.advtapp.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

// класс объявления - хранит инфу по конкретному объявлению
// карточка объявления - будет отдельный класс с зависимостью от Advrt

@Entity
class Advert (
    @PrimaryKey(autoGenerate = true)
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

    val published: String? = null,
    val updated: String? = null,

    val additionalParam: String?,//AdditionalParameters? = null,
    var adSetId: Long?, // ID связанного AdSpet

    var isGeoOn: Boolean = false,
    var isFavourite: Boolean = false,
    var hash: String? = null

    //@TODO добавить номера телефна
)

{
    // массив ссылок
    val URLs: Array<String>?
        get() {
            if (url == null) {
                return null
            }else{
                return url.split(" ").toTypedArray()
            }
        }
    // массив картинок (URL)
    val images: Array<String>?
        get() {
            val result = imagesURL
            if (result != null) return imagesURL!!.split(" ").toTypedArray()
            else return null
        }

    fun saveImages() {
        CoroutineScope(Dispatchers.IO)
    }
}