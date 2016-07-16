package nl.sjtek.client.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.InfoRequest;
import nl.sjtek.client.android.api.ToggleRequest;
import nl.sjtek.client.android.api.WatsonRequest;
import nl.sjtek.client.android.api.WatsonResponse;
import nl.sjtek.client.android.fragments.FragmentDashboard;
import nl.sjtek.client.android.fragments.FragmentLED;
import nl.sjtek.client.android.fragments.FragmentMusic;
import nl.sjtek.client.android.fragments.FragmentSonarr;
import nl.sjtek.client.android.fragments.FragmentTransmission;
import nl.sjtek.client.android.interfaces.OnVolumePressListener;
import nl.sjtek.client.android.receiver.WiFiReceiver;
import nl.sjtek.client.android.utils.Storage;
import nl.sjtek.control.data.responses.ResponseCollection;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_TARGET_FRAGMENT = "extra_target_fragment";
    public static final String EXTRA_BACKSTACK = "add_to_backstack";
    public static final String ACTION_CHANGE_FRAGMENT = "nl.sjtek.client.android.ACTION_CHANGE_FRAGMENT";
    public static final String TARGET_DASHBOARD = "target_dashboard";
    public static final String TARGET_MUSIC = "target_music";
    public static final String TARGET_SONARR = "target_sonarr";
    public static final String TARGET_TRANSMISSION = "target_transmission";
    public static final String TARGET_LED = "target_led";
    private static final int REQUEST_VOICE_RECOGNITION = 8001;

    private IntentFilter intentFilter = new IntentFilter(ACTION_CHANGE_FRAGMENT);
    private OnVolumePressListener volumeListener = null;
    private FloatingActionButton fab;
    private BroadcastReceiver fragmentBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_CHANGE_FRAGMENT)) {
                String target = intent.getStringExtra(EXTRA_TARGET_FRAGMENT);
                boolean addToBackStack = intent.getBooleanExtra(EXTRA_BACKSTACK, false);
                if (target != null && !target.isEmpty()) {
                    replaceFragment(target, addToBackStack);
                }
            }
        }
    };
    private RequestQueue requestQueue;

    private ProgressDialog progressDialog;

    private TextView textViewTemp;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String username = Storage.getInstance().getUsername().toLowerCase();
        if (Storage.getInstance().isCredentialsSet() && toolbar != null) {
            toolbar.setSubtitle(String.format("Hallo %s%s", username.substring(0, 1).toUpperCase(), username.substring(1)));
        }

        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAction(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationHeader(navigationView);

        String target = TARGET_DASHBOARD;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            target = bundle.getString(EXTRA_TARGET_FRAGMENT, TARGET_DASHBOARD);
        }

        replaceFragment(target);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        WiFiReceiver.updateNotification(this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_led).setVisible(Storage.getInstance().getCheckExtraLights());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_led:
                replaceFragment(new FragmentLED(), true);
                return true;
            case R.id.action_toggle:
                requestQueue.add(new ToggleRequest(new Response.Listener<ResponseCollection>() {
                    @Override
                    public void onResponse(ResponseCollection response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Storage.getInstance().areCredentialsChanged()) {
            Storage.getInstance().setCredentialsChanged(false);
            recreate();
        }
        registerReceiver(fragmentBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(fragmentBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void initNavigationHeader(NavigationView navigationView) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_activity_main, null);
        textViewTemp = (TextView) headerView.findViewById(R.id.textViewHeaderTemp);
        TextView textViewLine = (TextView) headerView.findViewById(R.id.textViewHeaderLine);

        String[] lines = getResources().getStringArray(R.array.sjtek_lines);
        Random random = new Random();
        textViewLine.setText(lines[random.nextInt(lines.length - 1)]);

        navigationView.addHeaderView(headerView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            replaceFragment(new FragmentDashboard());
        } else if (id == R.id.nav_music) {
            replaceFragment(new FragmentMusic());
        } else if (id == R.id.nav_sonarr) {
            replaceFragment(new FragmentSonarr());
        } else if (id == R.id.nav_transmission) {
            replaceFragment(new FragmentTransmission());
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, ActivitySettings.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
    }

    public void replaceFragment(String target) {
        replaceFragment(target, false);
    }

    public void replaceFragment(String target, boolean addToBackStack) {
        switch (target) {
            case TARGET_DASHBOARD:
                replaceFragment(new FragmentDashboard(), addToBackStack);
                break;
            case TARGET_MUSIC:
                replaceFragment(new FragmentMusic(), addToBackStack);
                break;
            case TARGET_SONARR:
                replaceFragment(new FragmentSonarr(), addToBackStack);
                break;
            case TARGET_TRANSMISSION:
                replaceFragment(new FragmentTransmission(), addToBackStack);
                break;
            case TARGET_LED:
                replaceFragment(new FragmentLED(), true);
                break;
            default:
                replaceFragment(new FragmentDashboard(), addToBackStack);
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        transaction.commit();

        if (fragment instanceof FragmentDashboard) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }

        if (fragment instanceof FragmentMusic) {
            volumeListener = (FragmentMusic) fragment;
        } else {
            volumeListener = null;
        }
    }

    private void fabAction(View view) {
        startVoiceRecognition();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (volumeListener == null) return super.onKeyDown(keyCode, event);

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeListener.onVolumeLower();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeListener.onVolumeRaise();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        startActivityForResult(intent, REQUEST_VOICE_RECOGNITION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VOICE_RECOGNITION && resultCode == Activity.RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = results.get(0);
            try {
                voiceCommand(text);
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
                Snackbar.make(findViewById(android.R.id.content), "Watson can't process " + text,
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void voiceCommand(final String text) throws UnsupportedEncodingException, JSONException {
        showLoadingDialog("Watson is thinking", text);
        requestQueue.add(new WatsonRequest(text, new Response.Listener<WatsonResponse>() {
            @Override
            public void onResponse(WatsonResponse response) {
                hideLoadingDialog();
                speak(response.getText());
                requestQueue.add(new InfoRequest(response.getUrl(), new Response.Listener<ResponseCollection>() {
                    @Override
                    public void onResponse(ResponseCollection response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingDialog();
                speak("I don't understand, " + text);
                Snackbar.make(findViewById(android.R.id.content), "Watson doesn't understand " + text,
                        Snackbar.LENGTH_LONG).show();
            }
        }));
    }

    @SuppressWarnings("deprecation")
    private void speak(final String text) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        textToSpeech.setLanguage(Locale.UK);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    private void showLoadingDialog(String title, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        } else {
            progressDialog.dismiss();
        }

        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoadingDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }
}
