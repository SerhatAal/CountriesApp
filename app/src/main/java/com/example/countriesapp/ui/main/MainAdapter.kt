package com.example.countriesapp.ui.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapp.R
import com.example.countriesapp.data.model.country.Country
import com.example.countriesapp.databinding.ItemCountryBinding
import com.example.countriesapp.utils.ClickListener
import com.example.countriesapp.utils.FavouriteManager


class MainAdapter constructor(
    private val listener: ClickListener,
    private val favouriteManager: FavouriteManager
) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    private var countries: ArrayList<Country> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCountryBinding.bind(itemView)
        fun bind(country: Country) {
            binding.textViewCountryName.text = country.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_country, parent,
                false
            )
        )

    override fun getItemCount(): Int = countries.size

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.binding.itemCountry.setOnClickListener {
            listener.onClickData(countries[position])
        }

        val isFav = favouriteManager.countryInFav(countries[position])

        if (isFav) {
            holder.binding.imageViewFavouriteState.setImageResource(R.drawable.ic_fav_state)
        } else {
            holder.binding.imageViewFavouriteState.setImageResource(R.drawable.ic_fav_empty_state)
        }

        holder.binding.imageViewFavouriteState.setOnClickListener {
            val isInFav = favouriteManager.countryInFav(countries[position])

            if (isInFav) {
                favouriteManager.removeCountry(countries[position])
            } else {
                favouriteManager.setCountry(countries[position])
            }

            notifyDataSetChanged()
        }

    }

    fun setData(countries: List<Country>) {
        this.countries.apply {
            clear()
            addAll(countries)
        }
    }
}
