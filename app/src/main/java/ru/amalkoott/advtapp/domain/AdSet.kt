package ru.amalkoott.advtapp.domain

import androidx.compose.runtime.MutableState
import java.time.LocalDate

class AdSet(
    var name: String,
    var adverts: List<Advrt>,

    var update_interval: Int,
    var last_update: LocalDate?
) {

}