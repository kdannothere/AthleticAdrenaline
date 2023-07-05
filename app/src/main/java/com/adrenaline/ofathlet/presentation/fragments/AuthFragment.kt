package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentAuthBinding
import com.adrenaline.ofathlet.presentation.GameViewModel

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
            viewModel.apply {
                isLoggingByPhone = true
                isLoggingByEmail = false
                isUserAnonymous = false
            }
            updateBoxes()
        }

        binding.box2.setOnClickListener {
            viewModel.apply {
                isLoggingByPhone = false
                isLoggingByEmail = true
                isUserAnonymous = false
            }
            updateBoxes()
        }

        binding.box3.setOnClickListener {
            viewModel.apply {
                isLoggingByPhone = false
                isLoggingByEmail = false
                isUserAnonymous = true
            }
            updateBoxes()
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.apply {
                when {
                    isLoggingByPhone -> findNavController().navigate(R.id.action_AuthFragment_to_AuthPhoneFragment)
                    isLoggingByEmail -> findNavController().navigate(R.id.action_AuthFragment_to_AuthEmailFragment)
                    isUserAnonymous -> findNavController().navigate(R.id.action_AuthFragment_to_MenuFragment)
                }
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
}