package com.example.quotegen.data

import android.content.Context
import android.content.SharedPreferences
import com.example.quotegen.model.Quote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object QuoteManager {

    private const val PREFS_NAME = "quote_prefs"
    private const val FAVORITES_KEY = "favorite_quotes"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun addFavorite(quote: Quote) {
        val favorites = getFavorites().toMutableList()
        if (favorites.none { it.id == quote.id }) {
            favorites.add(quote)
            saveFavorites(favorites)
        }
    }

    fun getFavorites(): List<Quote> {
        val json = prefs.getString(FAVORITES_KEY, null)
        if (json.isNullOrEmpty()) return emptyList()

        val type = object : TypeToken<List<Quote>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun removeFavorite(quote: Quote) {
        val favorites = getFavorites().filter { it.id != quote.id }
        saveFavorites(favorites)
    }

    private fun saveFavorites(favorites: List<Quote>) {
        val json = Gson().toJson(favorites)
        prefs.edit().putString(FAVORITES_KEY, json).apply()
    }
}
