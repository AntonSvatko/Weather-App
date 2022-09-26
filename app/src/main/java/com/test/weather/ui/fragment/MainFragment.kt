package com.test.weather.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.weather.R
import com.test.weather.data.entity.City
import com.test.weather.databinding.FragmentMainBinding
import com.test.weather.ui.adapter.CitiesAdapter
import com.test.weather.ui.base.fragment.BaseFragment
import com.test.weather.ui.viewmodel.MainViewModel
import com.test.weather.utill.CustomOnScrollListener
import com.test.weather.utill.CustomQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private lateinit var adapter: CitiesAdapter
    private var list = listOf<City>()
    private var i = 1

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.citiesList.collect { listCities ->
                adapter = provideAdapter(listCities)
                binding.recyclerView.pagination()
                binding.recyclerView.adapter = adapter

                binding.searchView.setOnQueryTextListener(CustomQueryTextListener { newText ->
                    viewModel.getCities(newText)
                })

                viewModel.searchedCitiesFlow.flowWithLifecycle(lifecycle).collect { list ->
                    this@MainFragment.list = list
                    adapter.submitList(list, 0, PAGE_SIZE)
                    i = 1
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }
    }

    private fun provideAdapter(list: List<City>) =
        CitiesAdapter(list) {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
            )
        }

    private fun RecyclerView.pagination() {
        val mLayoutManager = LinearLayoutManager(requireContext())
        layoutManager = mLayoutManager
        scrollToPosition(0)
        addOnScrollListener(CustomOnScrollListener(mLayoutManager) {
            this@MainFragment.adapter.submitList(list, 0, PAGE_SIZE * i)
            i++
        })
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}