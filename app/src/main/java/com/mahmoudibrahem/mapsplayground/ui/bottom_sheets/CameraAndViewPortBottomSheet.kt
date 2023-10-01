package com.mahmoudibrahem.mapsplayground.ui.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mahmoudibrahem.mapsplayground.databinding.CameraBottomSheetLayoutBinding

class CameraAndViewPortBottomSheet : BottomSheetDialogFragment() {

    private var binding: CameraBottomSheetLayoutBinding? = null

    var onShowBuildingSwitch: ((Boolean) -> Unit)? = null
    var onMoveCameraBtnClicked: (() -> Unit)? = null
    var onAnimateCameraBtnClicked: (() -> Unit)? = null
    var onMoveCameraWithBoundariesBtnClicked: (() -> Unit)? = null
    var onMoveCameraWithRestrictBtnClicked: (() -> Unit)? = null
    var onSlideZoomSlider: ((List<Float>) -> Unit)? = null
    private var isShowBuildingsEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CameraBottomSheetLayoutBinding.inflate(inflater, container, false)
        binding?.showBuildingSwitch?.isChecked = isShowBuildingsEnabled
        initClicks()
        return binding?.root
    }

    private fun initClicks() {
        binding?.showBuildingSwitch?.setOnCheckedChangeListener { _, isChecked ->
            onShowBuildingSwitch?.invoke(isChecked)
            isShowBuildingsEnabled = isChecked
        }
        binding?.moveCameraBtn?.setOnClickListener {
            onMoveCameraBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.animateCameraBtn?.setOnClickListener {
            onAnimateCameraBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.moveCameraWithBoundsAndRestrictBtn?.setOnClickListener {
            onMoveCameraWithRestrictBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.moveCameraWithBoundsBtn?.setOnClickListener {
            onMoveCameraWithBoundariesBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.zoomSlider?.addOnChangeListener { slider, _, _ ->
            onSlideZoomSlider?.invoke(slider.values)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}