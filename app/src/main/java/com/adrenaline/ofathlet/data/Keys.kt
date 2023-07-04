package com.adrenaline.ofathlet.data

import androidx.datastore.preferences.core.intPreferencesKey

object Keys {
    val balanceKey = intPreferencesKey("balance")
    val winKey = intPreferencesKey("win")
    val betKey = intPreferencesKey("bet")
}