package ru.amalkoott.advtapp.domain

import android.media.Image

// класс объявления - хранит инфу по конкретному объявлению
// карточка объявления - будет отдельный класс с зависимостью от Advrt
class Advrt (
    val name: String,
    val ad_caption: String,
    val price: Float,
    val footage: Float, // метраж
    val room: Int,
    val floor: Int,
    val location: String,
    val home_caption: String,

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
{
    private val footage_price: Float = price / footage
    private val imgs = arrayOf(
        "https://desktopmania.ru/pics/00/05/13/DesktopMania.ru-5132-300x240.jpg",
        "https://c.wallhere.com/photos/10/26/1920x1200_px_animals_cats_Tanks-1914705.jpg!s",
        "https://images.chesscomfiles.com/uploads/v1/user/77559592.9cb711dc.160x160o.e195dd620cda.jpeg",
    )
    val images: Array<String>
        get() {
            return arrayOf(
                "https://desktopmania.ru/pics/00/05/13/DesktopMania.ru-5132-300x240.jpg",
                "https://c.wallhere.com/photos/10/26/1920x1200_px_animals_cats_Tanks-1914705.jpg!s",
                "https://images.chesscomfiles.com/uploads/v1/user/77559592.9cb711dc.160x160o.e195dd620cda.jpeg",
            )
        }
}