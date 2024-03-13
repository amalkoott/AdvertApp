package ru.amalkoott.advtapp.domain

// класс объявления - хранит инфу по конкретному объявлению
// карточка объявления - будет отдельный класс с зависимостью от Advrt
class Advrt (
    var name: String,
    var caption: String,
    var price: Float
    //@TODO var pictures: Array<Image>
)
{
}