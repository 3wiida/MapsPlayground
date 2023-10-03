package com.mahmoudibrahem.mapsplayground.ui.home

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

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

}