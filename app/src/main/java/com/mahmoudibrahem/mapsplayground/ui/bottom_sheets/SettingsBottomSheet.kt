package com.mahmoudibrahem.mapsplayground.ui.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.databinding.SettingsBottomSheetLayoutBinding
import com.mahmoudibrahem.mapsplayground.model.MapStyleItem
import com.mahmoudibrahem.mapsplayground.model.MapTypeItem
import com.mahmoudibrahem.mapsplayground.ui.adapters.MapStyleAdapter
import com.mahmoudibrahem.mapsplayground.ui.adapters.MapTypeAdapter

class SettingsBottomSheet : BottomSheetDialogFragment() {

    private var binding: SettingsBottomSheetLayoutBinding? = null
    private var mapTypeAdapter: MapTypeAdapter? = null
    private var mapStyleAdapter: MapStyleAdapter? = null
    private var mapTypeSelectedId = 1
    private var mapStyleSelectedId = 1
    private var isZoomControlEnabled = false
    private var isZoomGestureEnabled = true
    private var isScrollGestureEnabled = true
    private var isRotateGestureEnabled = true
    private var isCompassEnabled = true
    private var isToolbarEnabled = true

    var onZoomControlSwitch: ((Boolean) -> Unit)? = null
    var onZoomGestureSwitch: ((Boolean) -> Unit)? = null
    var onScrollGestureSwitch: ((Boolean) -> Unit)? = null
    var onCompassSwitch: ((Boolean) -> Unit)? = null
    var onToolbarSwitch: ((Boolean) -> Unit)? = null
    var onRotateGestureSwitch: ((Boolean) -> Unit)? = null
    var onMapTypeChanged: ((MapTypeItem) -> Unit)? = null
    var onMapStyleChanged: ((MapStyleItem) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsBottomSheetLayoutBinding.inflate(inflater, container, false)
        initView()
        initRecyclers()
        initClicks()
        return binding?.root
    }

    private fun initView() {
        binding?.zoomControlSwitch?.isChecked = isZoomControlEnabled
        binding?.zoomGesturesSwitch?.isChecked = isZoomGestureEnabled
        binding?.scrollGesturesSwitch?.isChecked = isScrollGestureEnabled
        binding?.rotateGesturesSwitch?.isChecked = isRotateGestureEnabled
        binding?.compassSwitch?.isChecked = isCompassEnabled
        binding?.toolbarSwitch?.isChecked = isToolbarEnabled
    }

    private fun initRecyclers() {
        mapTypeAdapter = MapTypeAdapter()
        mapTypeAdapter?.submitList(getMapTypeList().map { it.copy(isSelected = it.id == mapTypeSelectedId) })
        binding?.mapTypeRv?.adapter = mapTypeAdapter

        mapStyleAdapter = MapStyleAdapter()
        mapStyleAdapter?.submitList(getMapStyleList().map { it.copy(isSelected = it.id == mapStyleSelectedId) })
        binding?.mapStyleRv?.adapter = mapStyleAdapter
    }

    private fun initClicks() {
        binding?.zoomControlSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onZoomControlSwitch?.invoke(isChecked)
            isZoomControlEnabled = isChecked
        }

        binding?.zoomGesturesSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onZoomGestureSwitch?.invoke(isChecked)
            isScrollGestureEnabled = isChecked
        }

        binding?.scrollGesturesSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onScrollGestureSwitch?.invoke(isChecked)
            isScrollGestureEnabled = isChecked
        }

        binding?.rotateGesturesSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onRotateGestureSwitch?.invoke(isChecked)
            isRotateGestureEnabled = isChecked
        }

        binding?.compassSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onCompassSwitch?.invoke(isChecked)
            isCompassEnabled = isChecked
        }

        binding?.toolbarSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onToolbarSwitch?.invoke(isChecked)
            isToolbarEnabled = isChecked
        }


        mapTypeAdapter?.onSelectedItemChanged = { item ->
            onMapTypeChanged?.invoke(item)
            val newList =
                mapTypeAdapter?.currentList?.map { it.copy(isSelected = it.id == item.id) }
            mapTypeAdapter?.submitList(newList)
            mapTypeSelectedId = item.id
            this.dismiss()
        }

        mapStyleAdapter?.onSelectedItemChanged = { item ->
            onMapStyleChanged?.invoke(item)
            val newList =
                mapStyleAdapter?.currentList?.map { it.copy(isSelected = it.id == item.id) }
            mapStyleAdapter?.submitList(newList)
            mapStyleSelectedId = item.id
            this.dismiss()
        }
    }

    private fun getMapTypeList() = listOf(
        MapTypeItem(name = "Normal", id = 1, isSelected = true),
        MapTypeItem(name = "Satellite", id = 2),
        MapTypeItem(name = "Terrain", id = 3),
        MapTypeItem(name = "Hybrid", id = 4),
        MapTypeItem(name = "None", id = 5)
    )

    private fun getMapStyleList() = listOf(
        MapStyleItem(
            name = "Standard",
            id = 1,
            isSelected = true,
            stylePath = R.raw.map_standard_style
        ),
        MapStyleItem(name = "Silver", id = 2, stylePath = R.raw.map_silver_style),
        MapStyleItem(name = "Retro", id = 3, stylePath = R.raw.map_retro_style),
        MapStyleItem(name = "Dark", id = 4, stylePath = R.raw.map_dark_style),
        MapStyleItem(name = "Night", id = 5, stylePath = R.raw.map_night_style),
        MapStyleItem(name = "Aubergine", id = 6, stylePath = R.raw.map_aubergine_style)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mapTypeAdapter = null
        mapStyleAdapter = null
    }
}