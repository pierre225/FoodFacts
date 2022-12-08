package com.pierre.ui.search

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.pierre.ui.MainActivity
import com.pierre.ui.R
import com.pierre.ui.base.BaseFragment
import com.pierre.ui.databinding.FragmentSearchBinding
import com.pierre.ui.search.model.SearchState
import com.pierre.ui.search.model.UiProduct
import com.pierre.ui.search.utils.ExceptionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.Executors

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    private val currentSearch get() = binding.search.text?.toString() ?: ""

    override fun initBinding(inflater: LayoutInflater): ViewBinding {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            search.addTextChangedListener { search() }
            scannerButton.setOnClickListener { onClickScanner() }
        }

        observeSearch()
    }

    override fun onDestroyView() {
        binding.scannerView.releaseCameraProvider()
        super.onDestroyView()
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
            is SearchState.Scanner -> displayScannerState(true)
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
        displayScannerState(false)
    }

    /**
     * Show the scanner view and start scanning or hide and stop scanning
     */
    private fun displayScannerState(display: Boolean) {
        binding.scannerView.apply {
            visibility = if (display) View.VISIBLE else View.GONE
            if (display) startScanning(this@SearchFragment, searchViewModel.imageAnalysisUseCase)
            else stopScanning()
        }
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

                product.ingredients?.also { ingredients.text = HtmlCompat.fromHtml(it, 1) }

                Glide.with(image)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(image)
            }
        }
    }

    private fun search(code: String = currentSearch) {
        if (code.isNotBlank() && searchViewModel.codeIsValid(code)) {
            searchViewModel.search(code)
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) openScanner()
        else Toast.makeText(context, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show()
    }

    private fun hasCameraPermission() =
        context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED } == true

    private fun onClickScanner() {
        if (hasCameraPermission()) openScanner()
        else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun openScanner() {
        searchViewModel.openScanner()
    }
}