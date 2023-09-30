package com.mahmoudibrahem.mapsplayground.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.databinding.HomeFragmentLayoutBinding
import com.mahmoudibrahem.mapsplayground.util.Enums

class HomeFragment : Fragment() {

    private var binding: HomeFragmentLayoutBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
    }

    private fun initClicks() {
        binding?.playground?.setOnClickListener {
            if (viewModel.hasLocationPermission(requireContext())) {
                findNavController().navigate(R.id.action_homeFragment_to_playgroundFragment)
            } else {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPermissionFragment(Enums.HomeTo.PLAYGROUND))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}