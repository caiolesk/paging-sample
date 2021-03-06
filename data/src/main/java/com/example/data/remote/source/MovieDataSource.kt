package com.example.data.remote.source

import com.example.domain.entities.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {

    fun fetchMovieDetails(movieId: Int): Flow<Movie>
}