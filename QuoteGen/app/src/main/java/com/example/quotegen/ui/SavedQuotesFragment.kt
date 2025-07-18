package com.example.quotegen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quotegen.R
import com.example.quotegen.data.QuoteManager
import com.example.quotegen.data.UserPreferences
import com.example.quotegen.model.Quote

class SavedQuotesFragment : Fragment() {

    private lateinit var containerLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_saved_quotes, container, false)
        containerLayout = view.findViewById(R.id.saved_quotes_container)
        refreshQuotes()
        return view
    }

    private fun refreshQuotes() {
        containerLayout.removeAllViews()
        val favorites = QuoteManager.getFavorites()

        val preferredAuthor = UserPreferences.getPreferredAuthor(requireContext())
        val preferredCategory = UserPreferences.getPreferredCategory(requireContext())

        val filteredFavorites = favorites.filter { quote ->
            (preferredAuthor == "All" || quote.author == preferredAuthor) &&
                    (preferredCategory == "All" || quote.category == preferredCategory)
        }

        if (filteredFavorites.isEmpty()) {
            val emptyText = TextView(requireContext()).apply {
                text = "No saved quotes yet."
                textSize = 18f
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setPadding(16, 32, 16, 0)
            }
            containerLayout.addView(emptyText)
        } else {
            filteredFavorites.forEach { quote ->
                val quoteLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(24, 24, 24, 24)
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
                }

                val quoteText = TextView(requireContext()).apply {
                    text = "\"${quote.text}\""
                    textSize = 20f
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.quote_text_color))
                }

                val quoteAuthor = TextView(requireContext()).apply {
                    text = "— ${quote.author}"
                    textSize = 16f
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.quote_text_color))
                }

                val removeButton = Button(requireContext()).apply {
                    text = "Remove"
                    textSize = 14f
                    setOnClickListener {
                        QuoteManager.removeFavorite(quote)
                        Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
                        refreshQuotes()
                    }
                }

                quoteLayout.addView(quoteText)
                quoteLayout.addView(quoteAuthor)
                quoteLayout.addView(removeButton)

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 32)
                }
                quoteLayout.layoutParams = layoutParams

                containerLayout.addView(quoteLayout)
            }
        }
    }
}
