package com.example.videogame_mvc.ui.viewmodel

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videogame_mvc.model.GameRepository
import com.example.videogame_mvc.model.Videogame
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class TaskViewModel: ViewModel() {

    private val repositorio = GameRepository()

    private val _games = MutableLiveData<List<Videogame>>()
    val games: LiveData<List<Videogame>> get() = _games

    private val _singleGame = MutableLiveData<Videogame>()
    val singleGame: LiveData<Videogame> get() = _singleGame

    private val _estaCargando = MutableLiveData<Boolean>()
    val estaCargando: LiveData<Boolean> get() = _estaCargando

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    fun obtenerVideojuegos() {
        _games.value = emptyList<Videogame>()
    }

    fun obtenerJuegoAleatorio(){
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeEstado.value = "Conectando..."

            val resultado = repositorio.getRandomGame()

            resultado.onSuccess { juego ->
                _mensajeEstado.value = "Juego obtenido correctamente"
                _singleGame.value = juego

            }.onFailure {
                _mensajeEstado.value = "Error: ${it.message}"
            }
            _estaCargando.value = false

        }
    }

    fun obtenerTresAleatorios(){
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeEstado.value = "Obteniendo tres juegos"

            try {
                val resultados: List<Result<Videogame>> = withTimeout(4000) {

                    val deferred1 = async { repositorio.getRandomGame() }
                    val deferred2 = async { repositorio.getRandomGame() }
                    val deferred3 = async { repositorio.getRandomGame() }

                    awaitAll(deferred1, deferred2, deferred3)
            }
                _games.value = resultados.mapNotNull { it.getOrNull() }
                _mensajeEstado.value = "Carga finalizada"

            } catch (e: TimeoutCancellationException) {

                _mensajeEstado.value = "Servidor muy lento. Inténtalo nuevamente"
                throw e
            } finally {
                _estaCargando.value = false
            }


        }
    }
}




















