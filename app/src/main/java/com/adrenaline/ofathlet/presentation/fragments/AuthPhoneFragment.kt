package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentAuthPhoneBinding
import com.adrenaline.ofathlet.presentation.GameViewModel

class AuthPhoneFragment : Fragment() {

    private var _binding: FragmentAuthPhoneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthPhoneBinding.inflate(inflater, container, false)

        binding.buttonPlay.setOnClickListener {
            findNavController().navigate(R.id.action_AuthNumberFragment_to_MenuFragment)
        }

        return binding.root
    }
}