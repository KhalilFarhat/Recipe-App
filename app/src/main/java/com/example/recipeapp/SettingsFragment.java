package com.example.recipeapp;

import android.os.Bundle;
import android.os.Handler;

import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Get the CheckBoxPreference object for the dark mode setting
        CheckBoxPreference darkModePref = findPreference("dark_mode");

        // Set the preference's summary to reflect its current value
        assert darkModePref != null;
        darkModePref.setSummaryOn("Enabled");
        darkModePref.setSummaryOff("Disabled");

        // Set a listener to detect when the preference value changes
        darkModePref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabled = (boolean) newValue;
            if (enabled) {
                // Apply the dark mode theme
                requireActivity().setTheme(R.style.Dark_Theme_RecipeApp);
            } else {
                // Revert to the default theme
                requireActivity().setTheme(R.style.Theme_RecipeApp);
            }
            new Handler().postDelayed(() -> {
                // Recreate the activity to apply the new theme
                requireActivity().recreate();
            }, 100);
            return true;
        });

    }
}
