package com.example.paging_sample.home

import android.content.Context
import android.content.Intent
import com.example.domain.entities.Movie
import com.example.paging_sample.details.createDetailActivityIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeRouter @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    fun routeToDetail(movie: Movie) {
        context.startActivity(
            context.createDetailActivityIntent(movie).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}