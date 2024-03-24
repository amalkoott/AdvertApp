package ru.amalkoott.advtapp.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity
class AdSet(
    var name: String? = "undefined_name",
    var adverts: List<Advert>? = null,
    var update_interval: Int? = null,
    var caption: String? = "empty_caption",
    var category: SetCategory? = null,
    var last_update: LocalDate? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}

data class AdSetWithAdverts(
    @Embedded val adSet: AdSet,
    @Relation(
        parentColumn = "id", // из таблицы AdSet
        entityColumn = "adSetId" // из таблицы Advert
    )
    val adverts: List<Advert>
)