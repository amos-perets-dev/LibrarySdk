package com.example.mylibrary.screens.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mylibrary.R
import com.example.mylibrary.screens.base.BaseFragment
import com.example.mylibrary.utils.permissions.IPermissionsUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.functions.Functions
import kotlinx.android.synthetic.main.fragment_welcome.view.*
import org.koin.android.ext.android.inject

class WelcomeFragment : BaseFragment() {

    private val viewModel by inject<WelcomeViewModel>()
    private val permissionsUtil  by inject<IPermissionsUtil>()

    override fun getLayout(): Int  = R.layout.fragment_welcome

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.button_first.setOnClickListener { viewModel.onClickStartScan() }

        viewModel.nextPage.observe(viewLifecycleOwner, Observer(findNavController()::navigate))
        viewModel.permissionNeeded.observe(viewLifecycleOwner, Observer(this::showPermissionDialog))

    }

    /**
     * Shw the permission dialog
     */
    private fun showPermissionDialog(permissionName: String) {
        compositeDisposable.add(
            permissionsUtil.isPermissionGranted(requireActivity(), permissionName)
                .subscribe(viewModel::onClickPermissionRequest, Throwable::printStackTrace)
        )
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

}
