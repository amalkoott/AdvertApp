package ru.amalkoott.advtapp.ui.advert.compose.screenCompose

import androidx.compose.runtime.Composable
import ru.amalkoott.advtapp.ui.advert.compose.RangeSliderFilter
import ru.amalkoott.advtapp.ui.advert.compose.TextFilter

@Composable
fun GeneralSetFilters(functions: Map<String,(String)->Unit>) {
    TextFilter("Санкт-Петербург", "Город", "Название города",functions["city"]!!)
    RangeSliderFilter("Цена",0f,100f,functions["minPrice"]!!,functions["maxPrice"]!!)
}