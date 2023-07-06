package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentWelcomeBinding
import com.adrenaline.ofathlet.presentation.GameViewModel

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.buttonPlay.setOnClickListener {
            when(viewModel.isUserLoggedIn) {
                true -> findNavController().navigate(R.id.action_WelcomeFragment_to_MenuFragment)
                false -> findNavController().navigate(R.id.action_WelcomeFragment_to_AuthFragment)
            }
        }

        return binding.root
    }
}