package com.example.videogame_mvc.ui.viewmodel

import android.graphics.Color
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.toColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videogame_mvc.model.GameRepository
import com.example.videogame_mvc.model.Videogame
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class TaskViewModel: ViewModel() {

    private val repositorio = GameRepository()

    private val _games = MutableLiveData<List<Videogame>>()
    val games: LiveData<List<Videogame>> get() = _games

    private val _singleGame = MutableLiveData<Videogame>()
    val singleGame: LiveData<Videogame> get() = _singleGame

    private val _juegoEstaCargando = MutableLiveData<Boolean>()
    val juegoEstaCargando: LiveData<Boolean> get() = _juegoEstaCargando

    private val _juegosCargando = MutableLiveData<Boolean>()
    val juegosCargando: LiveData<Boolean> get() = _juegosCargando

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    //progressbar
    private val _progreso = MutableLiveData<Int>()
    val progreso: LiveData<Int> = _progreso

    private val _textColor = MutableLiveData<String>()

    val textColor: LiveData<String> = _textColor

    fun obtenerVideojuegos() {
        _games.value = emptyList<Videogame>()
    }

    fun obtenerJuegoAleatorio(){
        viewModelScope.launch {
            _juegoEstaCargando.value = true
            _mensajeEstado.value = "Conectando..."
            _progreso.value = 0

            // Corrutina que simula el progreso
            val jobProgreso = launch {
                var progresoActual = 0

                while (progresoActual < 100 && isActive) {
                    delay(50)
                    progresoActual++
                    _progreso.value = progresoActual
                }
            }

             //Corrutina que obtiene el juego
            val resultadoDeferred = async {
                repositorio.getRandomGame()
            }

            val resultado = resultadoDeferred.await()

            jobProgreso.cancel()

            _progreso.value = 100

            resultado.onSuccess { juego ->
                _mensajeEstado.value = "Juego obtenido correctamente"
                _singleGame.value = juego

            }.onFailure {
                _mensajeEstado.value = "Error: ${it.message}"
                _textColor.value = "#FF0000"
            }
            _juegoEstaCargando.value = false

        }
    }

    fun obtenerTresAleatorios(){
        viewModelScope.launch {
            _juegosCargando.value = true
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
                _textColor.value = "#FF0000"
                throw e
            } finally {
                _juegosCargando.value = false
            }


        }
    }

    fun borrarListaJuegos(){
        _games.value = emptyList<Videogame>()
    }
}




















