package com.asd2.uaca.business

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.asd2.uaca.biclient.SettingsActivity

class Settings(context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val apiToken: String = preferences.getString(SettingsActivity.API_TOKEN_TEXT, "")
    val endpoint: String = preferences.getString(SettingsActivity.ENV_LIST, "-1")
}