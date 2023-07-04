package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.data.DataManager
import com.adrenaline.ofathlet.databinding.FragmentGameWheelBinding
import com.adrenaline.ofathlet.databinding.FragmentWelcomeBinding
import com.adrenaline.ofathlet.presentation.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameWheelFragment : Fragment() {

    private var _binding: FragmentGameWheelBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameWheelBinding.inflate(inflater, container, false)

        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {

        binding.apply {

            buttonRepeat.setOnClickListener {

            }

            buttonIncreaseBet.setOnClickListener {
                viewModel.increaseBet(requireContext())
                lifecycleScope.launch(Dispatchers.IO) {
                    DataManager.saveBet(
                        requireContext(),
                        viewModel.bet.value
                    )
                }
            }

            buttonDecreaseBet.setOnClickListener {
                viewModel.decreaseBet(requireContext())
                lifecycleScope.launch(Dispatchers.IO) {
                    DataManager.saveBet(
                        requireContext(),
                        viewModel.bet.value
                    )
                }
            }
        }
    }
}