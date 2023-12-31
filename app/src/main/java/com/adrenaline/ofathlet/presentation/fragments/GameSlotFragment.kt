package com.adrenaline.ofathlet.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adrenaline.ofathlet.BestActivity
import com.adrenaline.ofathlet.presentation.GameViewModel
import com.adrenaline.ofathlet.data.DataManager
import com.adrenaline.ofathlet.databinding.FragmentGameSlotBinding
import com.adrenaline.ofathlet.presentation.slot.SlotAdapter
import com.adrenaline.ofathlet.presentation.utilities.MusicUtility
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameSlotFragment : Fragment() {

    private var _binding: FragmentGameSlotBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var leftSlotAdapter: SlotAdapter
    private lateinit var centerSlotAdapter: SlotAdapter
    private lateinit var rightSlotAdapter: SlotAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameSlotBinding.inflate(inflater, container, false)

        ViewUtility.updateFieldHeight(
            binding.slotField,
            viewModel.isHeightCorrect
        ) { viewModel.setIsHeightCorrect(true) }

        viewModel.apply {
            if (leftSlots.isEmpty()) generateSlots()
            binding.totalValue.text = balance.value.toString()
            binding.betValue.text = bet.value.toString()
        }
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

        viewModel.isPlayingSoundWin.onEach { newValue ->
            if(newValue) {
                playWinSound()
                viewModel.setIsPlayingSoundWin(false)
            }
        }.launchIn(lifecycleScope)

        viewModel.isPlayingSoundLose.onEach { newValue ->
            if(newValue) {
                playLoseSound()
                viewModel.setIsPlayingSoundLose(false)
            }
        }.launchIn(lifecycleScope)
    }

    private fun setRecyclerViews() {

        leftSlotAdapter = SlotAdapter(viewModel.leftSlots)
        centerSlotAdapter = SlotAdapter(viewModel.centerSlots)
        rightSlotAdapter = SlotAdapter(viewModel.rightSlots)
        binding.leftRecyclerView.apply {
            adapter = leftSlotAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setOnTouchListener { _, _ -> true }
        }
        binding.centerRecyclerView.apply {
            adapter = centerSlotAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setOnTouchListener { _, _ -> true }
        }
        binding.rightRecyclerView.apply {
            adapter = rightSlotAdapter
            layoutManager = LinearLayoutManager(requireContext())
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
                playClickSound()
                viewModel.spinSlots(
                    listOf(
                        leftRecyclerView,
                        centerRecyclerView,
                        rightRecyclerView
                    ),
                    requireContext()
                )
            }

            buttonIncreaseBet.setOnClickListener {
                playClickSound()
                viewModel.increaseBet(requireContext())
                lifecycleScope.launch(Dispatchers.IO) {
                    DataManager.saveBet(
                        requireContext(),
                        viewModel.bet.value
                    )
                }
            }

            buttonDecreaseBet.setOnClickListener {
                playClickSound()
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

    private fun playWinSound() {
        MusicUtility.playSound(
            mediaPlayer = (activity as BestActivity).soundPlayer,
            MusicUtility.soundWinResId,
            requireContext(),
            viewModel.viewModelScope,
            viewModel.isSoundOn,
            viewModel.isVibrationOn
        )
    }

    private fun playLoseSound() {
        MusicUtility.playSound(
            mediaPlayer = (activity as BestActivity).soundPlayer,
            MusicUtility.soundLoseResId,
            requireContext(),
            viewModel.viewModelScope,
            viewModel.isSoundOn,
            viewModel.isVibrationOn
        )
    }
}