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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    String oldPassword = "";
    String newPassword = "";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference changePassPreference = findPreference("change_password");
        changePassPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Create a new instance of the AlertDialog.Builder
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.change_password_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Do nothing here because we override this button later to change the close behaviour.
                                //However, we still need this because on older versions of Android unless we
                                //pass a handler the button doesn't get instantiated
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        EditText passwordEditTextOld = view.findViewById(R.id.password_edit_text_old);
                        EditText passwordEditTextNew = view.findViewById(R.id.password_edit_text_new);
                        if (passwordEditTextOld != null && passwordEditTextNew != null) {
                            passwordEditTextOld.setTypeface(null, Typeface.BOLD);
                            passwordEditTextNew.setTypeface(null, Typeface.BOLD);
                        }

                        dbHelper db = new dbHelper(getContext());
                        SharedPreferences sp = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        String email = sp.getString("email", "");
                        String oldPassword = passwordEditTextOld.getText().toString();
                        String newPassword = passwordEditTextNew.getText().toString();
                        if(newPassword.length() <8)
                            Toast.makeText(getActivity().getApplicationContext(), "Password should be more than 8 characters", Toast.LENGTH_SHORT).show();
                        else if (oldPassword.equals(newPassword))
                            Toast.makeText(getActivity().getApplicationContext(), "New password cannot be the same as old password", Toast.LENGTH_SHORT).show();
                        else if (db.changePassword(email,oldPassword,newPassword)){
                            Toast.makeText(getActivity().getApplicationContext(), "Password updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else
                            Toast.makeText(getActivity().getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });

        Preference deleteAccountPreference = findPreference("delete_account");
        deleteAccountPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Create a new instance of the AlertDialog.Builder


                // Inflate the layout for the dialog box
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.areyousure_dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Do nothing here because we override this button later to change the close behaviour.
                                //However, we still need this because on older versions of Android unless we
                                //pass a handler the button doesn't get instantiated
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dbHelper db = new dbHelper(getContext());
                        SharedPreferences sp = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        String email = sp.getString("email", "");
                        db.deleteAccount(email);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", "");
                        editor.putBoolean("IsSignedIn", false);
                        editor.commit();
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(),WelcomeActivity.class));
                    }
                });
                return true;
            }
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
