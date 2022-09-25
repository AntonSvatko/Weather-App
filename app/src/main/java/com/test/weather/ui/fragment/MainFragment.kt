package com.test.weather.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.weather.R
import com.test.weather.data.entity.City
import com.test.weather.databinding.FragmentMainBinding
import com.test.weather.ui.adapter.CitiesAdapter
import com.test.weather.ui.base.fragment.BaseFragment
import com.test.weather.ui.viewmodel.MainViewModel
import com.test.weather.utill.CustomOnScrollListener
import com.test.weather.utill.CustomQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private lateinit var adapter: CitiesAdapter
    private var list = listOf<City>()
    private var i = 1

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = provideAdapter()
        binding.recyclerView.adapter = adapter
        pagination()

        binding.searchView.setOnQueryTextListener(CustomQueryTextListener { newText ->
            viewModel.getCities(newText)
        })

        lifecycleScope.launch {
            viewModel.searchedCitiesFlow.flowWithLifecycle(lifecycle).collect { list ->
                this@MainFragment.list = list
                adapter.submitList(
                    kotlin.runCatching { list.subList(0, PAGE_SIZE) }.getOrDefault(list)
                )
                i = 1
                binding.recyclerView.scrollToPosition(0)
            }
        }
    }

    private fun provideAdapter() =
        CitiesAdapter(viewModel.citiesList.orEmpty()) {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
            )
        }

    private fun pagination() {
        val mLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.scrollToPosition(0)
        binding.recyclerView.addOnScrollListener(CustomOnScrollListener {
            val visibleItemCount = mLayoutManager.childCount
            val totalItemCount = mLayoutManager.itemCount
            val pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                adapter.submitList(
                    kotlin.runCatching { list.subList(0, PAGE_SIZE * i) }.getOrDefault(list)
                )
                i++
            }
        })
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}