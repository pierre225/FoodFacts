package com.pierre.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.pierre.ui.R
import com.pierre.ui.base.BaseFragment
import com.pierre.ui.databinding.FragmentSearchBinding
import com.pierre.ui.search.model.SearchState
import com.pierre.ui.search.model.UiProduct
import com.pierre.ui.search.utils.ExceptionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    override fun initBinding(inflater: LayoutInflater): ViewBinding {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.search.addTextChangedListener { search() }

        observeSearch()
    }

    private fun observeSearch() {
        lifecycleScope.launchWhenStarted {
            searchViewModel.state.collectLatest {
                handleState(it)
            }
        }
    }

    private fun handleState(state: SearchState) {
        hideStateViews()
        when (state) {
            is SearchState.Initial -> displayInitialState(true)
            is SearchState.Loading -> displayLoadingState(true)
            is SearchState.Success -> displaySuccessState(state.uiProduct)
            is SearchState.Error -> displayErrorState(
                e = state.e,
                retry =
                    if (ExceptionUtils.canRetry(state.e)) OnClickListener { search() }
                    else null)
        }
    }

    private fun hideStateViews() {
        displayLoadingState(false)
        displaySuccessState(null)
        displayInitialState(false)
    }

    private fun displayInitialState(display: Boolean) {
        binding.initialCard.root.visibility = if (display) View.VISIBLE else View.GONE
    }

    private fun displaySuccessState(product: UiProduct?) {
        binding.productCard.apply {
            if (product == null) {
                root.visibility = View.GONE
            } else {
                root.visibility = View.VISIBLE
                name.text = product.name
                brand.text = product.brand
                description.text = product.description
                ingredients.text = product.ingredients
                Glide.with(image)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(image)
            }
        }
    }

    private fun search() {
        val code = binding.search.text?.toString()
        if (!code.isNullOrBlank() && searchViewModel.codeIsValid(code)) {
            searchViewModel.search(code)
        }
    }
}