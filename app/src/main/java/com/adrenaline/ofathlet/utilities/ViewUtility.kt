package com.adrenaline.ofathlet.utilities

import android.widget.ImageView
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

}