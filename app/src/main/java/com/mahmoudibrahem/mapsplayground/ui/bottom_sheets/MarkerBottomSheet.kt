package com.mahmoudibrahem.mapsplayground.ui.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mahmoudibrahem.mapsplayground.databinding.MarkerBottomSheetLayoutBinding

class MarkerBottomSheet : BottomSheetDialogFragment() {

    private var binding: MarkerBottomSheetLayoutBinding? = null

    var onAddDefMarkerBtnClicked: (() -> Unit)? = null
    var onAddVectorMarkerBtnClicked: (() -> Unit)? = null
    var onAddBitmapMarkerClicked: (() -> Unit)? = null
    var onRemoveMarkersBtnClicked: (() -> Unit)? = null
    var onEnableMarkerDragSwitch: ((Boolean) -> Unit)? = null
    var onEnableCustomInfoWindowSwitch: ((Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MarkerBottomSheetLayoutBinding.inflate(inflater, container, false)
        initClicks()
        return binding?.root
    }

    private fun initClicks() {
        binding?.addDefMarkerBtn?.setOnClickListener {
            onAddDefMarkerBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.addVectorMarkerBtn?.setOnClickListener {
            onAddVectorMarkerBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.addBitmapMarkerBtn?.setOnClickListener {
            onAddBitmapMarkerClicked?.invoke()
            this.dismiss()
        }
        binding?.removeMarkersBtn?.setOnClickListener {
            onRemoveMarkersBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.enableMarkerDragSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onEnableMarkerDragSwitch?.invoke(isChecked)
        }
        binding?.enableCustomInfoSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onEnableCustomInfoWindowSwitch?.invoke(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}