package com.adrenaline.ofathlet.presentation

import android.content.Context
import android.util.DisplayMetrics
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.adrenaline.ofathlet.data.Constants
import com.adrenaline.ofathlet.data.DataManager
import com.adrenaline.ofathlet.presentation.slot.Slot
import com.adrenaline.ofathlet.presentation.utilities.ImageUtility
import com.adrenaline.ofathlet.presentation.utilities.ViewUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

    private var login = ""
    var isMusicOn = true
    var isSoundOn = true
    var isVibrationOn = true
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

    private val _isPlayingSoundWin = MutableStateFlow(false)
    val isPlayingSoundWin = _isPlayingSoundWin.asStateFlow()

    private val _isPlayingSoundLose = MutableStateFlow(false)
    val isPlayingSoundLose = _isPlayingSoundLose.asStateFlow()

    private var isSpinningSlots = false
    private var latestIndex = 0
    private val random = Random(Date().time)
    private val positions = mutableListOf(0, 0, 0)


    // wheel variables
    private var spinningDuration = 5000L
    private val sectorsPrizes =
        intArrayOf(0, 100, 50, 0, 10, 200, 10, 100, 50, 100)
    private val sectorDegrees = mutableListOf<Int>()
    // current position of wheel
    private var sectorIndex = 9
    var isSpinningWheel = false

    init {
        getDegreeForSectors()
    }

    fun spinWheel(
        viewImageWheel: ImageView,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            if (isSpinningWheel) return@launch
            setBalance(balance.value - bet.value, context)
            if (balance.value < 0) setBalance(
                Constants.balanceDefault,
                context
            ) // for infinite credits
            isSpinningWheel = true
            val fromDegrees = sectorDegrees[sectorIndex].toFloat()
            sectorIndex = random.nextInt(0, sectorDegrees.size)
            val toDegrees = (360 * sectorDegrees.size).toFloat() + sectorDegrees[sectorIndex]
            val rotateAnimation = RotateAnimation(
                fromDegrees,
                toDegrees,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f
            )
            rotateAnimation.run {

                duration = spinningDuration
                fillAfter = true
                interpolator = AccelerateDecelerateInterpolator()

                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        val sectorPrize = sectorsPrizes[sectorsPrizes.size - (sectorIndex + 1)]
                        val result: Int = (sectorPrize * 0.01 * bet.value).toInt()
                        val isUserWon = sectorPrize != 0

                        if (isUserWon) {
                            setIsPlayingSoundWin(true)
                            setBalance(balance.value + result + bet.value, context)
                            setWin(win.value + result, context)
                        } else setIsPlayingSoundLose(true)

                        isSpinningWheel = false
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}

                })
            }
            viewImageWheel.startAnimation(rotateAnimation)
        }
    }

    fun spinSlots(
        recyclers: List<RecyclerView?>,
        context: Context,
        scope: CoroutineScope = viewModelScope,
    ) {
        scope.launch(Dispatchers.Main) {
            if (isSpinningSlots) return@launch
            isSpinningSlots = true
            setBalance(balance.value - bet.value, context)
            if (balance.value < 0) setBalance(
                Constants.balanceDefault,
                context
            ) // for infinite credits
            generateNewPositions()
            repeat(3) { index ->
                scroll(recyclers[index], index, context)
            }
        }
    }

    private fun scroll(
        recycler: RecyclerView?,
        index: Int,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    return (ViewUtility.getFieldHeight() * 0.0003f)
                }
            }
            smoothScroller.targetPosition = positions[index]
            recycler?.layoutManager?.startSmoothScroll(smoothScroller)
            if (index == latestIndex) attachListener(recycler, context)
        }
    }

    private fun attachListener(column: RecyclerView?, context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            column?.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            checkSlotsCombo(context)
                            isSpinningSlots = false
                            recyclerView.removeOnScrollListener(this)
                        }
                    }
                }
            )
        }
    }

    private fun checkSlotsCombo(
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
            setIsPlayingSoundWin(true)
            setBalance(balance.value + creditsWon, context)
            setWin(win.value + creditsWon, context)
        } else setIsPlayingSoundLose(true)
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

    fun setIsPlayingSoundWin(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _isPlayingSoundWin.emit(value)
        }
    }

    fun setIsPlayingSoundLose(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _isPlayingSoundLose.emit(value)
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
        if (bet.value < balance.value && !isSpinningSlots) {
            setBet(bet.value + 1, context)
        }
    }

    fun decreaseBet(context: Context) {
        if (bet.value > 1 && !isSpinningSlots) {
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
    }

    private fun getDegreeForSectors() {
        viewModelScope.launch {
            val oneSectorDegree = 360 / sectorsPrizes.size
            repeat(sectorsPrizes.size) {
                sectorDegrees += (it + 1) * oneSectorDegree
            }
        }
    }

    fun loadSettings(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val isMusicOn = async { DataManager.loadMusicSetting(context) }
            val isSoundOn = async { DataManager.loadSoundSetting(context) }
            val isVibrationOn = async { DataManager.loadVibrationSetting(context) }
            this@GameViewModel.isMusicOn = isMusicOn.await()
            this@GameViewModel.isSoundOn = isSoundOn.await()
            this@GameViewModel.isVibrationOn = isVibrationOn.await()
        }
    }
}