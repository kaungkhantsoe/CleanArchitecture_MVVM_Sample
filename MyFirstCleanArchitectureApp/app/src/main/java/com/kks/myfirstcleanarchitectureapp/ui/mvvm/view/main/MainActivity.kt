package com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.main

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.RequestManager
import com.kks.myfirstcleanarchitectureapp.common.ViewBindingActivity
import com.kks.myfirstcleanarchitectureapp.common.gone
import com.kks.myfirstcleanarchitectureapp.common.visible
import com.kks.myfirstcleanarchitectureapp.databinding.ActivityMainBinding
import com.kks.myfirstcleanarchitectureapp.ui.common.DataState
import com.kks.myfirstcleanarchitectureapp.ui.common.ScreenState
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ViewBindingActivity<ActivityMainBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var requestManager: RequestManager

    private val viewModel: MainViewModel by viewModels()

    private val adapter by lazy {
        MainAdapter(requestManager)
    }

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setup() {

        binding.swipeRefresh.setOnRefreshListener(this)

        binding.recyclerMovieList.adapter = adapter
        binding.recyclerMovieList.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.recyclerMovieList.setHasFixedSize(true)

        viewModel.dataState.observe(::getLifecycle, ::updateUI)
    }

    private fun updateUI(screenState: ScreenState<DataState>) {
        when (screenState) {
            ScreenState.Loading -> binding.progress.visible()
            is ScreenState.Render -> processRenderState(screenState.renderState)
        }
    }

    private fun processRenderState(renderState: DataState) {
        binding.progress.gone()
        when(renderState) {
            is DataState.Success -> {
                if(renderState.data is List<*>) {
                    addMovies(renderState.data as List<Movie>)
                }
            }
            is DataState.Error -> {
                showToast(renderState.message)
            }
        }
    }

    private fun addMovies(movies: List<Movie>) {
        adapter.add(movies)
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        adapter.clear()
        viewModel.loadMovies()
    }

}