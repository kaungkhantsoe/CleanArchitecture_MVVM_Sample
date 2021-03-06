package com.kks.myfirstcleanarchitectureapp.ui.common

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
sealed class ScreenState<out T> {
    object Loading : ScreenState<Nothing>()
    class Render<T>(val renderState: T) : ScreenState<T>()
}