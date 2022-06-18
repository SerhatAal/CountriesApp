package com.example.countriesapp.ui.favourites

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countriesapp.R
import com.example.countriesapp.data.model.country.Country
import com.example.countriesapp.databinding.FragmentFavouritesBinding
import com.example.countriesapp.ui.main.MainViewModel
import com.example.countriesapp.utils.ClickListener
import com.example.countriesapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(), ClickListener {
    private lateinit var binding: FragmentFavouritesBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val adapter: FavouritesAdapter by lazy {
        FavouritesAdapter(this, mainViewModel.favouriteManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        setupUI()

        return binding.root
    }

    private fun setupUI() {
        checkEmptyState()
        mainViewModel.favouriteManager.getFavLiveData()?.observe(viewLifecycleOwner) {
            adapter.setData(mainViewModel.favouriteManager.getCountries() ?: arrayListOf())

            checkEmptyState()
        }

        binding.recyclerViewCountriesInFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCountriesInFav.adapter = adapter
    }

    override fun onClickData(country: Country) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.SHARED_PREFERENCES_KEY, country)
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_favouritesFragment_to_detailFragment, bundle)
        }
    }

    private fun checkEmptyState() {
        val countries = mainViewModel.favouriteManager.getCountries() ?: arrayListOf()
        if (countries.isEmpty()) {
            binding.textViewEmptyState.visibility = View.VISIBLE
            binding.recyclerViewCountriesInFav.visibility = View.GONE
            binding.progressBarFav.visibility = View.GONE
        } else {
            binding.textViewEmptyState.visibility = View.GONE
            binding.recyclerViewCountriesInFav.visibility = View.VISIBLE
        }
    }
}
