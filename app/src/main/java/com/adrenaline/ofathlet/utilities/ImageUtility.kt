package com.adrenaline.ofathlet.utilities

import com.adrenaline.ofathlet.R
import kotlin.random.Random

object ImageUtility {

    private val drawableTen = R.drawable.slot_ten
    private val drawableA = R.drawable.slot_a
    private val drawableJ = R.drawable.slot_j
    private val drawableK = R.drawable.slot_k
    private val drawableQ = R.drawable.slot_q

    fun getRandomImageId(): Int {
        return Random.nextInt(0, 4)
    }

    fun getDrawableId(imageId: Int): Int {
        return when (imageId) {
            0 -> drawableTen
            1 -> drawableA
            2 -> drawableJ
            3 -> drawableK
            4 -> drawableQ
            else -> throw Exception("Unknown ImageId")
        }
    }
}