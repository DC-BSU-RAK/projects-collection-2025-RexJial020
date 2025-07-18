package com.example.quotegen.data

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {

    private const val PREFS_NAME = "quote_preferences"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_AUTHOR = "preferred_author"
    private const val KEY_CATEGORY = "preferred_category"

    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Dark Mode
    fun setDarkMode(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun getDarkMode(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_DARK_MODE, false)
    }

    // Filters (author/category)
    fun setPreferredAuthor(context: Context, author: String) {
        getPrefs(context).edit().putString(KEY_AUTHOR, author).apply()
    }

    fun getPreferredAuthor(context: Context): String {
        return getPrefs(context).getString(KEY_AUTHOR, "All") ?: "All"
    }

    fun setPreferredCategory(context: Context, category: String) {
        getPrefs(context).edit().putString(KEY_CATEGORY, category).apply()
    }

    fun getPreferredCategory(context: Context): String {
        return getPrefs(context).getString(KEY_CATEGORY, "All") ?: "All"
    }
}
