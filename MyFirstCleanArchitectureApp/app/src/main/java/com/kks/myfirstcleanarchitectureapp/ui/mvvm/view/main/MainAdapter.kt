package com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kks.myfirstcleanarchitectureapp.R
import com.kks.myfirstcleanarchitectureapp.common.BaseAdapter
import com.kks.myfirstcleanarchitectureapp.common.inflate
import com.kks.myfirstcleanarchitectureapp.databinding.ItemMovieBinding
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder


/**
 * Created by kaungkhantsoe on 5/17/21.
 **/
class MainAdapter(
    private val requestManager: RequestManager,
    private val listener: MainListener
) : BaseAdapter() {

    override fun onCreateCustomViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = ViewHolder(
        parent.inflate(
            R.layout.item_movie
        )
    )

    override fun onBindCustomViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(getItemsList()[position] as Movie)
    }

    override fun onCreateCustomHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder? = null


    override fun onBindCustomHeaderViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    }

    inner class ViewHolder(private val view: View) : ParallaxViewHolder(view) {
        private val binding = ItemMovieBinding.bind(view)

        fun bind(movie: Movie) {

            Glide.with(view.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(binding.imageMoviePoster)
            binding.textMovieTitle.text = "${adapterPosition+1} ${movie.originalTitle}"

            itemView.setOnClickListener {
                listener.onClickMovie(movie.id,binding.imageMoviePoster)
            }

            this.backgroundImage.reuse()
        }

        override fun getParallaxImageId(): Int {
            return R.id.image_movie_poster
        }
    }
}