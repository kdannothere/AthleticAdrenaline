package com.adrenaline.ofathlet.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    suspend fun saveBalance(context: Context, balance: Int) {
        context.dataStore.edit { data ->
            data[Keys.balanceKey] = balance
        }
    }

    suspend fun saveWin(context: Context, win: Int) {
        context.dataStore.edit { data ->
            data[Keys.winKey] = win
        }
    }

    suspend fun saveBet(context: Context, bet: Int) {
        context.dataStore.edit { data ->
            data[Keys.betKey] = bet
        }
    }

    suspend fun saveLogin(context: Context, login: String) {
        context.dataStore.edit { data ->
            data[Keys.loginKey] = login
        }
    }

    suspend fun saveMusicSetting(context: Context, isMusicOn: Boolean) {
        context.dataStore.edit { data ->
            data[Keys.musicSettingKey] = isMusicOn
        }
    }

    suspend fun saveSoundSetting(context: Context, isSoundOn: Boolean) {
        context.dataStore.edit { data ->
            data[Keys.soundSettingKey] = isSoundOn
        }
    }

    suspend fun saveVibrationSetting(context: Context, isVibrationOn: Boolean) {
        context.dataStore.edit { data ->
            data[Keys.vibrationSettingKey] = isVibrationOn
        }
    }

    suspend fun loadBalance(context: Context, currentValue: Int): Int {
        return context.dataStore.data
            .map { preferences ->
                preferences[Keys.balanceKey]
                    ?: currentValue
            }
            .first()
    }

    suspend fun loadWin(context: Context, currentValue: Int): Int {
        return context.dataStore.data
            .map { preferences ->
                preferences[Keys.winKey]
                    ?: currentValue
            }
            .first()
    }

    suspend fun loadBet(context: Context, currentValue: Int): Int {
        return context.dataStore.data
            .map { preferences ->
                preferences[Keys.betKey] ?: currentValue
            }
            .first()
    }

    suspend fun loadLogin(context: Context): String {
        return context.dataStore.data
            .map { preferences ->
                preferences[Keys.loginKey] ?: ""
            }
            .first()
    }

    suspend fun loadMusicSetting(context: Context): Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[Keys.musicSettingKey] ?: true
            }
            .first()
    }

    suspend fun loadSoundSetting(context: Context): Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[Keys.soundSettingKey] ?: true
            }
            .first()
    }

    suspend fun loadVibrationSetting(context: Context): Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[Keys.vibrationSettingKey] ?: true
            }
            .first()
    }
}