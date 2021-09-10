package com.example.moviestest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestest.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(val Movies:List<Movie>):RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MovieHolder(layoutInflater.inflate(R.layout.item_movie, parent,false))
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.render(Movies[position])
    }

    override fun getItemCount(): Int {
        return Movies.size
    }

    class MovieHolder(val view:View):RecyclerView.ViewHolder(view){
        fun render(movie: Movie){
            val binding = ItemMovieBinding.bind(view)
            binding.tvMovieName.text = movie.title
            binding.tvCount.text = movie.voteCount.toString()
            binding.tvAverage.text = movie.averageVote.toString()
            //binding.tvMovieName.text = movie.picture
            Picasso.get().load("https://www.themoviedb.org/t/p/w440_and_h660_face"+movie.picture).into(binding.ivImage)
            view.setOnClickListener { Toast.makeText(view.getContext(), movie.overview, Toast . LENGTH_LONG).show() }
        }
    }

}