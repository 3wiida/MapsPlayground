package com.mahmoudibrahem.mapsplayground.ui.permission_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.databinding.PermissionFragmentLayoutBinding
import com.mahmoudibrahem.mapsplayground.util.Enums
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class PermissionFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var binding: PermissionFragmentLayoutBinding? = null
    private val viewModel: PermissionViewModel by viewModels()
    private val args: PermissionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PermissionFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
    }

    private fun initClicks() {
        binding?.btnContinue?.setOnClickListener {
            if (viewModel.hasLocationPermission(requireContext())) {
                when (args.direction as Enums.HomeTo) {
                    Enums.HomeTo.PLAYGROUND -> {
                        findNavController().navigate(R.id.action_permissionFragment_to_playgroundFragment)
                    }

                    Enums.HomeTo.TRACKER -> {
                        findNavController().navigate(R.id.action_permissionFragment_to_trackerFragment)
                    }
                }
            } else {
                viewModel.requestLocationPermission(this)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (args.direction as Enums.HomeTo) {
            Enums.HomeTo.PLAYGROUND -> {
                findNavController().navigate(R.id.action_permissionFragment_to_playgroundFragment)
            }

            Enums.HomeTo.TRACKER -> {
                findNavController().navigate(R.id.action_permissionFragment_to_trackerFragment)
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}