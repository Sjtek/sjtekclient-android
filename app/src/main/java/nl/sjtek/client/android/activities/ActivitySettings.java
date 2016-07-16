package nl.sjtek.client.android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.receiver.WiFiReceiver;
import nl.sjtek.client.android.utils.Storage;

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

        private void setAccountPreference() {
            Preference accountPref = findPreference(getString(R.string.pref_key_account));
            if (Storage.getInstance().isCredentialsSet()) {
                accountPref.setTitle(getString(R.string.pref_title_sign_out));
                accountPref.setSummary(getString(R.string.pref_summary_signed_in, Storage.getInstance().getUsername()));
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
                // TODO: 16-7-16 Trigger widget
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (Storage.getInstance().isCredentialsSet()) {
                Storage.getInstance().clearCredentials();
                setAccountPreference();
            } else {
                startActivity(new Intent(getActivity().getApplicationContext(), ActivityLogin.class));
            }
            return true;
        }
    }
}
