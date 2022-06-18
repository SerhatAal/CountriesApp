package com.example.countriesapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countriesapp.R
import com.example.countriesapp.data.model.country.Country
import com.example.countriesapp.databinding.FragmentMainBinding
import com.example.countriesapp.utils.ClickListener
import com.example.countriesapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), ClickListener {
    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        setupUI()
        setupAPICall()

        return binding.root
    }

    private fun setupUI() {
        binding.recyclerViewCountries.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(this, mainViewModel.favouriteManager)
        binding.recyclerViewCountries.adapter = adapter
    }

    private fun setupAPICall() {
        mainViewModel.fetchCountries().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { countryData -> adapter.setData(countryData.data) }
                    binding.recyclerViewCountries.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewCountries.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.recyclerViewCountries.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onClickData(country: Country) {
        val bundle = Bundle()
        bundle.putParcelable("country", country)
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_mainFragment_to_detailFragment, bundle)
        }
    }
}