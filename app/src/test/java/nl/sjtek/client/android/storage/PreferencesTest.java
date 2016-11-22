package nl.sjtek.client.android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * Created by wouter on 22-11-16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PreferencesTest {

    private static final String KEY = "preferences";

    private SharedPreferences getSharedPreferences() {
        return RuntimeEnvironment.application.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    @Before
    public void clearPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        sharedPreferences.edit().clear().commit();
    }

    @Test
    public void clearCredentials() throws Exception {
        Preferences preferences = new Preferences(getSharedPreferences());
        preferences.clearCredentials();
        assertEquals(preferences.getUsername(), "");
        assertEquals(preferences.getToken(), "");
        assertEquals(preferences.areCredentialsChanged(), true);
        assertEquals(preferences.isCredentialsSet(), false);
    }

    @Test
    public void setCredentials() throws Exception {
        final String user = "testuser";
        final String password = "testpassword";
        final String token = "Basic dGVzdHVzZXI6dGVzdHBhc3N3b3Jk";
        Preferences preferences = new Preferences(getSharedPreferences());
        preferences.setCredentials(user, password);
        assertEquals(preferences.getUsername(), user);
        assertEquals(preferences.getToken(), token);
        assertEquals(preferences.areCredentialsChanged(), true);
        assertEquals(preferences.isCredentialsSet(), true);
    }

    @Test
    public void getToken() throws Exception {
        Preferences preferences = new Preferences(getSharedPreferences());
        assertEquals(preferences.getToken(), "");
    }

    @Test
    public void getUsername() throws Exception {
        Preferences preferences = new Preferences(getSharedPreferences());
        assertEquals(preferences.getUsername(), "");
    }

    @Test
    public void isCredentialsSet() throws Exception {
        Preferences preferences = new Preferences(getSharedPreferences());
        assertEquals(preferences.isCredentialsSet(), false);
    }

    @Test
    public void areCredentialsChanged() throws Exception {
        Preferences preferences = new Preferences(getSharedPreferences());
        assertEquals(preferences.areCredentialsChanged(), false);
    }

    @Test
    public void clearCredentialsChangedFlag() throws Exception {
        Preferences preferences = new Preferences(getSharedPreferences());
        assertEquals(preferences.areCredentialsChanged(), false);
        preferences.clearCredentials();
        assertEquals(preferences.areCredentialsChanged(), true);
        preferences.clearCredentialsChangedFlag();
        assertEquals(preferences.areCredentialsChanged(), false);
    }

    @Test
    public void isEmpty() throws Exception {
        assertEquals(Preferences.isEmpty("hoi"), false);
        assertEquals(Preferences.isEmpty(""), true);
        assertEquals(Preferences.isEmpty(" "), true);
        assertEquals(Preferences.isEmpty(null), true);
    }

}