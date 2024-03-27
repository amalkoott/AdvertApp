package ru.amalkoott.advtapp.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

@Entity
class AdSet(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = "undefined_name",
    var adverts: List<Advert>? = null,
    var update_interval: Int? = null,
    var caption: String? = "empty_caption",
    var category: SetCategory? = null,
    var last_update: LocalDate? = null
) {
    /*
    constructor() : this(
        name = "name",
        adverts = null,
        update_interval = null,
        caption = "empty",
        category = null,
        last_update = null) {

    }*/
    /*
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

     */
}

data class AdSetWithAdverts(
    @Embedded val adSet: AdSet,
    @Relation(
        parentColumn = "id", // из таблицы AdSet
        entityColumn = "adSetId" // из таблицы Advert
    )
    val adverts: List<Advert>
)