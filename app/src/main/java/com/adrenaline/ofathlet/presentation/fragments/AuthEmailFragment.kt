package com.adrenaline.ofathlet.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.databinding.FragmentAuthEmailBinding
import com.adrenaline.ofathlet.presentation.GameViewModel
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility

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
            signIn()
            if (viewModel.isUserLoggedIn) {
                findNavController().navigate(R.id.action_AuthEmailFragment_to_MenuFragment)
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

        return binding.root
    }

    private fun signIn(email: String = binding.loginValue.text.toString()) {
        val isEmailAddress = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!isEmailAddress) return
        viewModel.signIn(requireContext(), email)
    }
}