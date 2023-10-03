package com.mahmoudibrahem.mapsplayground.ui.permission_screen

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.util.Constants.LOCATION_PERMISSION_REQ_CODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {

    fun hasLocationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= 33) {
            EasyPermissions.hasPermissions(
                context, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else if (Build.VERSION.SDK_INT >= 29) {
            EasyPermissions.hasPermissions(
                context, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context, Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
    }

    fun requestLocationPermission(host: Fragment) {
        if (Build.VERSION.SDK_INT >= 33) {
            EasyPermissions.requestPermissions(
                host,
                host.getString(R.string.permission_rational),
                LOCATION_PERMISSION_REQ_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else if (Build.VERSION.SDK_INT >= 29) {
            EasyPermissions.requestPermissions(
                host,
                host.getString(R.string.permission_rational),
                LOCATION_PERMISSION_REQ_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        } else {
            EasyPermissions.requestPermissions(
                host,
                host.getString(R.string.permission_rational),
                LOCATION_PERMISSION_REQ_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
    }


}