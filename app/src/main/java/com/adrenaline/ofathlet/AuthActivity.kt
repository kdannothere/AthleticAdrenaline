package com.adrenaline.ofathlet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}