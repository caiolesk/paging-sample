package com.example.movies_mobile2you.home

import androidx.lifecycle.viewModelScope
import com.example.data.remote.MovieRepository
import com.example.movies_mobile2you.extension.onCollect
import com.example.movies_mobile2you.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel<HomeIntent, HomeState>() {

    override fun handle(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.StartData -> fetchMovieDetails()
        }
    }

    private fun fetchMovieDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchMovieDetails(movieId).onCollect(
                onLoading = {
                    _state.value = HomeState.Loading(it)
                },
                onSuccess = {
                    _state.value = HomeState.SuccessDetail(it)
                    fetchMoviesSimilar()
                },
                onError = {
                    _state.value = HomeState.Error
                }
            )
        }
    }

    private fun fetchMoviesSimilar() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchMoviesSimilar(movieId).onCollect(
                onLoading = {
                    _state.value = HomeState.Loading(it)
                },
                onSuccess = {
                    _state.value = HomeState.SuccessSimilarMovies(it)
                },
                onError = {
                    _state.value = HomeState.Error
                }
            )
        }

    }

    companion object {
        private const val movieId = 497582
    }
}