package com.adrenaline.ofathlet.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentAuthPhoneBinding
import com.adrenaline.ofathlet.presentation.GameViewModel
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility

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
            signIn()
            if (viewModel.isUserLoggedIn) {
                findNavController().navigate(R.id.action_AuthPhoneFragment_to_MenuFragment)
            }
        }

        binding.loginValue.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ViewUtility.hideSoftKeyboard(view, this)
                binding.buttonPlay.callOnClick()
                true
            } else {
                false
            }
        }
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        binding.countryPicker.setTypeFace(typeface)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // fixing auto text feature for older android devices
            ViewUtility.apply {
                makeTextAutoSize(binding.textSignUp)
                makeTextAutoSize(binding.titlePlay)
            }
        }

        return binding.root
    }

    private fun signIn(phone: String = binding.loginValue.text.toString()) {
        val countryCode = binding.countryPicker.selectedCountryCode
        val isPhoneNumber = Patterns.PHONE.matcher(countryCode + phone).matches()
        if (!isPhoneNumber) return
        viewModel.signIn(requireContext(), phone)
        binding.buttonPlay.callOnClick()
    }
}