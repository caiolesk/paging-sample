package com.example.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.remote.MovieRepository
import com.example.data.remote.api.MovieApi
import com.example.data.remote.source.MovieDataSource
import com.example.data.remote.source.MovieDataSourceImpl
import com.example.domain.entities.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieDataSource,
    private val movieApi: MovieApi
) : MovieRepository {

    override fun fetchMovieDetails(movieId: Int) =
        remoteDataSource.fetchMovieDetails(movieId)

    override fun fetchMoviesSimilar(
        movieId: Int
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MovieDataSourceImpl(
                    movieApi = movieApi
                ).apply {
                    id = movieId
                }
            }
        ).flow
    }
}