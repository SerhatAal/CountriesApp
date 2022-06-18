package com.example.countriesapp.ui.favourites

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

class FavouritesAdapter(
    private val listener: ClickListener,
    private val favouriteManager: FavouriteManager
) : RecyclerView.Adapter<FavouritesAdapter.ItemViewHolder>() {

    private var countriesInFav = favouriteManager.getCountries() ?: arrayListOf()

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCountryBinding.bind(itemView)

        fun bind(country: Country) {
            binding.textViewCountryName.text = country.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_country, parent,
                false
            )
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(countriesInFav[position])
        holder.binding.itemCountry.setOnClickListener {
            listener.onClickData(countriesInFav[position])
        }

        holder.binding.imageViewFavouriteState.setImageResource(R.drawable.ic_fav_state)

        holder.binding.imageViewFavouriteState.setOnClickListener {
            favouriteManager.removeCountry(countriesInFav[position])
        }
    }

    override fun getItemCount(): Int = countriesInFav.size

    fun setData(countries: ArrayList<Country>) {
        this.countriesInFav = countries
        notifyDataSetChanged()
    }
}