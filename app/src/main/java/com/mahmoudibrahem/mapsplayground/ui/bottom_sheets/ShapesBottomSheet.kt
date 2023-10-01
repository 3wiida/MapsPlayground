package com.mahmoudibrahem.mapsplayground.ui.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mahmoudibrahem.mapsplayground.databinding.ShapesBottomSheetLayoutBinding

class ShapesBottomSheet : BottomSheetDialogFragment() {

    private var binding: ShapesBottomSheetLayoutBinding? = null
    var onDrawPolylineBtnClicked: (() -> Unit)? = null
    var onDrawCircleBtnClicked: (() -> Unit)? = null
    var onDrawPolygonBtnClicked: (() -> Unit)? = null
    var onDrawPolygonWithHoleBtnClicked: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShapesBottomSheetLayoutBinding.inflate(inflater, container, false)
        initClicks()
        return binding?.root
    }

    private fun initClicks() {
        binding?.drawPolylineBtn?.setOnClickListener {
            onDrawPolylineBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.drawCircleBtn?.setOnClickListener {
            onDrawCircleBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.drawPolygonBtn?.setOnClickListener {
            onDrawPolygonBtnClicked?.invoke()
            this.dismiss()
        }
        binding?.drawPolygonWithHoleBtn?.setOnClickListener {
            onDrawPolygonWithHoleBtnClicked?.invoke()
            this.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}