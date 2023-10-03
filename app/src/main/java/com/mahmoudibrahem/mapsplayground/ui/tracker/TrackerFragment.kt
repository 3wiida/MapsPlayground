package com.mahmoudibrahem.mapsplayground.ui.tracker

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.databinding.FragmentTrackerBinding
import com.mahmoudibrahem.mapsplayground.service.TrackingService
import com.mahmoudibrahem.mapsplayground.util.Constants.TRACKING_SERVICE_ACTION_START
import com.mahmoudibrahem.mapsplayground.util.Constants.TRACKING_SERVICE_ACTION_STOP
import com.mahmoudibrahem.mapsplayground.util.tracker_util.TrackerUtil

class TrackerFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentTrackerBinding? = null
    private val viewModel: TrackerViewModel by viewModels()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var map: GoogleMap? = null
    private var steps = mutableListOf<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackerBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initClicks()
        if (TrackingService.isRunning) {
            binding?.countDownTv?.isVisible = false
            binding?.startBtn?.isVisible = false
            binding?.stopBtn?.isVisible = true
            binding?.resultSecion?.isVisible = true
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        TrackerUtil.initTrackMap(this.map)
        observeLocationUpdates()
    }


    private fun observeLocationUpdates() {
        TrackingService.steps.observe(viewLifecycleOwner) { steps ->
            steps?.let {
                if (it.isNotEmpty()) {
                    this.steps = it
                    TrackerUtil.drawTrackPolyLine(map = map, steps = it)
                }
            }
        }
    }

    private fun initClicks() {
        binding?.startBtn?.setOnClickListener {
            map?.clear()
            binding?.countDownTv?.isVisible = true

            viewModel.startCountDown(
                onTick = {
                    binding?.countDownTv?.text = it
                },
                onFinish = {
                    binding?.countDownTv?.isVisible = false
                    binding?.startBtn?.isVisible = false
                    binding?.stopBtn?.isVisible = true
                    binding?.resultSecion?.isVisible = true
                    sendActionToService(TRACKING_SERVICE_ACTION_START)
                }
            )
            if (viewModel.hasLocationPermission(requireContext())) {
                TrackerUtil.animateToDeviceLocation(
                    map = map,
                    locationProviderClient = fusedLocationClient!!
                )
            }

        }

        binding?.stopBtn?.setOnClickListener {
            binding?.startBtn?.isVisible = true
            binding?.stopBtn?.isVisible = false
            sendActionToService(TRACKING_SERVICE_ACTION_STOP)
            TrackerUtil.animateCameraToBounds(
                map = map,
                steps = steps
            )
        }
    }

    private fun sendActionToService(action: String) {
        Intent(
            requireContext(),
            TrackingService::class.java
        ).apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        map = null
    }

}