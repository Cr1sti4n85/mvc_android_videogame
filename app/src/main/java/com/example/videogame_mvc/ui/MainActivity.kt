package com.example.videogame_mvc

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.videogame_mvc.databinding.ActivityMainBinding
import com.example.videogame_mvc.model.GameRepository

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
            val videoJuego = repo.getRandomGame()
            binding.tvTituloJuego.text = videoJuego.titulo
            binding.tvPlatforma.text = videoJuego.plataforma
            binding.tvLanzamiento.text = "Lanzamiento: ${videoJuego.lanzamiento}"
            binding.ivImagenJuego.setImageResource(videoJuego.imagen)

        }


    }

}