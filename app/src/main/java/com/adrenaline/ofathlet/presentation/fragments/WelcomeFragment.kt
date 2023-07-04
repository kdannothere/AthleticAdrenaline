package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.buttonPlay.setOnClickListener {
            findNavController().navigate(R.id.action_WelcomeFragment_to_AuthFragment)
        }

        return binding.root
    }
}