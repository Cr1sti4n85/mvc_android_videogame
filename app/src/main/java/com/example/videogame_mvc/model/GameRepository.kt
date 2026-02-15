package com.example.videogame_mvc.model

import com.example.videogame_mvc.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class GameRepository {

    private val listaJuegos = mutableListOf<Videogame>(
        Videogame("Zelda", "Wii", R.drawable.zelda, 2024),
        Videogame("Resident Evil 2", "PS5", R.drawable.re_2_remake, 2025),
        Videogame("Super Mario", "Wii",R.drawable.super_mario_wonder , 2023),
        Videogame("GTA V", "PS4", R.drawable.gta5, 2020),
        Videogame("Uncharted", "PS4", R.drawable.uncharted, 2019),
        );

    private val listaVista = mutableListOf<Videogame>()

    suspend fun getRandomGame(): Result<Videogame>{
        return withContext(Dispatchers.IO){
            val delayAleatorio = (2000..5000).random().toLong()
            delay(delayAleatorio)
            if (listaVista.size == listaJuegos.size) {
                listaVista.clear()
            }

            val errorAleatorio = (1..5).random()

            if (errorAleatorio == 1) {
                Result.failure(Exception("Error de red"))
            } else {
                val disponibles = listaJuegos.filter { it !in listaVista }
                val elementoAleatorio = disponibles.random()
                listaVista.add(elementoAleatorio)
                Result.success(elementoAleatorio)
            }
        }
    }

    open fun obtenerTodos(): List<Videogame> = listaJuegos.toList()
}