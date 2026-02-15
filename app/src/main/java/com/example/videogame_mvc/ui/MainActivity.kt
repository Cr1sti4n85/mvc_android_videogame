package com.example.videogame_mvc

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.videogame_mvc.databinding.ActivityMainBinding
import com.example.videogame_mvc.model.GameRepository
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    private val repo = GameRepository();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {
        binding.btnJuegoSiguiente.setOnClickListener {
           obtenerConManejoDeErrores()
        }

    }


    private fun obtenerConManejoDeErrores(){
        lifecycleScope.launch {
            limpiarPantalla()
            binding.circularProgressIndicator.visibility = View.VISIBLE
            binding.btnJuegoSiguiente.isEnabled = false
            binding.tvEstado.text = "Conectando..."

            val resultado = repo.getRandomGame()
            binding.circularProgressIndicator.visibility = View.GONE
            binding.btnJuegoSiguiente.isEnabled = true

            resultado
                .onSuccess { videojuego ->
                    binding.tvEstado.text = "Juego obtenido correctamente"
                    binding.tvTituloJuego.text = videojuego.titulo
                    binding.tvPlatforma.text = videojuego.plataforma
                    binding.tvLanzamiento.text = "Lanzamiento: ${videojuego.lanzamiento}"
                    binding.ivImagenJuego.setImageResource(videojuego.imagen)
                }
                .onFailure { error ->
                    binding.tvEstado.text = "Error: ${error.message}"
                }

        }
    }

    private fun obtenerTresJuegos(){
        lifecycleScope.launch {
            try {
                binding.circularProgressIndicator.visibility = View.VISIBLE

                withTimeout(5000) {
                    val deferred1 = async { repo.getRandomGame() }
                    val deferred2 = async { repo.getRandomGame() }
                    val deferred3 = async { repo.getRandomGame() }

                    val resultados = awaitAll(deferred1, deferred2, deferred3)
                    }
                }
            catch (e: TimeoutCancellationException){
                binding.tvEstado.text = "El servidor es muy lento..."
                throw e
            } finally {
                binding.circularProgressIndicator.visibility = View.VISIBLE
            }
        }
    }

    private fun limpiarPantalla(){
        binding.tvEstado.text = ""
        binding.tvTituloJuego.text = ""
        binding.tvPlatforma.text = ""
        binding.tvLanzamiento.text = ""
        binding.ivImagenJuego.setImageDrawable(null)

    }

}