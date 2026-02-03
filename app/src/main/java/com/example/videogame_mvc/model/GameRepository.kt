package com.example.videogame_mvc.model

import com.example.videogame_mvc.R

class GameRepository {

    private val listaJuegos = listOf<Videogame>(
        Videogame("Zelda", "Wii", R.drawable.zelda, 2024),
        Videogame("Resident Evil 2", "PS5", R.drawable.re_2_remake, 2025),
        Videogame("Super Mario", "Wii",R.drawable.super_mario_wonder , 2023),
        Videogame("GTA V", "PS4", R.drawable.gta5, 2020),
        Videogame("Uncharted", "PS4", R.drawable.uncharted, 2019),
        );

    fun getRandomGame(): Videogame{
        val elementoAleatorio = listaJuegos.random()
        return elementoAleatorio
    }
}