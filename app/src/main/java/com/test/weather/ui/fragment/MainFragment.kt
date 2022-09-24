package com.test.weather.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.test.weather.R
import com.test.weather.databinding.FragmentMainBinding
import com.test.weather.ui.adapter.CitiesAdapter
import com.test.weather.ui.base.fragment.BaseFragment
import com.test.weather.utill.CustomQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private lateinit var adapter: CitiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = provideAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.photosLiveData.observe(viewLifecycleOwner) { pair ->
            lifecycleScope.launch {
                pair.first.collect { adapter.oddPhoto = it }
                pair.second.collect { adapter.evenPhoto = it }
                adapter.submitList(viewModel.citiesList)
            }
        }

        binding.searchView.setOnQueryTextListener(CustomQueryTextListener { newText ->
            adapter.submitList(listOf())
            viewModel.getCities(newText)
        })

        lifecycleScope.launch {
            viewModel.searchedCitiesFlow.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun provideAdapter() =
        CitiesAdapter {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
            )
        }
}