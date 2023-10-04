package com.mahmoudibrahem.mapsplayground.ui.tracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch

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
        requireActivity().intent.action = null
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initClicks()
        observeElapsedTime()
        if (TrackingService.isRunning) {
            binding?.countDownTv?.isVisible = false
            binding?.startBtn?.isVisible = false
            binding?.stopBtn?.isVisible = true
            binding?.resultSecion?.isVisible = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        map.isMyLocationEnabled = true
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
                    if (it.isNotEmpty()) {
                        val distance = TrackerUtil.calculateDistance(it.first(), it.last())
                        binding?.distanceTv?.text = getString(R.string.km, distance)
                    }
                }
            }
        }
    }

    private fun observeElapsedTime() {
        lifecycleScope.launch {
            TrackingService.elapsedTime.observe(viewLifecycleOwner) {
                binding?.elapsedTimeTv?.text = getString(R.string.min, it)
            }
        }
    }

    private fun initClicks() {
        binding?.startBtn?.setOnClickListener {
            binding?.startBtn?.text = getString(R.string.getting_ready)
            if (viewModel.hasLocationPermission(requireContext())) {
                TrackerUtil.animateToDeviceLocation(
                    map = map,
                    locationProviderClient = fusedLocationClient!!,
                    onSuccess = {
                        map?.clear()
                        binding?.countDownTv?.isVisible = true
                        binding?.distanceTv?.text = getString(R.string._0_00_km)
                        startCountDown()
                    }
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

    private fun startCountDown() {
        viewModel.startCountDown(
            onTick = {
                binding?.countDownTv?.text = it
            },
            onFinish = {
                binding?.countDownTv?.isVisible = false
                binding?.startBtn?.isVisible = false
                binding?.stopBtn?.isVisible = true
                binding?.resultSecion?.isVisible = true
                binding?.startBtn?.text = getString(R.string.start)
                sendActionToService(TRACKING_SERVICE_ACTION_START)
            }
        )
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