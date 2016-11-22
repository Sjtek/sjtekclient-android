package nl.sjtek.client.android.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.receiver.SjtekWidgetProvider;
import nl.sjtek.client.android.receiver.WiFiReceiver;
import nl.sjtek.client.android.storage.Preferences;

/**
 * Activity for displaying the application settings and user account.<br>
 * The sign in button will go to the {@link ActivityLogin}.
 * After sign in, it will show the users name and will log out the user on click.
 */
public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference accountPref = findPreference(getString(R.string.pref_key_account));
            accountPref.setOnPreferenceClickListener(this);
            setAccountPreference();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            setAccountPreference();
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        /**
         * Update the account field.<br>
         * Will give the option to sign in if no account has been stored.<br>
         * Will show the user name and an option to sign out if there is a user stored.
         */
        private void setAccountPreference() {
            Preference accountPref = findPreference(getString(R.string.pref_key_account));
            if (Preferences.getInstance(getActivity()).isCredentialsSet()) {
                accountPref.setTitle(getString(R.string.pref_title_sign_out));
                accountPref.setSummary(getString(R.string.pref_summary_signed_in, Preferences.getInstance(getActivity()).getUsername()));
            } else {
                accountPref.setTitle(getString(R.string.pref_title_sign_in));
                accountPref.setSummary("");
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.pref_key_notification_enable))) {
                if (getActivity() != null)
                    WiFiReceiver.updateNotification(getActivity().getApplicationContext());
            } else if (key.equals(getString(R.string.pref_key_widget_transparent))) {
                if (getActivity() != null)
                    SjtekWidgetProvider.updateAllWidgets(getActivity().getApplicationContext());
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (Preferences.getInstance(getActivity()).isCredentialsSet()) {
                deleteAccount();
            } else {
                startActivity(new Intent(getActivity().getApplicationContext(), ActivityLogin.class));
            }
            return true;
        }

        @SuppressWarnings("deprecation")
        private void deleteAccount() {
            Preferences preferences = Preferences.getInstance(getActivity());
            AccountManager accountManager = AccountManager.get(getActivity().getApplicationContext());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                accountManager.removeAccount(new Account(preferences.getUsername(), "nl.sjtek"), null, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {

                    }
                }, new Handler());
            } else {
                accountManager.removeAccount(new Account(preferences.getUsername(), "nl.sjtek"), new AccountManagerCallback<Boolean>() {
                    @Override
                    public void run(AccountManagerFuture<Boolean> accountManagerFuture) {

                    }
                }, new Handler());
            }

            preferences.clearCredentials();
            setAccountPreference();
        }
    }
}
