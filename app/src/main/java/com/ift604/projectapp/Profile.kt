package com.ift604.projectapp

import java.util.*

// add bio
class Profile(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val distance: Int,
    val work: String,
    val pictures: List<String>,
    val photo: String,
    val bio: String,
    val age: Int)