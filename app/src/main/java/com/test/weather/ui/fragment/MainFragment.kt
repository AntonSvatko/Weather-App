package com.test.weather.ui.fragment

import android.os.Bundle
import android.view.View
import com.test.weather.R
import com.test.weather.databinding.FragmentMainBinding
import com.test.weather.ui.adapter.CitiesAdapter
import com.test.weather.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val adapter: CitiesAdapter by lazy {
        CitiesAdapter {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        viewModel.launchSafely {
            viewModel.getEvenPhoto().collectLatest { even ->
                viewModel.getOddPhoto().collectLatest { odd ->
                    withContext(Dispatchers.Main) {
                        adapter.evenPhoto = even
                        adapter.oddPhoto = odd
                        adapter.submitList(viewModel.cities)
                    }
                }
            }
        }
    }
}