package com.example.videogame_mvc

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videogame_mvc.databinding.ActivityMainBinding
import com.example.videogame_mvc.model.GameRepository
import com.example.videogame_mvc.model.Videogame
import com.example.videogame_mvc.ui.GameAdapter
import com.example.videogame_mvc.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    private lateinit var gameAdapter: GameAdapter
    // private val repo = GameRepository();

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setupListener()
        setupRecyclerView()
        setupObservers()
        setupListeners()

//        viewModel.obtenerVideojuegos()
    }

    /*private fun setupListener() {
        binding.btnJuegoSiguiente.setOnClickListener {
           obtenerConManejoDeErrores()
        }

        binding.btnTresJuegos.setOnClickListener {
            obtenerTresJuegos()
        }

    }*/


    /*private fun obtenerConManejoDeErrores(){
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
                    binding.ivImagenJuego.visibility = View.VISIBLE
                }
                .onFailure { error ->
                    binding.tvEstado.text = "${error.message}"
                    binding.tvEstado.setTextColor(Color.RED)
                    Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT ).show()
                }

        }
    }*/

    /*private fun obtenerTresJuegos(){
        lifecycleScope.launch {
            try {
                limpiarPantalla()
                binding.circularProgressIndicator.visibility = View.VISIBLE

                val resultados: List<Result<Videogame>> = withTimeout(4000) {

                    val deferred1 = async { repo.getRandomGame() }
                    val deferred2 = async { repo.getRandomGame() }
                    val deferred3 = async { repo.getRandomGame() }

                    awaitAll(deferred1, deferred2, deferred3)
                }

                // convertir List<Result<Videogame>> a List<Videogame>
                val juegos = resultados.mapNotNull { it.getOrNull() }

                // actualizar recycler
                gameAdapter.updateData(juegos)

                }
            catch (e: TimeoutCancellationException){
                binding.tvEstado.text = "Servidor muy lento. Inténtalo nuevamente"
                throw e
            } finally {
                binding.circularProgressIndicator.visibility = View.GONE
            }
        }
    }*/

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

    private fun setupObservers(){
        viewModel.games.observe(this) {
            lista -> gameAdapter.updateData(lista)
        }

        viewModel.singleGame.observe(this) { videojuego ->
            binding.tvTituloJuego.text = videojuego.titulo
            binding.tvPlatforma.text = videojuego.plataforma
            binding.tvLanzamiento.text = "Lanzamiento: ${videojuego.lanzamiento}"
            binding.ivImagenJuego.visibility = View.VISIBLE
            binding.ivImagenJuego.setImageResource(videojuego.imagen)
        }

        viewModel.juegoEstaCargando.observe(this) {cargando ->
            binding.progressBarHorizontal.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.btnJuegoSiguiente.isEnabled = !cargando
        }

        viewModel.juegosCargando.observe(this) { cargando ->
            binding.circularProgress.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.btnTresJuegos.isEnabled = !cargando
        }

        viewModel.mensajeEstado.observe(this){mensaje ->
            binding.tvEstado.text = mensaje
        }

        viewModel.progreso.observe(this) { progreso ->
            binding.progressBarHorizontal.progress = progreso
        }

    }

    private fun setupListeners(){
        binding.btnJuegoSiguiente.setOnClickListener {
            limpiarPantalla()
            viewModel.obtenerJuegoAleatorio()
        }

        binding.btnTresJuegos.setOnClickListener {
            limpiarPantalla()
            viewModel.obtenerTresAleatorios()
        }
    }

    private fun limpiarPantalla(){
        binding.tvEstado.text = ""
        binding.tvEstado.setTextColor(Color.BLACK)
        binding.tvTituloJuego.text = ""
        binding.tvPlatforma.text = ""
        binding.tvLanzamiento.text = ""
        binding.ivImagenJuego.setImageDrawable(null)
        binding.ivImagenJuego.visibility = View.GONE
        viewModel.borrarListaJuegos()
    }

}