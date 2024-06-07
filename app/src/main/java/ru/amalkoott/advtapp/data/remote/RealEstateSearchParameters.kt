package ru.amalkoott.advtapp.data.remote

data class RealEstateSearchParameters (
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
    var room:String? = null, // todo поменять на string
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
    fun getJson(){

    }
    fun setRoomValue(value: String){
        room = if(value == "null" || value == "-Неважно") null
        else room.setValue(value)
    }
    fun setFinishValue(value: String){
        finish = if (value == "Нет") value else finish!!.replace("Нет","").setValue(value)
    }
    fun setRepairValue(value: String){
    repair = repair.setValue(value)
    }
    fun setWallValue(value: String){
       wallMaterial = wallMaterial.setValue(value)
        //if(value[0] == '-') wallMaterial = wallMaterial?.replace(value.drop(1),"") else wallMaterial += "$value "
    }
    fun setParkingValue(value: String){
    parking = parking.setValue(value)
    }
    fun setViewValue(value: String){
    view = view.setValue(value)
    }
    fun setAmenitiesValue(value: String){
    amenities = amenities.setValue(value)
    }
    fun setCommunicationValue(value: String){
    communication = communication.setValue(value)
    }
    fun setRentFeatureValue(value: String){
        rentFeature = rentFeature.setValue(value)
    }
    fun setFloorTypeValue(value: String){
        if (value == "Только последний") floorType = value
        else floorType = floorType.setValue(value)
    }
    private fun String?.setValue(word: String): String? {
        return if (word[0] == '-')
        {
            val res = this!!.replace(word.drop(1), "")
            if (res.replace(" ","") == "") "null" else res
        }
         else
            if(this.toString().contains(Regex("null"))) "${this.toString().replace("null","")}$word "
            else  "$this$word "


    }

fun isNotEmpty():Boolean{
    if ((city == null)||(category == null)||(dealType == null)||(livingType == null) ||(rentType == null)||
        (priceType == null)||(minPrice == null)||(maxPrice == null)||
        (minArea == null)||(maxArea == null)||
        (minLArea == null)||(maxLArea== null)||
        (minKArea == null)||(maxKArea == null)||
        (minFloor == null)||(maxFloor== null)||(minFloors == null)||(maxFloors == null)|| (floorType == null)||
        (repair == null)||(finish == null)||
        (travelType == null)||(travelTime == null)||
        (cell == null)||(apart == null)||(roomType == null)||(room == null)||
        (toiletType == null)||(wallMaterial == null) ||(balconyType == null)||
        (parking == null)||(liftType == null)||(amenities == null) ||(view == null)||
        (communication == null)||(include == null)||(exclude == null)||(rentFeature == null)) return false
    return true
}
    fun cancelRent(){
        // обнуление всех rent-параметров
         rentType= null
         amenities = null
         rentFeature = null

    }
    fun cancelSale(){
        // обнуление всех sale-параметро
        priceType = null
    }
    fun cancelLayout(){
        minFloor = null // этаж
         maxFloor= null
         minFloors= null // этажность
         maxFloors= null
         floorType = null

         finish = null
         travelTime = null
         travelType = null
         apart = null

    }
    fun cancelCountry(){
         communication = null
         toiletType = null

         travelTime = null
         travelType = null
         repair = null

    }
    fun cancelFlat(){
         floorType= null

         repair = null

         parking = null
         toiletType= null
         apart = null

         travelTime = null
         travelType = null


         minFloor= null // этаж
         maxFloor= null

    }
}