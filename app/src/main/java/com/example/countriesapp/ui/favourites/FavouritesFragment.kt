package com.example.countriesapp.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countriesapp.R
import com.example.countriesapp.data.model.country.Country
import com.example.countriesapp.databinding.FragmentFavouritesBinding
import com.example.countriesapp.ui.main.MainViewModel
import com.example.countriesapp.utils.ClickListener
import com.example.countriesapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites), ClickListener {
    private val binding by viewBinding(FragmentFavouritesBinding::bind)

    private val mainViewModel: MainViewModel by viewModels()

    private val favouriteAdapter: FavouritesAdapter by lazy {
        FavouritesAdapter(this, mainViewModel.favouriteManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        checkEmptyState()

        mainViewModel.favouriteManager.getFavLiveData()?.observe(viewLifecycleOwner) {
            favouriteAdapter.setData(mainViewModel.favouriteManager.getCountries() ?: arrayListOf())

            checkEmptyState()
        }

        binding.recyclerViewCountriesInFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouriteAdapter
        }
    }

    override fun onClickData(country: Country) {
        val bundle = Bundle()
        bundle.putParcelable("country", country)
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_favouritesFragment_to_detailFragment, bundle)
        }
    }

    private fun checkEmptyState() {
        val countries = mainViewModel.favouriteManager.getCountries() ?: arrayListOf()

        with(binding) {
            if (countries.isEmpty()) {
                textViewEmptyState.visibility = View.VISIBLE
                recyclerViewCountriesInFav.visibility = View.GONE
                progressBarFav.visibility = View.GONE
            } else {
                textViewEmptyState.visibility = View.GONE
                recyclerViewCountriesInFav.visibility = View.VISIBLE
            }
        }
    }
}
