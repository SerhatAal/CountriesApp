package com.example.countriesapp.data.model.country

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("data")
    val data: List<Country>
)