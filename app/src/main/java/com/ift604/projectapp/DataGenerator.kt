package com.ift604.projectapp

class DataGenerator {
    val names: List<String> = listOf(
        "Alex", "Max", "Emilio", "Etienne", "Natasha", "Sabrina", "Jessie", "Kristina", "Nicolas", "Gabriel",
        "Anna", "Jessica"
    )

    val bios: List<String> = listOf(
        "Two truths & a lie… I was a Gerber baby. I once won a regional hot dog eating competition. Chrissy Teigen used my banana bread recipe on her secret food blog.",
        "2 truths and a lie… I’m double jointed. My cat is in an extremely popular meme. I was bitten by a dolphin in Maui.",
        "In the event of a zombie apocalypse…\n" +
                "I’d immediately steal the Egyptian presidential yacht. Load it up with tater tots and Missy Elliot records. Take it out to the middle of the Pacific and get my tan on. Life’s short anyway, I want to go out in style.",
        "I’ve always known the secret to happiness is gratitude. I’m still always surprised and humbled by how much I have in my life to be grateful for!",
        "Dogs are overrated.\n" +
                "The empire did nothing wrong.\n" +
                "Cards Against Humanity is boring and lazy.\n" +
                "Burger King fries > McDonalds fries.",
        "Would you rather be able to eat anything you want and not get fat or be well rested on one hour of sleep?",
        "Extremely accurate snowball thrower. My cord management is both compulsive and flawless. I can touch my nose with my tongue."
    )

    companion object {
        val instance = DataGenerator()
    }

    fun generateName(): String {
        return names.random()
    }

    fun generateBio(): String {
        return bios.random()
    }
}