package com.adrenaline.ofathlet.presentation

import android.content.Context
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.adrenaline.ofathlet.data.Constants
import com.adrenaline.ofathlet.data.DataManager
import com.adrenaline.ofathlet.presentation.slot.Slot
import com.adrenaline.ofathlet.presentation.utilities.ImageUtility
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.absoluteValue
import kotlin.random.Random

class GameViewModel : ViewModel() {

    val leftSlots = mutableListOf<Slot>()
    val centerSlots = mutableListOf<Slot>()
    val rightSlots = mutableListOf<Slot>()

    var login = ""
    val isUserLoggedIn get() = login.isNotEmpty()
    var isUserAnonymous = true
    var isLoggingByEmail = false
    var isLoggingByPhone = false

    private val _balance = MutableStateFlow(Constants.balanceDefault)
    val balance = _balance.asStateFlow()

    private val _win = MutableStateFlow(0)
    val win = _win.asStateFlow()

    private val _bet = MutableStateFlow(Constants.betDefault)
    val bet = _bet.asStateFlow()

    private val _isHeightCorrect = MutableStateFlow(false)
    val isHeightCorrect = _isHeightCorrect.asStateFlow()

    private var isSpinning = false
    private var latestIndex = 0
    private val random = Random(Date().time)
    private val positions = mutableListOf(0, 0, 0)

    fun spin(
        recyclers: List<RecyclerView?>,
        managers: List<LinearLayoutManager>,
        context: Context,
        scope: CoroutineScope = viewModelScope,
    ) {
        scope.launch(Dispatchers.Main) {
            if (isSpinning) return@launch
            isSpinning = true
            if (balance.value <= 0) setBalance(
                Constants.balanceDefault,
                context
            ) // for infinite credits
            setBalance(balance.value - bet.value, context)
            generateNewPositions()
            repeat(3) { index ->
                scroll(recyclers[index], managers[index], index, context)
            }
        }
    }

    private fun scroll(
        recycler: RecyclerView?,
        manager: LinearLayoutManager,
        index: Int,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    return (ViewUtility.getFieldHeight() * 0.0002f)
                }

            }
            smoothScroller.targetPosition = positions[index]
            manager.startSmoothScroll(smoothScroller)  // recycler?.layoutManager.startSmoothScroll()
            if (index == latestIndex) attachListener(recycler, context)
        }
    }

    private fun attachListener(column: RecyclerView?, context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            column?.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            checkForCombo(context)
                            isSpinning = false
                            recyclerView.removeOnScrollListener(this)
                        }
                    }
                }
            )
        }
    }

    private fun checkForCombo(
        context: Context,
    ) {
        val leftTopImage = getImageIdById(positions[0], leftSlots)
        val centerTopImage = getImageIdById(positions[1], centerSlots)
        val rightTopImage = getImageIdById(positions[2], rightSlots)
        val leftCenterImage = getImageIdById(positions[0] + 1, leftSlots)
        val centerCenterImage = getImageIdById(positions[1] + 1, centerSlots)
        val rightCenterImage = getImageIdById(positions[2] + 1, rightSlots)
        val leftBottomImage = getImageIdById(positions[0] + 2, leftSlots)
        val centerBottomImage = getImageIdById(positions[1] + 2, centerSlots)
        val rightBottomImage = getImageIdById(positions[2] + 2, rightSlots)
        // win if images are the same
        var creditsWon = 0
        if (leftTopImage == centerTopImage && centerTopImage == rightTopImage) {
            creditsWon += bet.value * 10
        }
        if (leftCenterImage == centerCenterImage && centerCenterImage == rightCenterImage) {
            creditsWon += bet.value * 10
        }
        if (leftBottomImage == centerBottomImage && centerBottomImage == rightBottomImage) {
            creditsWon += bet.value * 10
        }
        if (creditsWon > 0) {
            setBalance(balance.value + creditsWon, context)
            setWin(win.value + creditsWon, context)
        }
    }

    fun generateSlots(amount: Int = 50) {
        repeat(amount) { id ->
            leftSlots.add(Slot(id = id, imageId = ImageUtility.getRandomImageId()))
            centerSlots.add(Slot(id = id, imageId = ImageUtility.getRandomImageId()))
            rightSlots.add(Slot(id = id, imageId = ImageUtility.getRandomImageId()))
        }
    }

    private fun getImageIdById(id: Int, slots: MutableList<Slot>): Int {
        var imageId = 0
        slots.forEach {
            if (it.id == id) {
                imageId = it.imageId
                return@forEach
            }
        }
        return imageId
    }

    // gen positions and define the latest index
    // which means the column that will stop scrolling latest
    private fun generateNewPositions() {
        var biggestDiff = 0
        repeat(3) { index ->
            val currentPosition = positions[index]
            val newPosition = getNewPosition(index)
            positions[index] = newPosition
            val diff = (currentPosition - newPosition).absoluteValue
            if (diff > biggestDiff) {
                biggestDiff = diff
                latestIndex = index
            }
        }
    }

    // position always will be the top one
    // never be as the previous value
    // at least 20 positions before or after the previous one
    private fun getNewPosition(index: Int): Int {
        val newPosition = random.nextInt(0, leftSlots.lastIndex - 1)
        return if (positions[index] == newPosition ||
            (positions[index] - newPosition).absoluteValue < 20
        ) getNewPosition(index) else newPosition
    }

    fun setIsHeightCorrect(newState: Boolean) {
        viewModelScope.launch {
            _isHeightCorrect.emit(newState)
        }
    }

    fun setBalance(value: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _balance.emit(value)
            if (isUserAnonymous) return@launch
            DataManager.saveBalance(context, value)
        }
    }

    fun setWin(value: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _win.emit(value)
            if (isUserAnonymous) return@launch
            DataManager.saveWin(context, value)
        }
    }

    fun setBet(value: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _bet.emit(value)
            if (isUserAnonymous) return@launch
            DataManager.saveBet(context, value)
        }
    }

    fun increaseBet(context: Context) {
        if (bet.value < balance.value && !isSpinning) {
            setBet(bet.value + 1, context)
        }
    }

    fun decreaseBet(context: Context) {
        if (bet.value > 1 && !isSpinning) {
            setBet(bet.value - 1, context)
        }
    }

    fun signIn(context: Context, login: String) {
        this.login = login
        isUserAnonymous = false
        viewModelScope.launch(Dispatchers.IO) {
            DataManager.saveLogin(context, login)
        }
    }

    fun resetScore(context: Context) {
        setWin(0, context)
        setBalance(Constants.balanceDefault, context)
        setBet(Constants.betDefault, context)
    }
}