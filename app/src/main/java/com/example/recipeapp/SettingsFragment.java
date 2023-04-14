package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

        Preference SignOut = findPreference("sign_out");
        SignOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                SharedPreferences sp = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email", "");
                editor.putBoolean("IsSignedIn", false);
                editor.commit();
                startActivity(new Intent(getActivity(),WelcomeActivity.class));
                return true;
            }
        });
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("privacy_policy")) {
            // Show the privacy policy dialog
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                showPrivacyPolicyDialog();
            }
            return true;
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void showPrivacyPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Privacy Policy");

        TextView message = new TextView(requireContext());
        message.setText("This app collects anonymous usage data to help us improve the user experience. We do not collect or share any personal information about you. Your privacy is important to us, and we strive to ensure that your data is always protected. By using this app, you agree to our privacy policy and acknowledge that we may use anonymous usage data to improve our services.");
        message.setPadding(50, 50, 50, 50);
        message.setGravity(Gravity.START);
        message.setTextSize(16);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                message.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            }
        }
        builder.setView(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.show();
    }
}
