package com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.moviedetail

import android.view.LayoutInflater
import com.kks.myfirstcleanarchitectureapp.common.ViewBindingActivity
import com.kks.myfirstcleanarchitectureapp.databinding.ActivityMovieDetailBinding

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
class MovieDetailActivity: ViewBindingActivity<ActivityMovieDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMovieDetailBinding
        get() = ActivityMovieDetailBinding::inflate

    override fun setup() {

    }
}