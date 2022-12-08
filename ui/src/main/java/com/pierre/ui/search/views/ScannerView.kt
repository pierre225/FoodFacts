package com.pierre.ui.search.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.pierre.ui.databinding.ScannerCardBinding

/**
 * The Scnner view can start and stop a camera that will
 * send its captures to a usecase (imageAnalysisUseCase)
 */
class ScannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: ScannerCardBinding

    init {
        binding = ScannerCardBinding.inflate(LayoutInflater.from(context), this)
        orientation = VERTICAL
        configureCameraProvider()
    }

    private var cameraProvider: ProcessCameraProvider? = null

    /**
     * Retrieve and store the camera provider
     */
    private fun configureCameraProvider() {
        context?.also { context ->
            ProcessCameraProvider.getInstance(context).also {
                it.addListener({ cameraProvider = it.get() }, ContextCompat.getMainExecutor(context))
            }
        }
    }

    private val previewUseCase by lazy {
        Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(binding.scanner.surfaceProvider) }
    }

    fun releaseCameraProvider() {
        cameraProvider?.shutdown()
        cameraProvider = null
    }

    fun startScanning(lifecycleOwner: LifecycleOwner, imageAnalysisUseCase: UseCase) {
        try {
            cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                imageAnalysisUseCase)
        } catch (illegalStateException: IllegalStateException) {
            // If the use case has already been bound to another lifecycle or method is not called on main thread.
            Toast.makeText(context, illegalStateException.message.orEmpty(), Toast.LENGTH_SHORT).show()
        } catch (illegalArgumentException: IllegalArgumentException) {
            // If the provided camera selector is unable to resolve a camera to be used for the given use cases.
            Toast.makeText(context, illegalArgumentException.message.orEmpty(), Toast.LENGTH_SHORT).show()
        }
    }

    fun stopScanning() {
        cameraProvider?.unbindAll()
    }

}