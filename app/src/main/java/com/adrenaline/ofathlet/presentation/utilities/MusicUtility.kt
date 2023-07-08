package com.adrenaline.ofathlet.presentation.utilities

import android.content.Context
import android.media.MediaPlayer
import com.adrenaline.ofathlet.R
import com.adrenaline.ofathlet.data.DataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object MusicUtility {

    val musicResId = R.raw.music
    val soundWinResId = R.raw.win_sound
    val soundLoseResId = R.raw.lose_sound
    val soundClickResId = R.raw.click_sound

    fun playMusic(mediaPlayer: MediaPlayer, context: Context, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            val isMusicOn = async {  DataManager.loadMusicSetting(context) }
            if (!isMusicOn.await() || mediaPlayer.isPlaying) return@launch
            launch(Dispatchers.Main) {
                mediaPlayer.isLooping = true
                mediaPlayer.start()
            }
        }
    }

    fun pauseMusic(mediaPlayer: MediaPlayer, scope: CoroutineScope) {
        if (mediaPlayer.isPlaying) {
            scope.launch(Dispatchers.Main) {
                mediaPlayer.pause()
            }
        }
    }

    fun playSound(mediaPlayer: MediaPlayer, rawResId: Int, context: Context, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            val isSoundOn = async { DataManager.loadMusicSetting(context) }
            if (!isSoundOn.await()) return@launch
            launch(Dispatchers.Main) Main@{
                val assetFileDescriptor =
                    context.resources.openRawResourceFd(rawResId) ?: return@Main
                mediaPlayer.reset()
                mediaPlayer.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                assetFileDescriptor.close()
                mediaPlayer.prepareAsync()
            }
        }
    }
}