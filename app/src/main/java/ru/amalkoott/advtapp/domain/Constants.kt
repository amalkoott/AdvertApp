package ru.amalkoott.advtapp.domain

object Constants {
    const val WORK_TAG = "UpdateSet_"
    const val LOG_UPDATE_TAG = "UpdatingWork"
    const val PERIODIC_TAG = "PeriodicUpdate"
    const val ONE_TIME_TAG = "OneTimeUpdate"
    const val SET_TAG = "Set_"

    val SITES = mapOf<String,String>(
        "avito" to "Авито",
        "cian" to "ЦИАН",
        "domclick" to "Домклик")
}

fun Map<String,String>.getFromUrl(items: Array<String>): Map<String,String>{
    var result = mutableMapOf<String,String>()

    items.forEach {
        val site:String = Regex("(?<=\\.)(\\w+)(?=\\.)").find(it)!!.value
        result.put(Constants.SITES[site]!!,it)
    }
    return result
}