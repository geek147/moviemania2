package com.envious.moviemania.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.envious.domain.model.Genre
import com.envious.domain.model.Movie
import com.envious.moviemania.R
import com.envious.moviemania.databinding.ListItemGenreRowBinding
import com.envious.moviemania.ui.DetailActivity
import com.envious.moviemania.ui.fragment.ListMovieFragment

class GenreAdapter(private var fragment: Fragment) : RecyclerView.Adapter<GenreAdapter.MainViewHolder>() {
    private var listGenre: MutableList<Genre> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: ListItemGenreRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(fragment.context),
            R.layout.list_item_genre_row, parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (listGenre.isNullOrEmpty()) {
            0
        } else {
            listGenre.size
        }
    }

    override fun getItemId(position: Int): Long {
        val genre: Genre = listGenre[position]
        return genre.id.toLong()
    }

    fun addData(list: List<Genre>) {
        this.listGenre.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Genre>) {
        this.listGenre.clear()
        this.listGenre.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listGenre[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment)
        }
    }

    class MainViewHolder(private val binding: ListItemGenreRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Genre, fragment: Fragment) {
            val bundle = Bundle()
            bundle.putInt(ListMovieFragment.EXTRA_USER_LIST_MOVIE, model.id)
            binding.genre = model

            itemView.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_homeFragment_to_listMovieFragment, bundle)
            }
        }
    }
}