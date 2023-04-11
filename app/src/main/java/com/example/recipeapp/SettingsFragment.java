package com.example.recipeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

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

        // Get the Preference object for the privacy policy setting
        Preference privacyPolicyPref = findPreference("privacy_policy");

        // Set the click listener for the privacy policy preference
        assert privacyPolicyPref != null;
        privacyPolicyPref.setOnPreferenceClickListener(this);

        // Get the CheckBoxPreference object for the auto-update setting
        CheckBoxPreference autoUpdatePref = findPreference("auto_update");

        // Set a listener to detect when the preference value changes
        assert autoUpdatePref != null;
        autoUpdatePref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabled = (boolean) newValue;
            if (enabled) {
                // Enable auto-update
                // TODO: Implement auto-update functionality
            } else {
                // Disable auto-update
                // TODO: Implement auto-update functionality
            }
            return true;
        });
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("privacy_policy")) {
            // Show the privacy policy dialog
            showPrivacyPolicyDialog();
            return true;
        }
        return false;
    }

    private void showPrivacyPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Privacy Policy");
        builder.setMessage("This app collects anonymous usage data to help us improve the user experience. We do not collect or share any personal information about you. Your privacy is important to us, and we strive to ensure that your data is always protected. By using this app, you agree to our privacy policy and acknowledge that we may use anonymous usage data to improve our services.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.show();
    }

}
