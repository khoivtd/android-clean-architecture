package com.aliasadi.clean.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aliasadi.clean.base.BaseViewModel
import com.aliasadi.clean.util.SingleLiveEvent
import com.aliasadi.data.util.DispatchersProvider
import com.aliasadi.domain.entities.Movie
import com.aliasadi.domain.usecase.GetMovies
import com.aliasadi.domain.util.onError
import com.aliasadi.domain.util.onSuccess

/**
 * Created by Ali Asadi on 13/05/2020
 */
class FeedViewModel internal constructor(
    private val getMovies: GetMovies,
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    private val movies: MutableLiveData<List<Movie>> = MutableLiveData()
    private val showLoading: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val hideLoading: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val showError: SingleLiveEvent<String> = SingleLiveEvent()
    private val navigateToMovieDetails: SingleLiveEvent<Movie> = SingleLiveEvent()

    fun onInitialState() = launchOnMainImmediate {
        loadMovies()
    }

    fun onMovieClicked(movie: Movie) = launchOnMainImmediate {
        navigateToMovieDetails.value = movie
    }

    private suspend fun loadMovies() {
        showLoading.value = Unit

        getMovies.execute()
            .onSuccess {
                hideLoading.value = Unit
                movies.value = it
            }.onError {
                hideLoading.value = Unit
                showError.value = it.message
            }
    }


    fun getMoviesLiveData(): LiveData<List<Movie>> = movies
    fun getShowLoadingLiveData(): LiveData<Unit> = showLoading
    fun getHideLoadingLiveData(): LiveData<Unit> = hideLoading
    fun getShowErrorLiveData(): LiveData<String> = showError
    fun getNavigateToMovieDetails(): LiveData<Movie> = navigateToMovieDetails

    class Factory(
        private val getMovies: GetMovies,
        private val dispatchers: DispatchersProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeedViewModel(getMovies, dispatchers) as T
        }
    }
}