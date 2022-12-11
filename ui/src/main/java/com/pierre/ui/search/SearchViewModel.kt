package com.pierre.ui.search

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.pierre.domain.usecases.SearchProductUseCase
import com.pierre.ui.search.mapper.UiProductMapper
import com.pierre.ui.search.model.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductUseCase: SearchProductUseCase,
    private val mapper: UiProductMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state

    // Use case able to analyse an image to detect a barcode (launched on a single worker thread)
    val imageAnalysisUseCase by lazy {
        ImageAnalysis
            .Builder()
            .build()
            .apply { setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy -> processImageProxy(scanner, imageProxy) } }
    }

    fun search(code: String) {
        viewModelScope.launch {
            _state.emit(SearchState.Loading)
            try { searchProduct(code) }
            catch (e: Exception) { onError(e) }
        }
    }

    /**
     * We consider a codebar to be valid if it has more than 12 chars
     * Maybe we should find a better regex for this
     */
    fun codeIsValid(code: String) =
        code.length >= 12

    fun openScanner() {
        viewModelScope.launch {
            _state.emit(SearchState.Scanner)
        }
    }

    private suspend fun searchProduct(code: String) {
        val product = mapper.toUi(searchProductUseCase.invoke(code))
        _state.emit(SearchState.Success(product))
    }

    private suspend fun onError(e: Exception) {
        _state.emit(SearchState.Error(e))
        _state.emit(SearchState.Initial)
    }

    private val scanner by lazy {
        BarcodeScanning.getClient(
            BarcodeScannerOptions
                .Builder()
                .setBarcodeFormats(Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E)
                .build())
    }

    /**
     * Analyse the image proxy and launch a search with it if it finds one
     */
    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees,)

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)?.rawValue
                    if (barcode != null && codeIsValid(barcode)) search(barcode)
                }
                .addOnFailureListener {
                    // This failure will happen if the barcode scanning model fails to download from Google Play Services
                    // Silent fail (for now...)
                }.addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must call image.close() on received images when finished
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }
}