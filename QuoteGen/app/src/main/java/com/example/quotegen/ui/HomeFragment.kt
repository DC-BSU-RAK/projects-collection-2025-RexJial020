package com.example.quotegen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quotegen.R
import com.example.quotegen.data.QuoteManager
import com.example.quotegen.model.Quote
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var quoteText: TextView
    private lateinit var quoteAuthor: TextView
    private lateinit var shuffleButton: Button
    private lateinit var favoriteButton: Button

    private val quotes = listOf(
        Quote(1, "Be yourself; everyone else is already taken.", "Oscar Wilde", "Motivational"),
        Quote(2, "Two things are infinite: the universe and human stupidity; and I'm not sure about the universe.", "Albert Einstein", "Humor"),
        Quote(3, "So many books, so little time.", "Frank Zappa", "Life"),
        Quote(4, "A room without books is like a body without a soul.", "Marcus Tullius Cicero", "Life"),
        Quote(5, "Be the change that you wish to see in the world.", "Mahatma Gandhi", "Motivational"),
        Quote(6, "In the middle of difficulty lies opportunity.", "Albert Einstein", "Motivational"),
        Quote(7, "If you tell the truth, you don't have to remember anything.", "Mark Twain", "Life"),
        Quote(8, "We are what we repeatedly do. Excellence, then, is not an act, but a habit.", "Aristotle", "Motivational"),
        Quote(9, "Without music, life would be a mistake.", "Friedrich Nietzsche", "Life"),
        Quote(10, "Life is what happens when you're busy making other plans.", "John Lennon", "Life"),
        Quote(11, "The only way to do great work is to love what you do.", "Steve Jobs", "Motivational"),
        Quote(12, "I have not failed. I've just found 10,000 ways that won't work.", "Thomas Edison", "Motivational"),
        Quote(13, "To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment.", "Ralph Waldo Emerson", "Motivational"),
        Quote(14, "Do what you can, with what you have, where you are.", "Theodore Roosevelt", "Motivational"),
        Quote(15, "The greatest glory in living lies not in never falling, but in rising every time we fall.", "Nelson Mandela", "Motivational"),
        Quote(16, "Happiness depends upon ourselves.", "Aristotle", "Life"),
        Quote(17, "Love all, trust a few, do wrong to none.", "William Shakespeare", "Love"),
        Quote(18, "It is better to be hated for what you are than to be loved for what you are not.", "Andre Gide", "Life"),
        Quote(19, "If you want to live a happy life, tie it to a goal, not to people or things.", "Albert Einstein", "Life"),
        Quote(20, "The purpose of our lives is to be happy.", "Dalai Lama", "Life"),
        Quote(21, "Life is really simple, but we insist on making it complicated.", "Confucius", "Life"),
        Quote(22, "Success usually comes to those who are too busy to be looking for it.", "Henry David Thoreau", "Motivational"),
        Quote(23, "Keep your face always toward the sunshine—and shadows will fall behind you.", "Walt Whitman", "Motivational"),
        Quote(24, "The best way to predict the future is to invent it.", "Alan Kay", "Motivational"),
        Quote(25, "Love recognizes no barriers.", "Maya Angelou", "Love")
    )



    private var currentQuote: Quote = quotes[0]

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        quoteText = view.findViewById(R.id.quote_text)
        quoteAuthor = view.findViewById(R.id.quote_author)
        shuffleButton = view.findViewById(R.id.shuffle_button)
        favoriteButton = view.findViewById(R.id.favorite_button)

        updateQuote(currentQuote)

        shuffleButton.setOnClickListener {
            currentQuote = quotes[Random.nextInt(quotes.size)]
            updateQuote(currentQuote)
        }

        favoriteButton.setOnClickListener {
            QuoteManager.addFavorite(currentQuote)
            Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun updateQuote(quote: Quote) {
        quoteText.text = "\"${quote.text}\""
        quoteAuthor.text = "— ${quote.author}"
    }
}
