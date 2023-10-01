package com.mahmoudibrahem.mapsplayground.ui.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.model.LocationData

class CustomInfoWindowAdapter(private val context: Context) :
    InfoWindowAdapter {

    private var data: LocationData? = null

    private val view =
        (context as Activity).layoutInflater.inflate(R.layout.custom_info_window_layout, null)

    override fun getInfoContents(p0: Marker): View? {
        data?.let { setUpView(it) }
        return view
    }

    override fun getInfoWindow(p0: Marker): View? {
        data?.let { setUpView(it) }
        return view
    }

    fun setData(data: LocationData) {
        this.data = data
        setUpView(data)
    }

    private fun setUpView(data: LocationData) {
        view.findViewById<TextView>(R.id.countryName).text =
            context.getString(R.string.country_name, data.countryName)
        view.findViewById<TextView>(R.id.countryCode).text =
            context.getString(R.string.country_code, data.countryCode)
        view.findViewById<TextView>(R.id.postalCode).text =
            context.getString(R.string.postal_code, data.postalCode)
        view.findViewById<TextView>(R.id.adminArea).text =
            context.getString(R.string.admin_area, data.adminArea)
        view.findViewById<TextView>(R.id.lat).text = context.getString(R.string.latitude, data.lat)
        view.findViewById<TextView>(R.id.lng).text = context.getString(R.string.longitude, data.lng)
    }
}