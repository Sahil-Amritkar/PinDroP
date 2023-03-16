package com.example.pindrop.classes

class Group(
    var id: String? = null,
    var name:String? = null,
    var password:String? = null,
    var members: MutableList<String?>? = null,
    var trips:MutableList<Trip>? = null):java.io.Serializable {
}