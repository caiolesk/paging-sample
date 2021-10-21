package com.example.data.remote.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.remote.api.MovieApi
import com.example.data.remote.mapper.GenresMapper
import com.example.data.remote.mapper.MovieMapper
import com.example.domain.entities.Movie
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieDataSourceImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieDataSource, PagingSource<Int, Movie>() {

    var id: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: FIRST_PAGE
        return try {
            delay(3000)
            withContext(IO) {
                val response = async {
                    movieApi.fetchMoviesSimilar(
                        movieId = id,
                        page = position
                    )
                }
                val genres = async {
                    movieApi.fetchGenresList().genresListPayload.map {
                        GenresMapper.map(it)
                    }
                }
                val list = MovieMapper.mapList(
                    payload = response.await(),
                    listGenres = genres.await()
                )

                val nextKey = if (list.isEmpty()) {
                    null
                } else {
                    position + 1
                }
                LoadResult.Page(
                    data = list,
                    prevKey = if (position == FIRST_PAGE) null else position - 1,
                    nextKey = nextKey
                )

            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun fetchMovieDetails(movieId: Int) = flow {
        emit(MovieMapper.map(movieApi.fetchMovieDetails(movieId), listOf()))
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}