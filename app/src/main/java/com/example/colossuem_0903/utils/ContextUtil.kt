package com.example.colossuem_0903.utils

import android.content.Context

class ContextUtil {

    companion object {

        private val prefName = "ColosseumPref"
        private val AUTO_LOGIN = "AUTO_LOGIN"

        fun setAutoLogIn(context: Context, isAutoLogIn: Boolean) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            pref.edit().putBoolean(AUTO_LOGIN, isAutoLogIn).apply()
        }

        fun getAutoLogIn(context: Context): Boolean {

            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(AUTO_LOGIN, false)
        }
    }
}