package com.example.movies_mobile2you.home

import androidx.paging.PagingData
import com.example.domain.entities.Movie

sealed class HomeState {
    data class Loading(val loading: Boolean) : HomeState()
    data class SuccessDetail(val movie: Movie) : HomeState()
    data class SuccessSimilarMovies(
        val similarMovies: PagingData<Movie>
    ) : HomeState()
    object Error : HomeState()
}