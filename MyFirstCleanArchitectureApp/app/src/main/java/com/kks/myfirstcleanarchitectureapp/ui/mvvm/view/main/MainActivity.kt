package com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.main

import android.content.Intent
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
        // Construct an Intent as normal
        // Construct an Intent as normal
        val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ID_EXTRA, id)

        // BEGIN_INCLUDE(start_activity)
        /*
     * Now create an {@link android.app.ActivityOptions} instance using the
     * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
     * method.
     */

        // BEGIN_INCLUDE(start_activity)
        /*
     * Now create an {@link android.app.ActivityOptions} instance using the
     * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
     * method.
     */
        val activityOptions: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity,  // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                Pair(
                    view,
                    MovieDetailActivity.VIEW_NAME_MOVIE_POSTER
                )

            )

        // Now we can start the Activity, providing the activity options as a bundle

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(this@MainActivity, intent, activityOptions.toBundle())
        // END_INCLUDE(start_activity)
    }

}