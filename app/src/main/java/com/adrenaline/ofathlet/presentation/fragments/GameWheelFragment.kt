package com.adrenaline.ofathlet.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.adrenaline.ofathlet.data.DataManager
import com.adrenaline.ofathlet.databinding.FragmentGameWheelBinding
import com.adrenaline.ofathlet.presentation.GameViewModel
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        viewModel.balance.onEach { newValue ->
            binding.totalValue.text = newValue.toString()
        }.launchIn(lifecycleScope)

        viewModel.win.onEach { newValue ->
            binding.winValue.text = newValue.toString()
        }.launchIn(lifecycleScope)

        viewModel.bet.onEach { newValue ->
            binding.betValue.text = newValue.toString()
        }.launchIn(lifecycleScope)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // fixing auto text feature for older Android APIs
            ViewUtility.apply {
                makeTextAutoSize(binding.totalTitle)
                makeTextAutoSize(binding.winTitle)
                makeTextAutoSize(binding.winValue)
                makeTextAutoSize(binding.totalValue)
                makeTextAutoSize(binding.betValue)
            }
        }

        return binding.root
    }

    private fun setClickListeners() {

        binding.apply {

            buttonRepeat.setOnClickListener {
                viewModel.spinWheel(binding.wheel, requireContext())
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