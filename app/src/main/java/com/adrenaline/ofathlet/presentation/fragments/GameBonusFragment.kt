package com.adrenaline.ofathlet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.data.DataManager
import com.adrenaline.ofathlet.databinding.FragmentGameBonusBinding
import com.adrenaline.ofathlet.databinding.FragmentWelcomeBinding
import com.adrenaline.ofathlet.presentation.GameViewModel
import com.adrenaline.ofathlet.presentation.slot.SlotAdapter
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameBonusFragment : Fragment() {

    private var _binding: FragmentGameBonusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var leftSlotAdapter: SlotAdapter
    private lateinit var centerSlotAdapter: SlotAdapter
    private lateinit var rightSlotAdapter: SlotAdapter
    private lateinit var leftManager: LinearLayoutManager
    private lateinit var centerManager: LinearLayoutManager
    private lateinit var rightManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameBonusBinding.inflate(inflater, container, false)

        ViewUtility.updateFieldHeight(
            binding.slotField,
            viewModel.isHeightCorrect
        ) { viewModel.setIsHeightCorrect(true) }

        viewModel.apply {
            if (leftSlots.isEmpty()) generateSlots()
            binding.totalValue.text = balance.value.toString()
            binding.betValue.text = bet.value.toString()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Default) {
            while (!viewModel.isHeightCorrect.value) {
                delay(100L)
            }
            launch(Dispatchers.Main) {
                setRecyclerViews()
                setClickListeners()
            }
        }
        viewModel.balance.onEach { newValue ->
            binding.totalValue.text = newValue.toString()
        }.launchIn(lifecycleScope)

        viewModel.win.onEach { newValue ->
            binding.winValue.text = newValue.toString()
        }.launchIn(lifecycleScope)

        viewModel.bet.onEach { newValue ->
            binding.betValue.text = newValue.toString()
        }.launchIn(lifecycleScope)
    }

    private fun setRecyclerViews() {

        leftSlotAdapter = SlotAdapter(viewModel.leftSlots)
        leftManager = LinearLayoutManager(requireContext())
        centerSlotAdapter = SlotAdapter(viewModel.centerSlots)
        centerManager = LinearLayoutManager(requireContext())
        rightSlotAdapter = SlotAdapter(viewModel.rightSlots)
        rightManager = LinearLayoutManager(requireContext())
        binding.leftRecyclerView.apply {
            adapter = leftSlotAdapter
            layoutManager = leftManager
            setOnTouchListener { _, _ -> true }
        }
        binding.centerRecyclerView.apply {
            adapter = centerSlotAdapter
            layoutManager = centerManager
            setOnTouchListener { _, _ -> true }
        }
        binding.rightRecyclerView.apply {
            adapter = rightSlotAdapter
            layoutManager = rightManager
            setOnTouchListener { _, _ -> true }
        }
        setCorrectRecyclerViewHeight(
            listOf(
                binding.leftRecyclerView,
                binding.centerRecyclerView,
                binding.rightRecyclerView
            )
        )
    }

    private fun setCorrectRecyclerViewHeight(recyclers: List<RecyclerView?>) {
        val params = listOf(
            recyclers[0]?.layoutParams,
            recyclers[1]?.layoutParams,
            recyclers[2]?.layoutParams
        )
        repeat(3) { index ->
            params[index]?.height = (ViewUtility.getFieldHeight() * 0.8).toInt()
            recyclers[index]?.layoutParams = params[index]
        }
    }

    private fun setClickListeners() {

        binding.apply {

            buttonRepeat.setOnClickListener {
                viewModel.spin(
                    listOf(
                        leftRecyclerView,
                        centerRecyclerView,
                        rightRecyclerView
                    ),
                    listOf(
                        leftManager,
                        centerManager,
                        rightManager
                    ),
                    requireContext()
                )
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

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.setIsHeightCorrect(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}