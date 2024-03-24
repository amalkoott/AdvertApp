package ru.amalkoott.advtapp.domain

import androidx.room.PrimaryKey

// класс объявления - хранит инфу по конкретному объявлению
// карточка объявления - будет отдельный класс с зависимостью от Advrt
class Advert (
    val name: String? = "undefined_name",
    val ad_caption: String? = "empty_caption",
    val price: Float? = null,
    val location: String? = "indefined_location",
    val URL: String? = "undefined_URL",
    val additionalParam: AdditionalParameters? = null,
    val adSetId: Long // ID связанного AdSet

    //val footage: Float, // метраж
    //val room: Int,
    //val floor: Int,
    //val home_caption: String,

    /*

    Объявление
-!! цена метра
- метраж кухни
- характеристика
- информация из росреестра

Объявление
- количество этажей в доме
- о квартире
- условия аренды (только аренда)
- правила (только аренда)


    */
    //@TODO var pictures: Array<Image>
)
//text = "Продается уютная квартира в районе с развитой инфраструктурой. Евро-двушка.- 34,7 м.кв+лоджия. Просторная кухня 14 м.кв. Широкая застекленная лоджия.В квартире очень тепло. Прекрасны йремонт от застройщика. Светлые обои, качественный ламинат. В санузле кафельная плитка .Рядом с домом школа с бассейном, магазины и вся необходимая инфраструктура. Один собственник, обременений нет. Буду рада показать этот отличный вариант. Ключи у агента.",

{

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    var isFavourites: Boolean = false


    //constructor():this("undefined_name","non_caption",0f,0f,0,0,"undefined_location","undefined_home")
    //private val footage_price: Float = price / footage

    val images: Array<String>
        get() {
            return arrayOf(
                "https://desktopmania.ru/pics/00/05/13/DesktopMania.ru-5132-300x240.jpg",
                "https://c.wallhere.com/photos/10/26/1920x1200_px_animals_cats_Tanks-1914705.jpg!s",
                "https://images.chesscomfiles.com/uploads/v1/user/77559592.9cb711dc.160x160o.e195dd620cda.jpeg",
            )
        }
}