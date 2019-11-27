package com.ift604.projectapp

import java.util.*

// add bio
class Profile(
    val id: UUID,
    val name: String,
    val age: Int,
    val distance: Int,
    val work: String,
    val pictures: List<String>)