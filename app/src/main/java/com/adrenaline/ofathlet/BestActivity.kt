package com.adrenaline.ofathlet

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.adrenaline.ofathlet.presentation.utilities.MusicUtility
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class BestActivity : AppCompatActivity() {

    val musicPlayer: MediaPlayer by lazy {
        MediaPlayer.create(this, MusicUtility.musicResId)
    }
    val soundPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_best)

        soundPlayer.setOnPreparedListener {
            soundPlayer.start()
        }

        soundPlayer.setOnCompletionListener {
            soundPlayer.reset()
        }
    }

    override fun onPause() {
        super.onPause()
        MusicUtility.pauseMusic(musicPlayer, lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        MusicUtility.playMusic(musicPlayer, applicationContext, lifecycleScope)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment_container).navigateUp()
    }
}