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
}