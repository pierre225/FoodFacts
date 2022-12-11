package com.pierre.ui.search.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.pierre.ui.R
import com.pierre.ui.databinding.ProductCardBinding
import com.pierre.ui.databinding.ScannerCardBinding
import com.pierre.ui.search.model.UiProduct

/**
 * The product view displays one product information
 */
class ProductView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: ProductCardBinding

    init {
        binding = ProductCardBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setProduct(product: UiProduct) {
        binding.apply {
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