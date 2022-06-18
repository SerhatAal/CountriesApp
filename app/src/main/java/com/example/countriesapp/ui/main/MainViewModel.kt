package com.example.countriesapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.countriesapp.data.repository.MainRepository
import com.example.countriesapp.utils.Resource
import com.example.countriesapp.utils.FavouriteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    val favouriteManager: FavouriteManager
) : ViewModel() {
    fun fetchCountries() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(data = mainRepository.getCountries()))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
}