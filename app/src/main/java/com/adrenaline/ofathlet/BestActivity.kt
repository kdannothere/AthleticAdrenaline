package com.adrenaline.ofathlet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.michaelrocks.paranoid.Obfuscate

// make logic for games
// save data

@Obfuscate
class BestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_best)
    }
}