package ru.amalkoott.advtapp.ui.advert.compose.screenCompose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.amalkoott.advtapp.ui.advert.compose.RangeFilter
import ru.amalkoott.advtapp.ui.advert.compose.RangeSliderFilter
import ru.amalkoott.advtapp.ui.advert.compose.TextFilter

@Composable
fun GeneralSetFilters(functions: Map<String,(String)->Unit>) {
    TextFilter("Санкт-Петербург", "Город", "Название города",functions["city"]!!)

    RangeFilter(name = "Цена", setMinValue = functions["minPrice"]!!, setMaxValue = functions["maxPrice"]!!, Modifier.padding(top = 16.dp))
}