package com.mahmoudibrahem.mapsplayground.ui.tracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.os.CountDownTimer
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.mahmoudibrahem.mapsplayground.R
import pub.devrel.easypermissions.EasyPermissions

class TrackerViewModel : ViewModel() {

    fun startCountDown(
        onTick: (String) -> Unit,
        onFinish: () -> Unit
    ) {
        val timer: CountDownTimer = object : CountDownTimer(4000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                onTick.invoke((millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                onFinish.invoke()
            }
        }
        timer.start()
    }

    @SuppressLint("InlinedApi")
    fun hasLocationPermission(context: Context):Boolean{
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }

}