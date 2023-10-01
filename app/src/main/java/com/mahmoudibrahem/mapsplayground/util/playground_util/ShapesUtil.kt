package com.mahmoudibrahem.mapsplayground.util.playground_util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Cap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.mahmoudibrahem.mapsplayground.util.nycLatLng
import com.mahmoudibrahem.mapsplayground.util.texasLatLng

object ShapesUtil {
    fun drawLine(
        startPosition: LatLng,
        endPosition: LatLng,
        map: GoogleMap?,
        color: Int,
        width: Float,
        pattern: List<PatternItem>,
        strokeCap: Cap
    ) {
        map?.addPolyline(
            PolylineOptions().apply {
                add(startPosition)
                add(endPosition)
                color(color)
                width(width)
                pattern(pattern)
                startCap(strokeCap)
                geodesic(true)
                jointType(JointType.ROUND)
            }
        )
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(nycLatLng, 1f))
    }

    fun drawCircle(
        center: LatLng,
        radius: Double,
        map: GoogleMap?,
        fillColor: Int,
        strokeColor: Int,
        strokePattern: List<PatternItem>
    ) {
        map?.addCircle(
            CircleOptions().apply {
                center(center)
                radius(radius)
                fillColor(fillColor)
                strokeColor(strokeColor)
                strokePattern(strokePattern)
            }
        )
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(texasLatLng, 12f))
    }

    fun drawPolygon(points: List<LatLng>, map: GoogleMap?) {
        map?.addPolygon(
            PolygonOptions().addAll(points)
        )
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(nycLatLng, 1f))
    }

    fun drawPolygonWithHole(
        polygonPoints: List<LatLng>,
        holePoints: List<LatLng>,
        map: GoogleMap?,
        fillColor: Int
    ) {
        map?.addPolygon(
            PolygonOptions().apply {
                addAll(polygonPoints)
                addHole(holePoints)
                fillColor(fillColor)
            }
        )
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(texasLatLng, 1f))
    }

}