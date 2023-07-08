package com.adrenaline.ofathlet.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.BestActivity
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentAuthBinding
import com.adrenaline.ofathlet.presentation.GameViewModel
import com.adrenaline.ofathlet.presentation.utilities.MusicUtility
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        updateBoxes()

        binding.box1.setOnClickListener {
            playClickSound()
            viewModel.apply {
                isLoggingByPhone = true
                isLoggingByEmail = false
                isUserAnonymous = false
            }
            updateBoxes()
        }

        binding.textPhone.setOnClickListener {
            binding.box1.callOnClick()
        }

        binding.box2.setOnClickListener {
            playClickSound()
            viewModel.apply {
                isLoggingByPhone = false
                isLoggingByEmail = true
                isUserAnonymous = false
            }
            updateBoxes()
        }

        binding.textEmail.setOnClickListener {
            binding.box2.callOnClick()
        }

        binding.box3.setOnClickListener {
            playClickSound()
            viewModel.apply {
                isLoggingByPhone = false
                isLoggingByEmail = false
                isUserAnonymous = true
            }
            updateBoxes()
        }

        binding.textAnonymous.setOnClickListener {
            binding.box3.callOnClick()
        }

        binding.buttonPlay.setOnClickListener {
            playClickSound()
            viewModel.apply {
                when {
                    isLoggingByPhone -> findNavController().navigate(R.id.action_AuthFragment_to_AuthPhoneFragment)
                    isLoggingByEmail -> findNavController().navigate(R.id.action_AuthFragment_to_AuthEmailFragment)
                    isUserAnonymous -> findNavController().navigate(R.id.action_AuthFragment_to_MenuFragment)
                }
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // fixing auto text feature for older android devices
            ViewUtility.apply {
                makeTextAutoSize(binding.textSignUp)
                makeTextAutoSize(binding.titlePlay)
                makeTextAutoSize(binding.textPhone)
                makeTextAutoSize(binding.textEmail)
                makeTextAutoSize(binding.textAnonymous)
            }
        }

        return binding.root
    }

    private fun updateBoxes() {
        viewModel.apply {
            when {
                isLoggingByPhone -> {
                    binding.box1.setImageResource(R.drawable.box_checked)
                    binding.box2.setImageResource(R.drawable.box_not_checked)
                    binding.box3.setImageResource(R.drawable.box_not_checked)
                }
                isLoggingByEmail -> {
                    binding.box1.setImageResource(R.drawable.box_not_checked)
                    binding.box2.setImageResource(R.drawable.box_checked)
                    binding.box3.setImageResource(R.drawable.box_not_checked)
                }
                isUserAnonymous -> {
                    binding.box1.setImageResource(R.drawable.box_not_checked)
                    binding.box2.setImageResource(R.drawable.box_not_checked)
                    binding.box3.setImageResource(R.drawable.box_checked)
                }
            }
        }
    }

    private fun playClickSound() {
        MusicUtility.playSound(
            mediaPlayer = (activity as BestActivity).soundPlayer,
            MusicUtility.soundClickResId,
            requireContext(),
            viewModel.viewModelScope,
            viewModel.isSoundOn,
            viewModel.isVibrationOn
        )
    }
}