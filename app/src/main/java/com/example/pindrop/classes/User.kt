package com.example.pindrop.classes

class User(var id:String? = null,
           var name:String? = null,
           var email:String? = null,
           var groups:MutableList<String>? = null):java.io.Serializable {
}