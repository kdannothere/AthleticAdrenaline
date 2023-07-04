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
import com.adrenaline.ofathlet.databinding.FragmentWelcomeBinding
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

        binding.box1.setOnClickListener {

        }

        binding.buttonPlay.setOnClickListener {

            findNavController().navigate(R.id.action_WelcomeFragment_to_AuthFragment)
        }

        return binding.root
    }
}