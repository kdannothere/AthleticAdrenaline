package com.adrenaline.ofathlet.data

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Keys {
    val balanceKey = intPreferencesKey("balance")
    val winKey = intPreferencesKey("win")
    val betKey = intPreferencesKey("bet")
    val loginKey = stringPreferencesKey("login")
}