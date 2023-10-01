package com.mahmoudibrahem.mapsplayground.util.playground_util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object CameraPlayground {

    private var cameraTilt = 0f
    fun showBuilding(map: GoogleMap?, isEnabled: Boolean): Float {
        cameraTilt = if (isEnabled) 45f else 0f
        map?.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition
                    .builder()
                    .apply {
                        target(map.cameraPosition.target)
                        tilt(cameraTilt)
                        bearing(100f)
                        zoom(18f)
                    }
                    .build()
            )
        )
        return cameraTilt
    }

    fun moveCamera(map: GoogleMap?, position: LatLng) {
        map?.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition
                    .builder()
                    .apply {
                        target(position)
                        tilt(cameraTilt)
                        bearing(100f)
                        zoom(18f)
                    }
                    .build()
            )
        )
    }

    fun animateCamera(map: GoogleMap?, position: LatLng) {
        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition
                    .builder()
                    .apply {
                        target(position)
                        tilt(cameraTilt)
                        zoom(12f)
                    }
                    .build()
            )
        )
    }

    fun moveCameraToLatLngBounds(map: GoogleMap?, bounds: LatLngBounds, padding: Int) {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(bounds, padding)
        )
    }

    fun moveCameraToLatLngBoundsWithRestrict(map: GoogleMap?, bounds: LatLngBounds, padding: Int) {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(bounds, padding)
        )
        map?.setLatLngBoundsForCameraTarget(bounds)
    }

    fun changeZoomLevels(map: GoogleMap?, minLevel: Float, maxLevel: Float) {
        map?.setMinZoomPreference(minLevel)
        map?.setMaxZoomPreference(maxLevel)
    }

}