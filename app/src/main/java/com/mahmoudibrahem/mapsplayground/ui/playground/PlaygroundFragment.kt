package com.mahmoudibrahem.mapsplayground.ui.playground

import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.RoundCap
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.databinding.FragmentPlaygroundBinding
import com.mahmoudibrahem.mapsplayground.model.LocationData
import com.mahmoudibrahem.mapsplayground.ui.adapters.CustomInfoWindowAdapter
import com.mahmoudibrahem.mapsplayground.ui.bottom_sheets.CameraAndViewPortBottomSheet
import com.mahmoudibrahem.mapsplayground.ui.bottom_sheets.MarkerBottomSheet
import com.mahmoudibrahem.mapsplayground.ui.bottom_sheets.SettingsBottomSheet
import com.mahmoudibrahem.mapsplayground.ui.bottom_sheets.ShapesBottomSheet
import com.mahmoudibrahem.mapsplayground.util.playground_util.CameraPlayground
import com.mahmoudibrahem.mapsplayground.util.playground_util.MarkerPlayground
import com.mahmoudibrahem.mapsplayground.util.cairoLatLng
import com.mahmoudibrahem.mapsplayground.util.floridaBounds
import com.mahmoudibrahem.mapsplayground.util.floridaLatLng
import com.mahmoudibrahem.mapsplayground.util.nycBounds
import com.mahmoudibrahem.mapsplayground.util.nycLatLng
import com.mahmoudibrahem.mapsplayground.util.playground_util.ShapesUtil
import com.mahmoudibrahem.mapsplayground.util.texasLatLng
import com.mahmoudibrahem.mapsplayground.util.usHole
import com.mahmoudibrahem.mapsplayground.util.usPolygon
import java.util.Locale

class PlaygroundFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private var binding: FragmentPlaygroundBinding? = null
    private lateinit var adapter: CustomInfoWindowAdapter
    private val settingsBottomSheet: SettingsBottomSheet by lazy { SettingsBottomSheet() }
    private val cameraBottomSheet: CameraAndViewPortBottomSheet by lazy { CameraAndViewPortBottomSheet() }
    private val shapesBottomSheet: ShapesBottomSheet by lazy { ShapesBottomSheet() }
    private val markerBottomSheet: MarkerBottomSheet by lazy { MarkerBottomSheet() }
    private val markerList: MutableList<Marker> by lazy { mutableListOf() }
    private var map: GoogleMap? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
        adapter = CustomInfoWindowAdapter(requireActivity())
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.playgroundMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initSettingsClicks()
        initCameraViewPortClicks()
        initMarkerClicks()
        initShapesClicks()
        binding?.clearBtn?.setOnClickListener {
            map?.clear()
            markerList.clear()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        map.setOnMarkerClickListener(this)
        map.setOnMarkerDragListener(this)
        this.map = map
    }

    private fun initSettingsClicks() {
        binding?.settings?.setOnClickListener {
            if (settingsBottomSheet.isAdded) return@setOnClickListener
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
        settingsBottomSheet.onCompassSwitch = { isChecked ->
            map?.uiSettings?.isCompassEnabled = isChecked
        }
        settingsBottomSheet.onToolbarSwitch = { isChecked ->
            map?.uiSettings?.isMapToolbarEnabled = isChecked
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
            if (cameraBottomSheet.isAdded) return@setOnClickListener
            cameraBottomSheet.show(requireActivity().supportFragmentManager, null)
        }
        cameraBottomSheet.onShowBuildingSwitch = { isChecked ->
            CameraPlayground.showBuilding(map = map, isEnabled = isChecked)
        }
        cameraBottomSheet.onMoveCameraBtnClicked = {
            CameraPlayground.moveCamera(map = map, position = texasLatLng)
        }
        cameraBottomSheet.onAnimateCameraBtnClicked = {
            CameraPlayground.animateCamera(map = map, position = cairoLatLng)
        }
        cameraBottomSheet.onMoveCameraWithBoundariesBtnClicked = {
            CameraPlayground.moveCameraToLatLngBounds(map = map, bounds = nycBounds, padding = 100)
        }
        cameraBottomSheet.onMoveCameraWithRestrictBtnClicked = {
            CameraPlayground.moveCameraToLatLngBoundsWithRestrict(
                map = map,
                bounds = floridaBounds,
                100
            )
        }
        cameraBottomSheet.onSlideZoomSlider = { values ->
            CameraPlayground.changeZoomLevels(
                map = map,
                minLevel = values.first(),
                maxLevel = values.last()
            )
        }
    }

    private fun initShapesClicks() {
        binding?.polygon?.setOnClickListener {
            if (shapesBottomSheet.isAdded) return@setOnClickListener
            shapesBottomSheet.show(requireActivity().supportFragmentManager, null)
        }
        shapesBottomSheet.onDrawPolylineBtnClicked = {
            ShapesUtil.drawLine(
                startPosition = nycLatLng,
                endPosition = floridaLatLng,
                map = map,
                color = R.color.app_button_color,
                width = 5f,
                pattern = listOf(Dash(10f), Gap(10f), Dot()),
                strokeCap = RoundCap()
            )
        }
        shapesBottomSheet.onDrawCircleBtnClicked = {
            ShapesUtil.drawCircle(
                center = texasLatLng,
                radius = 5000.0,
                map = map,
                fillColor = R.color.black,
                strokeColor = R.color.app_button_color,
                strokePattern = listOf(Dash(10f), Gap(10f), Dot()),
            )
        }
        shapesBottomSheet.onDrawPolygonBtnClicked = {
            ShapesUtil.drawPolygon(
                points = listOf(
                    texasLatLng,
                    nycLatLng,
                    floridaLatLng
                ),
                map = map
            )
        }
        shapesBottomSheet.onDrawPolygonWithHoleBtnClicked = {
            ShapesUtil.drawPolygonWithHole(
                polygonPoints = usPolygon,
                holePoints = usHole,
                fillColor = R.color.app_button_color,
                map = map
            )
        }
    }

    private fun initMarkerClicks() {
        binding?.markerSettings?.setOnClickListener {
            if (markerBottomSheet.isAdded) return@setOnClickListener
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

        markerBottomSheet.onEnableMarkerDragSwitch = { isEnabled ->
            MarkerPlayground.enableMarkerDrag(isEnable = isEnabled, markers = markerList)
        }

        markerBottomSheet.onEnableCustomInfoWindowSwitch = { isEnabled ->
            if (isEnabled) {
                map?.setInfoWindowAdapter(adapter)
            } else {
                map?.setInfoWindowAdapter(null)
            }
        }
    }


    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragStart(p0: Marker) {}

    override fun onMarkerDragEnd(marker: Marker) {
        Toast.makeText(requireContext(), marker.position.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.showInfoWindow()
        MarkerPlayground.getLocationInfo(marker.position, requireContext(), Locale.US) { data ->
            val info = if (data == null) {
                LocationData(
                    countryName = null,
                    countryCode = null,
                    postalCode = null,
                    adminArea = null,
                    lat = marker.position.latitude.toString(),
                    lng = marker.position.longitude.toString()
                )
            } else {
                LocationData(
                    countryName = data.countryName,
                    countryCode = data.countryCode,
                    postalCode = data.postalCode,
                    adminArea = data.adminArea,
                    lat = data.latitude.toString(),
                    lng = data.longitude.toString()
                )
            }
            adapter.setData(info)
        }
        marker.showInfoWindow()
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        map = null
    }
}