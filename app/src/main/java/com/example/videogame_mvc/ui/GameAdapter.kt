package com.example.videogame_mvc.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videogame_mvc.databinding.ItemGameBinding
import com.example.videogame_mvc.model.Videogame

class GameAdapter(
    private var games: List<Videogame>
): RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(val binding: ItemGameBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GameViewHolder,
        position: Int
    ) {
        val game = games[position]
        holder.binding.tvTitle.text = game.titulo
        holder.binding.tvPlatform.text = game.plataforma
        holder.binding.tvDate.text = "Lanzamiento: ${game.lanzamiento}"
        holder.binding.ivIcon.setImageResource(game.imagen)
    }

    override fun getItemCount(): Int = games.size

    fun updateData(newGames: List<Videogame>) {
        this.games = newGames
        notifyDataSetChanged()
    }

}