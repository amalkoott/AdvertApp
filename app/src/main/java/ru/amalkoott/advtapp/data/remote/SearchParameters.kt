package ru.amalkoott.advtapp.data.remote

import androidx.compose.runtime.MutableState

data class SearchParameters (
    var city:String? = null,
    var category: String? = null,
    var dealType: String? = null,
    var livingType: String? = null,
    var rentType: String? = null,
    var priceType: String? = null,
    var minPrice: Float? = null,
    var maxPrice:Float? = null,

    var minArea:Int? = null,
    var maxArea:Int? = null,
    var minLArea:Int? = null,
    var maxLArea:Int? = null,
    var minKArea:Int? = null,
    var maxKArea:Int? = null,
    var minFloor:Int?= null, // этаж
    var maxFloor:Int?= null,
    var minFloors:Int?= null, // этажность
    var maxFloors:Int?= null,
    var floorType: String? = null,
    var repair: String? = null,
    var finish: String? = null,
    var travelTime: Byte? = null,
    var travelType: String? = null,
    var cell: String? = null,
    var apart: Boolean? = null,
    var roomType:Boolean? = null,
    var room:UByte? = null,
    var toiletType:Boolean? = null,
    var wallMaterial:String? = null,
    var balconyType:Boolean? = null,
    var parking:String? = null,
    var liftType:Boolean? = null,
    var amenities:String? = null,
    var view:String? = null,
    var communication:String? = null,
    var include:String? = null,
    var exclude:String? = null,
    var rentFeature:String? = null
){

}
/*

data class GitHubIssue(
    val number: Long?,
    val title: String,
    val body: String,
    val state: String?
) {
    constructor(number: Long?,
                title: String,
                body: String):
            this(number,title,body,null)
}

 */