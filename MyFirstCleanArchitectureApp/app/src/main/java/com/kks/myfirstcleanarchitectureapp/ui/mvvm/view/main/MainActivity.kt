package com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.main

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.kks.myfirstcleanarchitectureapp.common.SmartScrollListener
import com.kks.myfirstcleanarchitectureapp.common.ViewBindingActivity
import com.kks.myfirstcleanarchitectureapp.common.gone
import com.kks.myfirstcleanarchitectureapp.common.visible
import com.kks.myfirstcleanarchitectureapp.databinding.ActivityMainBinding
import com.kks.myfirstcleanarchitectureapp.ui.common.DataState
import com.kks.myfirstcleanarchitectureapp.ui.common.ScreenState
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.moviedetail.MovieDetailActivity
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ViewBindingActivity<ActivityMainBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    SmartScrollListener.OnSmartScrollListener,
MainListener{

    @Inject
    lateinit var requestManager: RequestManager

    private val viewModel: MainViewModel by viewModels()

    private lateinit var layoutManager: LinearLayoutManager

    private val adapter by lazy {
        MainAdapter(requestManager,this)
    }

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setup() {

        binding.swipeRefresh.setOnRefreshListener(this)

        layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.recyclerMovieList.adapter = adapter
        binding.recyclerMovieList.layoutManager = layoutManager
        binding.recyclerMovieList.setHasFixedSize(true)
        binding.recyclerMovieList.addOnScrollListener(SmartScrollListener(this))

        viewModel.screenState.observe(::getLifecycle, ::updateUI)
    }

    private fun updateUI(screenState: ScreenState<DataState>) {
        when (screenState) {
            ScreenState.Loading -> binding.progress.visible()
            is ScreenState.Render -> processRenderState(screenState.renderState)
        }
    }

    private fun processRenderState(renderState: DataState) {
        binding.progress.gone()
        adapter.clearFooter()
        when(renderState) {
            is DataState.Success -> {
                if(renderState.data is List<*>) {
                    addMovies(renderState.data as List<Movie>)
//                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                    },10000)
                }
            }
            is DataState.Error -> {
                showToast(renderState.message)
            }
            is DataState.EndReach -> {
                Snackbar.make(binding.contextView,"You have reached the end",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun addMovies(movies: List<Movie>) {
        adapter.clearFooter()
        adapter.add(movies)
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        adapter.clear()
        viewModel.pageNumber = 1
    }

    override fun onListEndReach() {
        adapter.showLoading()
        viewModel.pageNumber++
    }

    override fun onClickMovie(id: Int,view: View) {

        val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ID_EXTRA, id)

        val activityOptions: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity,
                Pair(
                    view,
                    MovieDetailActivity.VIEW_NAME_MOVIE_POSTER
                )

            )

        ActivityCompat.startActivity(this@MainActivity, intent, activityOptions.toBundle())
    }

}