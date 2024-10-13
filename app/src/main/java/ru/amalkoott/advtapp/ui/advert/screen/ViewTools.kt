package ru.amalkoott.advtapp.ui.advert.screen

fun String?.toValueFormattUpperCase():String{
    try {
        var str = this!!
        for (match in Regex("[А-Я]").findAll(str)) {
            str = str.replace(match.value," ${match.value}")
        }
        //val temp = str.replace("\"","")
        return str.replace("\"","").removeRange(0,1)//.drop(1)

    }catch (e:Exception){
        return "-"
    }
}
fun String?.toValue():String{
    if (this!! == "\"-\"") {
        return this.replace("\"","")
    }
    try {
        return this!!.replace("\"","")
    }catch (e:Exception){
        return ""
    }
}

fun String?.toValueFormatt():String{
    try {
        if (this!! == "\"-\"") return this.replace("\"","")
        var str = this!!
        for (match in Regex("[А-Я]").findAll(str)) {
            if(match.range.first != 1){
                str = str.replace(match.value," ${match.value.lowercase()}")
            }else{
                str = str.replace(match.value," ${match.value}")
            }
        }
        return str.replace("\"","").removeRange(0,1)//.drop(1)

    }catch (e:Exception){
        return "-"
    }
}

fun getTime(interval: Int?):String{
    return try {
        if (interval!!/60 < 1) "$interval мин." else "${interval/60} час."
    } catch (e: Exception){
        "???"
    }
}