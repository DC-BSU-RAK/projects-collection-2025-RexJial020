package com.example.quotegen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.quotegen.R
import com.example.quotegen.data.UserPreferences

class SettingsFragment : Fragment() {

    private lateinit var darkModeSwitch: Switch
    private lateinit var authorSpinner: Spinner
    private lateinit var categorySpinner: Spinner

    private val availableAuthors = listOf(
        "All",
        "Oscar Wilde",
        "Albert Einstein",
        "Frank Zappa",
        "Marcus Tullius Cicero",
        "Mahatma Gandhi"
    )

    private val availableCategories = listOf(
        "All",
        "Motivational",
        "Humor",
        "Life",
        "Love"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        darkModeSwitch = view.findViewById(R.id.switch_dark_mode)
        authorSpinner = view.findViewById(R.id.authorSpinner)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        darkModeSwitch.isChecked = UserPreferences.getDarkMode(requireContext())
        setupSpinner(authorSpinner, availableAuthors, UserPreferences.getPreferredAuthor(requireContext())) { selected ->
            UserPreferences.setPreferredAuthor(requireContext(), selected)
        }
        setupSpinner(categorySpinner, availableCategories, UserPreferences.getPreferredCategory(requireContext())) { selected ->
            UserPreferences.setPreferredCategory(requireContext(), selected)
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            UserPreferences.setDarkMode(requireContext(), isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            requireActivity().recreate() // To apply theme changes instantly
        }

        return view
    }

    private fun setupSpinner(
        spinner: Spinner,
        options: List<String>,
        selectedOption: String,
        onItemSelected: (String) -> Unit
    ) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(options.indexOf(selectedOption))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                onItemSelected(options[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
