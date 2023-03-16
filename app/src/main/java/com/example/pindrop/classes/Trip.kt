package com.example.pindrop.classes

private const val TAG = "TripClass"
class Trip(val name:String? = null,
           val description:String? = null,
           val status:String? = null,
           var headMarker: Marker? = null,
           var markerHue:Float?=null,
           var markers: List<Marker>? = null):java.io.Serializable {
//    var markerHue=0f
//    init {
//        Log.i(TAG,(status=="Completed").toString())
//        if(status=="Completed") {
//            markerHue = rand(60, 360)
//        } else{
//            markerHue= 0f
//        }
////         markerHue = rand(0, 360)
//
//    }
//    private fun rand(start: Int, end: Int): Float {
//        require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
//        return (Random(System.nanoTime()).nextInt(end - start + 1) + start).toFloat()
//    }
}