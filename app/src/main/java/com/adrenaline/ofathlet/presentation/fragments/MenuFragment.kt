package com.adrenaline.ofathlet.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.BestActivity
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentMenuBinding
import com.adrenaline.ofathlet.presentation.utilities.MusicUtility
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.buttonBack.setOnClickListener {
            playClickSound()
            findNavController().navigateUp()
        }

        binding.buttonGameWheel.setOnClickListener {
            playClickSound()
            findNavController().navigate(R.id.action_MenuFragment_to_GameWheelFragment)
        }

        binding.buttonGameSlot.setOnClickListener {
            playClickSound()
            findNavController().navigate(R.id.action_MenuFragment_to_GameSlotFragment)
        }

        binding.buttonGameBonus.setOnClickListener {
            playClickSound()
            findNavController().navigate(R.id.action_MenuFragment_to_GameBonusFragment)
        }

        binding.buttonSettings.setOnClickListener {
            playClickSound()
            findNavController().navigate(R.id.action_MenuFragment_to_SettingsFragment)
        }

        binding.linkPrivacy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
            startActivity(browserIntent)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // fix for auto text feature for older Android APIs
            ViewUtility.apply {
                makeTextAutoSize(binding.linkPrivacy)
                makeTextAutoSize(binding.titleGameWheel)
                makeTextAutoSize(binding.titleGameSlot)
                makeTextAutoSize(binding.titleGameBonus)
            }
        }

        return binding.root
    }

    private fun playClickSound() {
        MusicUtility.playSound(
            mediaPlayer = (activity as BestActivity).soundPlayer,
            MusicUtility.soundClickResId,
            requireContext(),
            lifecycleScope
        )
    }
}