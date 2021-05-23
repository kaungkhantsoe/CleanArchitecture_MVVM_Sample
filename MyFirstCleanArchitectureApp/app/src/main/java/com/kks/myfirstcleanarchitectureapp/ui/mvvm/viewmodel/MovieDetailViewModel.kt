package com.kks.myfirstcleanarchitectureapp.ui.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toPresentationModel
import com.kks.usecases.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by kaungkhantsoe on 20/05/2021.
 **/
@HiltViewModel
class MovieDetailViewModel
@Inject
constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel()
{
    fun getDetail(id: Int): Movie =
        movieUseCase.getMovie(id).toPresentationModel()

}