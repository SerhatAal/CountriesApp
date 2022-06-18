package com.example.countriesapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.countriesapp.R
import com.example.countriesapp.data.model.country.Country
import com.example.countriesapp.databinding.FragmentDetailBinding
import com.example.countriesapp.utils.Constants
import com.example.countriesapp.utils.Status
import com.example.countriesapp.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var country: Country? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        country = arguments?.getParcelable(Constants.SHARED_PREFERENCES_KEY)
        country?.let {
            setupAPICall()
            favIconSet()
        }

        binding.imageViewBackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.imageViewFavouriteButton.setOnClickListener {

            country?.let {
                val isInFav = detailViewModel.favouriteManager.countryInFav(it)

                if (isInFav)
                    detailViewModel.favouriteManager.removeCountry(it)
                else
                    detailViewModel.favouriteManager.setCountry(it)
            }
        }

        return binding.root
    }

    private fun setupAPICall() {

        detailViewModel.favouriteManager.getFavLiveData()?.observe(viewLifecycleOwner) {
            favIconSet()
        }

        detailViewModel.fetchCountryDetail(country?.code.orEmpty())
            .observe(viewLifecycleOwner) { countryDetailResponse ->
                when (countryDetailResponse.status) {
                    Status.SUCCESS -> {
                        binding.imageViewFlag.loadImage(countryDetailResponse.data?.data?.flagImageUri)
                        binding.textViewTitleToolbar.text = resources.getString(
                            R.string.country_name,
                            countryDetailResponse.data?.data?.name
                        )
                        binding.textViewCode.text =
                            resources.getString(R.string.countryCode, country?.code)
                        binding.buttonWiki.setOnClickListener {
                            val queryUrl: Uri =
                                Uri.parse("${Constants.WIKI_DATA_URL}${countryDetailResponse.data?.data?.wikiDataId}")
                            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
                            context?.startActivity(intent)
                        }
                        binding.progress.visibility = View.GONE
                        binding.linearLayoutRoot.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.linearLayoutRoot.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        binding.linearLayoutRoot.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            countryDetailResponse.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun favIconSet() {
        country?.let {
            val isInFav = detailViewModel.favouriteManager.countryInFav(it)

            if (isInFav)
                binding.imageViewFavouriteButton.setImageResource(R.drawable.ic_fav_state_white)
            else
                binding.imageViewFavouriteButton.setImageResource(R.drawable.ic_fav_empty_state_white)
        }
    }

}