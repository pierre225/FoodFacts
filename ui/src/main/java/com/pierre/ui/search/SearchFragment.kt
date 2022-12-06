package com.pierre.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.pierre.ui.base.BaseFragment
import com.pierre.ui.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A splash screen displayed to the user while we load the data in the background
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun initBinding(inflater: LayoutInflater): ViewBinding {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}