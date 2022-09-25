package com.test.weather.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.weather.R
import com.test.weather.databinding.FragmentDetailsBinding
import com.test.weather.network.utill.NetworkResult
import com.test.weather.ui.base.fragment.BaseFragment
import com.test.weather.ui.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(R.layout.fragment_details),
    OnMapReadyCallback {
    private val viewModel: DetailsViewModel by viewModels()

    private val args: DetailsFragmentArgs by navArgs()
    private val city by lazy {
        args.city
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val map = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

        viewModel.getWeather(city.coord)
        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            binding.response = response
            if (response is NetworkResult.Success)
                binding.weather = response.data
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(city.coord.lat, city.coord.lon)
        val marker =
            MarkerOptions().position(
                latLng
            )

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                10f
            )
        )
        googleMap.addMarker(marker)
    }

}