package com.kks.myfirstcleanarchitectureapp.ui.common

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
sealed class DataState {
    class Success(val data: Any): DataState()
    class Error(val message: String) : DataState()
}