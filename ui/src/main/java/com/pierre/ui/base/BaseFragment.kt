package com.pierre.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.pierre.ui.R
import com.pierre.ui.databinding.FragmentBaseBinding
import com.pierre.ui.search.utils.ExceptionUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * A class that contains what is common to our Fragments so we don't need to implement it in each.
 * This BaseFragment can show messages, errors.
 *
 * Additionally, it could handle Rx disposables, events, logging...
 *
 */
@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    private lateinit var baseBinding: FragmentBaseBinding

    // Child view binding
    abstract fun initBinding(inflater: LayoutInflater): ViewBinding

    // BaseFragment binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val childView = initBinding(inflater).root

        baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        baseBinding.baseContainer.addView(childView)

        return baseBinding.root
    }

    /**
     * Shows or hides a loader
     */
    protected fun displayLoadingState(display: Boolean) {
        baseBinding.baseLoader.visibility =
            if (display) View.VISIBLE else View.GONE
    }

    /**
     * Displays the error in a Snackbar, if there is a retry action, show the retry button
     */
    protected fun displayErrorState(e: Exception, retry: OnClickListener?) {
        displayLoadingState(false)
        context?.also { context ->
            Snackbar.make(
                baseBinding.baseRoot,
                ExceptionUtils.messageFromException(e, context),
                Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, retry).show()
        }
    }
}
