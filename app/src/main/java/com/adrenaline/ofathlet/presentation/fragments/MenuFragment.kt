package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentMenuBinding
import com.adrenaline.ofathlet.databinding.FragmentWelcomeBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.buttonGameWheel.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_GameWheelFragment)
        }

        binding.buttonGameSlot.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_GameSlotFragment)
        }

        binding.buttonGameBonus.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_GameBonusFragment)
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_SettingsFragment)
        }

        return binding.root
    }
}