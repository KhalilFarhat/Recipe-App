package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference changePassPreference = findPreference("change_password");
        changePassPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Create a new instance of the AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                // Inflate the layout for the dialog box
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.change_password_dialog, null);
                builder.setView(view);

                // Set up the cancel button to dismiss the dialog box
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Set up the save button to save the new password
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save the new password
                        EditText passwordEditTextOld = view.findViewById(R.id.password_edit_text_old);
                        EditText passwordEditTextNew = view.findViewById(R.id.password_edit_text_new);

                        String OldPassword = passwordEditTextOld.getText().toString();
                        String NewPassword = passwordEditTextNew.getText().toString();

                    }
                });

                // Create and show the dialog box
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

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
