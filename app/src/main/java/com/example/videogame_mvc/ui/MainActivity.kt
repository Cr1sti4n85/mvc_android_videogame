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
import kotlinx.coroutines.launch

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
            binding.tvEstado.text = "Obteniendo información"

            val resultado = repo.getRandomGame()
            binding.circularProgressIndicator.visibility = View.GONE

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

    private fun limpiarPantalla(){
        binding.tvEstado.text = ""
        binding.tvTituloJuego.text = ""
        binding.tvPlatforma.text = ""
        binding.tvLanzamiento.text = ""
        binding.ivImagenJuego.setImageDrawable(null)

    }

}