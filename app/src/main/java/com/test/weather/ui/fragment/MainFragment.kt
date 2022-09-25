package com.test.weather.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.test.weather.R
import com.test.weather.databinding.FragmentMainBinding
import com.test.weather.ui.adapter.CitiesAdapter
import com.test.weather.ui.base.fragment.BaseFragment
import com.test.weather.ui.viewmodel.MainViewModel
import com.test.weather.utill.CustomQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private lateinit var adapter: CitiesAdapter

    protected val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = provideAdapter()
        binding.recyclerView.adapter = adapter

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
        CitiesAdapter(viewModel.citiesList.orEmpty()) {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
            )
        }
}