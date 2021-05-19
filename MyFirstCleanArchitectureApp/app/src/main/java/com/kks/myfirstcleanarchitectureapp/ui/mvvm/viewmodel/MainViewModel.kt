package com.kks.myfirstcleanarchitectureapp.ui.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.myfirstcleanarchitectureapp.ui.common.ScreenState
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toPresentationModel
import com.kks.myfirstcleanarchitectureapp.ui.common.DataState
import com.kks.usecases.GetMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
@HiltViewModel
class MainViewModel
@Inject constructor(
    private val getMovies: GetMovies
) : ViewModel() {

    private lateinit var _dataState: MutableLiveData<ScreenState<DataState>>

    val dataState: LiveData<ScreenState<DataState>>
        get() {
            if (!::_dataState.isInitialized) {
                _dataState = MutableLiveData()
                _dataState.value = ScreenState.Loading
                loadMovies()
            }

            return _dataState
        }

    fun loadMovies(page: Int = 1) = viewModelScope.launch {
        try {
            flow { emit(getMovies.run(page)) }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    _dataState.value = ScreenState.Render(
                        DataState.Error(
                            throwable.localizedMessage ?: "Network error"
                        )
                    )
                }
                .collect {
                    _dataState.value = ScreenState.Render(DataState.Success(it.toPresentationModel().results))
                }
        } catch (e: Exception) {
            _dataState.value = ScreenState.Render(DataState.Error(e.localizedMessage ?: "Network error"))
        }
    }

//        GlobalScope.launch(Dispatchers.Main) {
//        val state = try {
//            ScreenState.Render(
//                DataState.Success(
//
//                    withContext(Dispatchers.IO) { getMovies(page) }.toPresentationModel().results
//                )
//            )
//
//        } catch (e: Exception) {
//            ScreenState.Render(DataState.Error(e.localizedMessage ?: "Network error"))
//        }
//        _dataState.value = state
//    }

//    private fun onMovieLoaded() {
//        _movieListState.value = ScreenState.Render(MovieListState.ShowMovies(mov))
//    }

}