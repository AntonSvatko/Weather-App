package com.test.weather.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.weather.R
import com.test.weather.databinding.FragmentMainBinding
import com.test.weather.ui.adapter.CitiesPagingDataAdapter
import com.test.weather.ui.base.fragment.BaseFragment
import com.test.weather.ui.viewmodel.MainViewModel
import com.test.weather.utill.CustomOnScrollListener
import com.test.weather.utill.CustomQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val adapter: CitiesPagingDataAdapter by lazy(this::provideAdapter)


    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        adapter = provideAdapter()
        binding.recyclerView.adapter = adapter
//        binding.recyclerView.pagination()

        binding.searchView.setOnQueryTextListener(CustomQueryTextListener { newText ->
            viewModel.getCities(newText)
        })

        lifecycleScope.launch {
            viewModel.getScansFlow().collectLatest {
                binding.progressBar.visibility = View.GONE
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
    }

    private fun provideAdapter() =
        CitiesPagingDataAdapter {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
            )
        }

    private fun RecyclerView.pagination() {
        val mLayoutManager = LinearLayoutManager(requireContext())
        layoutManager = mLayoutManager
        scrollToPosition(0)
        addOnScrollListener(CustomOnScrollListener(mLayoutManager) {
//            this@MainFragment.adapter.submitList(viewModel.list, 0, PAGE_SIZE * viewModel.i)
            viewModel.i++
        })
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}