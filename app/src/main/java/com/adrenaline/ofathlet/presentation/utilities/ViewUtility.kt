package com.adrenaline.ofathlet.presentation.utilities

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.StateFlow

object ViewUtility {

    private var fieldHeight = 0

    fun updateFieldHeight(
        fieldForSlots: ImageView,
        isHeightCorrect: StateFlow<Boolean>,
        setIsHeightCorrect: () -> Unit,
    ) {
        val vto = fieldForSlots.viewTreeObserver
        vto.addOnGlobalLayoutListener {
            if (!isHeightCorrect.value) {
                fieldHeight = fieldForSlots.height
                setIsHeightCorrect.invoke()
            }
        }
    }

    fun getFieldHeight(): Int = fieldHeight

    fun getSlotHeight(): Int = fieldHeight / 3

    fun hideSoftKeyboard(view: View, fragment: Fragment) {
        val inputMethodManager = fragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}