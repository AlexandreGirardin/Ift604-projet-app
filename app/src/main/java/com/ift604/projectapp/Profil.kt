package com.ift604.projectapp

import java.util.*
import kotlin.collections.ArrayList

class Profil(
    val id: UUID,
    val name: String,
    val age: Int,
    val distance: Int,
    val work: String,
    val pictures: ArrayList<String>)