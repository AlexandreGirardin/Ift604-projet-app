package com.ift604.projectapp

import org.json.JSONObject
import kotlin.collections.ArrayList

class Profile {
    var id: Int? = null
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var distance: Int = 0
    var position: ArrayList<Double> = arrayListOf(0.0, 0.0)
    var photo: String = ""
    var bio: String = ""
    var age: Int = 18

    constructor() {
    }

    constructor(
        pId: Int?,
        pName: String,
        pEmail: String,
        pPassword: String,
        pDistance: Int,
        pPosition: ArrayList<Double>,
        pPhoto: String,
        pBio: String,
        pAge: Int)
    {
        id = pId
        name = pName
        email = pEmail
        password = pPassword
        distance = pDistance
        position = pPosition
        photo = pPhoto
        bio = pBio
        age = pAge
    }

    constructor(obj: JSONObject) {
        // TODO: Ajouter la longitude et latitude???
        id = obj.getInt("id")
        name = obj.getString("name")
        photo = obj.getString("photo")
        bio = obj.getString("bio")
        age = obj.getInt("age")
    }
}
