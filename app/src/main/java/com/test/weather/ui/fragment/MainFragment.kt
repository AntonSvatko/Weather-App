package com.test.weather.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.test.weather.R
import com.test.weather.databinding.FragmentMainBinding
import com.test.weather.ui.adapter.CitiesPagingDataAdapter
import com.test.weather.ui.base.fragment.BaseFragment
import com.test.weather.ui.viewmodel.MainViewModel
import com.test.weather.utill.CustomQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val adapter: CitiesPagingDataAdapter by lazy(this::provideAdapter)
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(CustomQueryTextListener { newText ->
            viewModel.getCities(newText)
        })

        lifecycleScope.launch {
            viewModel.getAllCities.collectLatest {
                adapter.submitData(it)
            }
        }

        viewModel.ld.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(PagingData.empty())
                binding.recyclerView.scrollToPosition(0)
                adapter.submitData(it)
            }
        }

        adapter.addLoadStateListener {
            if ((it.refresh is LoadState.NotLoading) && adapter.itemCount != 0)
                binding.progressBar.visibility = View.GONE
        }
    }

    private fun provideAdapter() =
        CitiesPagingDataAdapter {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
            )
        }
}