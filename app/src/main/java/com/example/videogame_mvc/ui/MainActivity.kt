package com.example.videogame_mvc

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videogame_mvc.databinding.ActivityMainBinding
import com.example.videogame_mvc.model.GameRepository
import com.example.videogame_mvc.model.Videogame
import com.example.videogame_mvc.ui.GameAdapter
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    private lateinit var gameAdapter: GameAdapter
    private val repo = GameRepository();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()
        setupRecyclerView()
    }

    private fun setupListener() {
        binding.btnJuegoSiguiente.setOnClickListener {
           obtenerConManejoDeErrores()
        }

        binding.btnTresJuegos.setOnClickListener {
            obtenerTresJuegos()
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

                val resultados: List<Result<Videogame>> = withTimeout(5000) {

                    val deferred1 = async { repo.getRandomGame() }
                    val deferred2 = async { repo.getRandomGame() }
                    val deferred3 = async { repo.getRandomGame() }

                    awaitAll(deferred1, deferred2, deferred3)
                }

                // 🔥 convertir List<Result<Videogame>> a List<Videogame>
                val juegos = resultados.mapNotNull { it.getOrNull() }

                // actualizar recycler
                gameAdapter.updateData(juegos)

                }
            catch (e: TimeoutCancellationException){
                binding.tvEstado.text = "El servidor es muy lento..."
                throw e
            } finally {
                binding.circularProgressIndicator.visibility = View.GONE
            }
        }
    }

    //recycler view
    private fun setupRecyclerView(){
        gameAdapter = GameAdapter(
            games = emptyList()
        )

        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
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