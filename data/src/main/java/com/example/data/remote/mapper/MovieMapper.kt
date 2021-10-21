package com.example.data.remote.mapper

import com.example.data.remote.model.MoviePayload
import com.example.data.remote.model.MoviesPayload
import com.example.domain.entities.Genres
import com.example.domain.entities.Movie

object MovieMapper {

    fun mapList(
        payload: MoviesPayload,
        listGenres: List<Genres>
    ) = payload.moviesPayload.map {
        map(
            payload = it,
            listGenres = listGenres
        )
    }

    fun map(
        payload: MoviePayload,
        listGenres: List<Genres>
    ) = Movie(
        id = payload.id,
        title = payload.title,
        original_title = payload.original_title,
        overview = payload.overview,
        popularity = payload.popularity,
        backdrop_path = payload.backdrop_path,
        poster_path = payload.poster_path,
        release_date = payload.release_date,
        vote_count = payload.vote_count,
        vote_average = payload.vote_average,
        genre_ids = payload.genre_ids,
        genre_names = listGenres.mapGenres(payload.genre_ids ?: listOf())
    )

    private fun List<Genres>.mapGenres(listIds: List<Int>): List<String> {
        return this.filter { genres ->
            listIds.contains(genres.id)
        }.map { it.name }
    }
}