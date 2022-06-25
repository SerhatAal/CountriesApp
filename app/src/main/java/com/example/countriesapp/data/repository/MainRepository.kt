package com.example.countriesapp.data.repository

import com.example.countriesapp.data.api.ApiService
import com.example.countriesapp.data.model.country.CountryResponse
import com.example.countriesapp.data.model.detail.CountryDetailResponse
import com.example.countriesapp.utils.Constants
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getCountries(): CountryResponse {
        return apiService.getCountries(Constants.API_KEY, Constants.LIMIT)
    }

    suspend fun getCountryDetail(countryCode: String): CountryDetailResponse {
        return apiService.getCountryDetail(countryCode, Constants.API_KEY)
    }
}
