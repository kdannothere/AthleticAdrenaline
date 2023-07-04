package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentAuthEmailBinding
import com.adrenaline.ofathlet.databinding.FragmentWelcomeBinding
import com.adrenaline.ofathlet.presentation.GameViewModel

class AuthEmailFragment : Fragment() {

    private var _binding: FragmentAuthEmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthEmailBinding.inflate(inflater, container, false)

        binding.buttonPlay.setOnClickListener {
            findNavController().navigate(R.id.action_AuthEmailFragment_to_MenuFragment)
        }

        return binding.root
    }
}