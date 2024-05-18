package ru.amalkoott.advtapp.domain

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.App
import java.io.FileOutputStream
import java.net.URL

// класс объявления - хранит инфу по конкретному объявлению
// карточка объявления - будет отдельный класс с зависимостью от Advrt

@Entity
class Advert (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val name: String? = "undefined_name",
    val description: String? = "empty_description",
    val price: String? = null,
    val location: String? = "indefined_location",
    val address: String? = "undefined_address",
    val url: String? = "undefined_URL",
    val imagesURL: String? = "empty_imageURL",
    val additionalParam: String?,//AdditionalParameters? = null,
    var adSetId: Long?, // ID связанного AdSpet

    var isFavourite: Boolean = false,
    var hash: String? = null
    //val footage: Float, // метраж
    //val room: Int,
    //val floor: Int,
    //val home_caption: String,

    //@TODO добавить номера телефна
)
//text = "Продается уютная квартира в районе с развитой инфраструктурой. Евро-двушка.- 34,7 м.кв+лоджия. Просторная кухня 14 м.кв. Широкая застекленная лоджия.В квартире очень тепло. Прекрасны йремонт от застройщика. Светлые обои, качественный ламинат. В санузле кафельная плитка .Рядом с домом школа с бассейном, магазины и вся необходимая инфраструктура. Один собственник, обременений нет. Буду рада показать этот отличный вариант. Ключи у агента.",

{
/*
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
*/
    // var isFavourites: Boolean = false
    // массив ссылок
    val URLs: Array<String>
        get() {
            return url!!.split(" ").toTypedArray()
        }
    //constructor():this("undefined_name","non_caption",0f,0f,0,0,"undefined_location","undefined_home")
    //private val footage_price: Float = price / footage


    // массив картинок (URL)
    val images: Array<String>
        get() {
            return imagesURL!!.split(" ").toTypedArray()
            /*
            return arrayOf(
                "https://4-img.onrealt.ru/c326x235q80/files/12-2023/11/6e/79/astrakhan-snyat-kvartiru-na-dlitelnyj-srok-ulitca-treneva-23-191292784.jpg",
                "https://desktopmania.ru/pics/00/05/13/DesktopMania.ru-5132-300x240.jpg",
                "https://c.wallhere.com/photos/10/26/1920x1200_px_animals_cats_Tanks-1914705.jpg!s",
                "https://images.chesscomfiles.com/uploads/v1/user/77559592.9cb711dc.160x160o.e195dd620cda.jpeg",
            )
             */
        }

    suspend fun saveImages() {
        //val context = App.instance.applicationContext

        val scope = CoroutineScope(Dispatchers.IO)
        /*
        scope.launch {
            val url = URL(images[0])
            val imageData = url.readBytes()

            val file = FileOutputStream("filename")
        }
        */
/*
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // твой код
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {}

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        // Launch a new coroutine in the scope
        images.forEach {
                scope.launch {
                    Log.d("URLForImage",it)
                    val filename: String = "test_img"
                    val url = URL(it)
                    val imageData = url.readBytes()

                    val file = FileOutputStream(filename)
                    file.write(imageData)
                    file.close()
                }
        }
        */
    }
}