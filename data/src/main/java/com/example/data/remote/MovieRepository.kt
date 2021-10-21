package com.example.data.remote

import androidx.paging.PagingData
import com.example.domain.entities.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun fetchMovieDetails(movieId: Int): Flow<Movie>

    fun fetchMoviesSimilar(
        movieId: Int
    ): Flow<PagingData<Movie>>
}