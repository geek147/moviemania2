package com.envious.moviemania.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.envious.data.BuildConfig.IMAGE_URL
import com.envious.domain.model.Movie
import com.envious.moviemania.R
import com.envious.moviemania.databinding.ListItemRowBinding
import com.envious.moviemania.ui.DetailActivity
import com.envious.moviemania.ui.fragment.DetailFragment
import com.envious.moviemania.ui.fragment.ListMovieFragment
import com.envious.moviemania.utils.BindingConverters

class MovieAdapter(private var fragment: Fragment) : RecyclerView.Adapter<MovieAdapter.MainViewHolder>() {
    private var listMovies: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: ListItemRowBinding = DataBindingUtil.inflate(LayoutInflater.from(fragment.context), R.layout.list_item_row, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (listMovies.isNullOrEmpty()) {
            0
        } else {
            listMovies.size
        }
    }

    override fun getItemId(position: Int): Long {
        val movie: Movie = listMovies[position]
        return movie.id.toLong()
    }

    fun addData(list: List<Movie>) {
        this.listMovies.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Movie>) {
        this.listMovies.clear()
        this.listMovies.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listMovies[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment)
        }
    }

    class MainViewHolder(private val binding: ListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Movie, fragment: Fragment) {
            BindingConverters.loadImage(binding.ivMoviePoster, IMAGE_URL + model.posterPath)

            val bundle = Bundle()
            bundle.putParcelable(DetailFragment.EXTRA_USER_DETAIL, model)
            binding.movie = model
            itemView.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_listMovieFragment_to_detailFragment2, bundle)
            }
        }
    }
}
