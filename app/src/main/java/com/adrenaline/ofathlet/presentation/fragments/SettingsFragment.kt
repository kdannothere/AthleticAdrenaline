package com.adrenaline.ofathlet.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.databinding.FragmentSettingsBinding
import com.adrenaline.ofathlet.presentation.GameViewModel
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.buttonMusic.setOnClickListener {

        }

        binding.buttonSound.setOnClickListener {

        }

        binding.switchVibration.setOnClickListener {

        }

        binding.textButtonResetScore.setOnClickListener {
            viewModel.resetScore(requireContext())
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // fixing auto text feature for older Android APIs
            ViewUtility.apply {
                makeTextAutoSize(binding.textVibration)
                makeTextAutoSize(binding.textButtonResetScore)
            }
        }

        return binding.root
    }
}