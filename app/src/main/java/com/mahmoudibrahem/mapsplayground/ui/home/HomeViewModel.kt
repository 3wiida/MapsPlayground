package com.mahmoudibrahem.mapsplayground.ui.home

import android.Manifest
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    fun hasLocationPermission(context: Context): Boolean {
        return EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    companion object{
        enum class HomeTo{
            PLAYGROUND,
            TRACKER
        }
    }

}