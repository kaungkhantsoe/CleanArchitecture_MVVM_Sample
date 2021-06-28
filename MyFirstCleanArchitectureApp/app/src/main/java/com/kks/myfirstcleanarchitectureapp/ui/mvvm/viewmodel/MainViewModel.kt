package com.kks.myfirstcleanarchitectureapp.ui.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.myfirstcleanarchitectureapp.ui.common.DataState
import com.kks.myfirstcleanarchitectureapp.ui.common.ScreenState
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toPresentationModel
import com.kks.myfirstcleanarchitectureapp.ui.util.NetworkListener
import com.kks.myfirstcleanarchitectureapp.ui.util.NetworkUtil
import com.kks.usecases.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.kks.domain.Movie as DomainMovie

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
@HiltViewModel
class MainViewModel
@Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val networkUtil: NetworkListener
) : ViewModel() {

    private lateinit var _screenState: MutableLiveData<ScreenState<DataState>>
    private var _pageNumber: Int = 1
    private var _isRefreshed = false
    private var _loadedMovies = mutableListOf<Movie>()

    var pageNumber: Int = _pageNumber
        set(value) {
            _screenState.value = ScreenState.Loading

            field = value

            _isRefreshed = value == 1 && networkUtil.isNetworkAvailable()

            if (!_isRefreshed) queryMoviesFromDb(value)
            else loadMoviesFromRemote(value)
        }

    val screenState: LiveData<ScreenState<DataState>>
        get() {
            if (!::_screenState.isInitialized) {
                _screenState = MutableLiveData()

                viewModelScope.launch(Dispatchers.Main) {
                    _screenState.value = ScreenState.Loading
                }

                queryMoviesFromDb()
            }
//            viewModelScope.launch(Dispatchers.Main) {
//                _screenState.value = ScreenState.Render(DataState.Success(_loadedMovies))
//            }

            return _screenState
        }

    private fun queryMoviesFromDb(page: Int = 1) = viewModelScope.launch {
        if (page == 1) _loadedMovies.clear()

        val movies = movieUseCase.getMoviesFromLocal(page).map(DomainMovie::toPresentationModel)
        if (movies.isNullOrEmpty()) {
            if (networkUtil.isNetworkAvailable()) loadMoviesFromRemote(page)
            else _screenState.value = ScreenState.Render(DataState.EndReach)
        } else {
            _screenState.value = ScreenState.Render(DataState.Success(movies))
            _loadedMovies.addAll(movies)
        }
    }

    private fun loadMoviesFromRemote(page: Int = 1) = viewModelScope.launch {
        if (page == 1) _loadedMovies.clear()

        try {
            flow { emit(movieUseCase.getMoviesFromRemote(page)) }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    _screenState.value = ScreenState.Render(
                        DataState.Error(
                            throwable.localizedMessage ?: "Unknown error"
                        )
                    )
                }
                .collect {
                    when {
                        it.errors?.isNotEmpty() == true -> {
                            _screenState.value =
                                ScreenState.Render(
                                    DataState.Error(it.errors!![0])
                                )
                        }
                        it.success == false -> {
                            _screenState.value =
                                ScreenState.Render(
                                    DataState.Error(it.status_message ?: "")
                                )
                        }
                        else -> {
                            it.results?.let { results ->
                                if (results.isEmpty())
                                    _screenState.value = ScreenState.Render(DataState.EndReach)
                                else {
                                    movieUseCase.insertMovies(results)
                                    _loadedMovies.addAll(results.map { movie ->
                                        movie.toPresentationModel()
                                    })

                                    _screenState.value =
                                        ScreenState.Render(
                                            DataState.Success(
                                                results.map { movie ->
                                                    movie.toPresentationModel()
                                                }
                                            )
                                        )
                                }
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            _screenState.value =
                ScreenState.Render(DataState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

}