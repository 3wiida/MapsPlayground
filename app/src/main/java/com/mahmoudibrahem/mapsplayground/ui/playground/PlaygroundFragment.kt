package com.mahmoudibrahem.mapsplayground.ui.playground

import android.graphics.drawable.BitmapDrawable
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.databinding.FragmentPlaygroundBinding
import com.mahmoudibrahem.mapsplayground.ui.playground.bottom_sheets.CameraAndViewPortBottomSheet
import com.mahmoudibrahem.mapsplayground.ui.playground.bottom_sheets.MarkerBottomSheet
import com.mahmoudibrahem.mapsplayground.ui.playground.bottom_sheets.SettingsBottomSheet
import com.mahmoudibrahem.mapsplayground.ui.playground.playground_util.MarkerPlayground

class PlaygroundFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentPlaygroundBinding? = null
    private val settingsBottomSheet: SettingsBottomSheet by lazy { SettingsBottomSheet() }
    private val cameraBottomSheet: CameraAndViewPortBottomSheet by lazy { CameraAndViewPortBottomSheet() }
    private val markerBottomSheet: MarkerBottomSheet by lazy { MarkerBottomSheet() }
    private val markerList: MutableList<Marker> by lazy { mutableListOf() }
    private var map: GoogleMap? = null
    private var cameraTilt = 0f
    private val texasLatLng = LatLng(31.511301916399816, -99.19507048791485)
    private val nycLatLng = LatLng(40.71783982809633, -74.01956722190161)
    private val floridaLatLng = LatLng(27.72791982055598, -81.7161000506038)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.playgroundMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initSettingsClicks()
        initCameraViewPortClicks()
        initMarkerClicks()
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
    }

    private fun initSettingsClicks() {
        binding?.settings?.setOnClickListener {
            settingsBottomSheet.show(requireActivity().supportFragmentManager, null)
        }
        settingsBottomSheet.onZoomControlSwitch = { isChecked ->
            map?.uiSettings?.isZoomControlsEnabled = isChecked
        }
        settingsBottomSheet.onZoomGestureSwitch = { isChecked ->
            map?.uiSettings?.isZoomGesturesEnabled = isChecked
        }
        settingsBottomSheet.onScrollGestureSwitch = { isChecked ->
            map?.uiSettings?.isScrollGesturesEnabled = isChecked
        }
        settingsBottomSheet.onRotateGestureSwitch = { isChecked ->
            map?.uiSettings?.isRotateGesturesEnabled = isChecked
        }
        settingsBottomSheet.onMapTypeChanged = { mapTypeItem ->
            map?.mapType = when (mapTypeItem.id) {
                1 -> GoogleMap.MAP_TYPE_NORMAL
                2 -> GoogleMap.MAP_TYPE_SATELLITE
                3 -> GoogleMap.MAP_TYPE_TERRAIN
                4 -> GoogleMap.MAP_TYPE_HYBRID
                5 -> GoogleMap.MAP_TYPE_NONE
                else -> GoogleMap.MAP_TYPE_NORMAL
            }
        }
        settingsBottomSheet.onMapStyleChanged = { mapStyleItem ->
            map?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    mapStyleItem.stylePath
                )
            )
        }
    }

    private fun initCameraViewPortClicks() {
        binding?.camera?.setOnClickListener {
            cameraBottomSheet.show(requireActivity().supportFragmentManager, null)
        }
        cameraBottomSheet.onShowBuildingSwitch = { isChecked ->
            cameraTilt = if (isChecked) 45f else 0f
            map?.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition
                        .builder()
                        .apply {
                            target(map!!.cameraPosition.target)
                            tilt(cameraTilt)
                            bearing(100f)
                            zoom(18f)
                        }
                        .build()
                )
            )
        }
        cameraBottomSheet.onMoveCameraBtnClicked = {
            map?.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition
                        .builder()
                        .apply {
                            target(
                                LatLng(31.13346546696312, -99.33660633756232)
                            )
                            tilt(cameraTilt)
                            bearing(100f)
                            zoom(18f)
                        }
                        .build()
                )
            )
        }
        cameraBottomSheet.onAnimateCameraBtnClicked = {
            map?.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition
                        .builder()
                        .apply {
                            target(LatLng(30.035093642701582, 31.235114084065927))
                            tilt(cameraTilt)
                            zoom(12f)
                        }
                        .build()
                )
            )
        }
        cameraBottomSheet.onMoveCameraWithBoundariesBtnClicked = {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds(
                        LatLng(40.507792278844086, -74.26727414428862),
                        LatLng(40.912648868040556, -73.60921191097013)
                    ),
                    100
                )
            )
        }
        cameraBottomSheet.onMoveCameraWithRestrictBtnClicked = {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds(
                        LatLng(25.210546407531524, -87.64871718091898),
                        LatLng(30.850966090042842, -80.74930318492281)
                    ),
                    100
                )
            )
            map?.setLatLngBoundsForCameraTarget(
                LatLngBounds(
                    LatLng(25.210546407531524, -87.64871718091898),
                    LatLng(30.850966090042842, -80.74930318492281)
                )
            )
        }
        cameraBottomSheet.onSlideZoomSlider = { values ->
            map?.setMinZoomPreference(values[0])
            map?.setMaxZoomPreference(values[1])
        }
    }

    private fun initMarkerClicks() {
        binding?.markerSettings?.setOnClickListener {
            markerBottomSheet.show(requireActivity().supportFragmentManager, null)
        }

        markerBottomSheet.onAddDefMarkerBtnClicked = {
            val marker = MarkerPlayground.addDefMarker(
                map = map,
                position = texasLatLng
            )
            marker?.let { markerList.add(it) }

        }

        markerBottomSheet.onAddBitmapMarkerClicked = {
            val marker = MarkerPlayground.addBitmapMarker(
                map = map,
                position = nycLatLng,
                resourceId = R.drawable.car_marker
            )
            marker?.let { markerList.add(it) }
        }

        markerBottomSheet.onAddVectorMarkerBtnClicked = {
            val marker = MarkerPlayground.addVectorMarker(
                context = requireContext(),
                map = map,
                position = floridaLatLng,
                resourceId = R.drawable.scooter_marker
            )
            marker?.let { markerList.add(it) }
        }

        markerBottomSheet.onRemoveMarkersBtnClicked = {
            MarkerPlayground.removeAllMarkers(markers = markerList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        map = null
    }
}