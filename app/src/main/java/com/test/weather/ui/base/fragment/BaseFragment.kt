package com.test.weather.ui.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.test.weather.ui.viewmodel.MainViewModel

abstract class BaseFragment<T : ViewDataBinding>(private val resId: Int) : Fragment() {

    protected lateinit var binding: T
    protected val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, resId, container, false
        )

        binding.lifecycleOwner = this

        return binding.root
    }

}