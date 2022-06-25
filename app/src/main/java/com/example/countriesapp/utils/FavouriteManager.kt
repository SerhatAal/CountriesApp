package com.example.countriesapp.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.countriesapp.data.model.country.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject

class FavouriteManager @Inject constructor(private val preferences: SharedPreferences) {

    private var favLiveData: MutableLiveData<Boolean>? = null

    fun getCountries(): ArrayList<Country>? {
        val gson = Gson()
        val jsonString: String? = preferences.getString(Constants.SHARED_PREFERENCES_KEY, null)
        val type: Type? = object : TypeToken<ArrayList<Country>>() {}.type

        return gson.fromJson(jsonString, type)
    }

    fun setCountry(country: Country) {
        val countries = getCountries() ?: arrayListOf()
        countries.add(country)

        val editor = preferences.edit()
        val gson = Gson()
        val jsonString: String? = gson.toJson(countries)
        editor.putString(Constants.SHARED_PREFERENCES_KEY, jsonString)
        editor.apply()

        favLiveData?.postValue(true)
    }

    fun removeCountry(country: Country) {
        val countries = getCountries() ?: arrayListOf()

        countries.removeIf { it.code == country.code }

        val editor = preferences.edit()
        val gson = Gson()
        val jsonString: String? = gson.toJson(countries)
        editor.putString(Constants.SHARED_PREFERENCES_KEY, jsonString)
        editor.apply()

        favLiveData?.postValue(true)
    }

    fun countryInFav(country: Country): Boolean {
        val countries = getCountries() ?: arrayListOf()

        for (item in countries) {
            if (country.code == item.code) {
                return true
            }
        }
        return false
    }

    fun getFavLiveData(): MutableLiveData<Boolean>? {
        if (favLiveData == null) favLiveData = MutableLiveData<Boolean>()
        return favLiveData
    }
}
