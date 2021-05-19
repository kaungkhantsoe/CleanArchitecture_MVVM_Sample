package com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.kks.myfirstcleanarchitectureapp.R
import com.kks.myfirstcleanarchitectureapp.common.BaseAdapter
import com.kks.myfirstcleanarchitectureapp.common.inflate
import com.kks.myfirstcleanarchitectureapp.databinding.ItemMovieBinding
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie

/**
 * Created by kaungkhantsoe on 5/17/21.
 **/
class MainAdapter(private val requestManager: RequestManager) : BaseAdapter(){

    override fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ViewHolder(parent.inflate(R.layout.item_movie))

    override fun onBindCustomViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(getItemsList()[position] as Movie)
    }

    override fun onCreateCustomHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? = null


    override fun onBindCustomHeaderViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    }

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemMovieBinding.bind(view)

        fun bind(movie: Movie) {
            requestManager.load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(binding.imageMovie)
            binding.textMovieTitle.text = movie.originalTitle
        }
    }
}