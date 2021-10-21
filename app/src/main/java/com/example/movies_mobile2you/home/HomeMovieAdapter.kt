package com.example.movies_mobile2you.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Movie
import com.example.movies_mobile2you.R
import com.example.movies_mobile2you.databinding.AdapterMovieSimilarBinding
import com.example.movies_mobile2you.databinding.ItemLoadStateBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class HomeMovieAdapter @Inject constructor() :
    PagingDataAdapter<Movie, HomeMovieAdapter.ViewHolder>(REPO_COMPARATOR) {

    inner class ViewHolder(private val binding: AdapterMovieSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(itemView) {
            val urlBase = resources.getString(R.string.url_base_image_similar)

            Picasso.get()
                .load(urlBase.plus(movie.poster_path))
                .into(binding.imageMovieSimilar)
            binding.textTitleMovieSimilar.text = movie.title
            binding.textAgeMovie.text = movie.release_date?.split(DELIMITER_DATE)?.get(0) ?: ""
            binding.textGenres.text = movie.genre_names?.joinToString(SEPARATOR_GENRES)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        AdapterMovieSimilarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    private companion object {
        const val SEPARATOR_GENRES = ", "
        const val DELIMITER_DATE = "-"
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }
}

class ReposLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ReposLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: ReposLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ReposLoadStateViewHolder {
        return ReposLoadStateViewHolder.create(parent, retry)
    }
}

class ReposLoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.progress.isVisible = loadState is LoadState.Loading
        binding.retry.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): ReposLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state, parent, false)
            val binding = ItemLoadStateBinding.bind(view)
            return ReposLoadStateViewHolder(binding, retry)
        }
    }
}