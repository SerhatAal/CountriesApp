package com.example.countriesapp.utils

import com.example.countriesapp.data.model.country.Country

interface ClickListener {
    fun onClickData (country: Country)
}