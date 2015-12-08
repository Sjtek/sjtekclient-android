package nl.sjtek.client.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Random;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.UpdateRequest;
import nl.sjtek.client.android.api.VoiceParsingRequest;
import nl.sjtek.client.android.fragments.FragmentDashboard;
import nl.sjtek.client.android.fragments.FragmentMusic;
import nl.sjtek.client.android.fragments.FragmentSonarr;
import nl.sjtek.client.android.fragments.FragmentTransmission;
import nl.sjtek.client.android.interfaces.OnVolumePressListener;
import nl.sjtek.client.android.receiver.WiFiReceiver;
import nl.sjtek.client.android.update.Update;
import nl.sjtek.client.android.utils.Storage;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_VOICE_RECOGNITION = 8001;
    private static final String EXTRA_TARGET_FRAGMENT = "extra_target_fragment";
    private static final int TARGET_MUSIC = 1;
    private static final int TARGET_SONARR = 2;
    private static final int TARGET_TRANSMISSION = 3;

    private OnVolumePressListener volumeListener = null;
    private FloatingActionButton fab;
    private RequestQueue requestQueue;

    private ProgressDialog progressDialog;

    private TextView textViewTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        int target = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            target = bundle.getInt(EXTRA_TARGET_FRAGMENT);
        }

        switch (target) {
            case TARGET_MUSIC:
                replaceFragment(new FragmentMusic());
                break;
            case TARGET_SONARR:
                replaceFragment(new FragmentSonarr());
                break;
            case TARGET_TRANSMISSION:
                replaceFragment(new FragmentTransmission());
                break;
            default:
                replaceFragment(new FragmentDashboard());
        }

        WiFiReceiver.updateNotification(this.getApplicationContext());
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

    public void setTemperature(int temperature) {
        if (textViewTemp != null)
            textViewTemp.setText(String.format("%d ºC", temperature));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        } else if (id == R.id.nav_sign_in) {
            startActivity(new Intent(this, ActivityLogin.class));
        } else if (id == R.id.nav_sign_out) {
            Storage.getInstance().clearCredentials();
            startActivity(new Intent(this, ActivityLogin.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
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
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
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
            executeVoiceCommand(text);
        }
    }

    private void executeVoiceCommand(final String text) {
        showLoadingDialog("Emma is thinking...", text);
        requestQueue.add(new VoiceParsingRequest(text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoadingDialog("Emma is working for you", response);
                Log.i("URL Request", "Response: " + response);
                requestQueue.add(new UpdateRequest(response, new Response.Listener<Update>() {
                    @Override
                    public void onResponse(Update response) {
                        Log.i("Update Request", "Response: " + response.toString());
                        hideLoadingDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Update Request", "Error: " + error.getMessage());
                        hideLoadingDialog();
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                "Emma couldn't complete your request.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                }));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("URL Request", "Error: " + error.getMessage());
                hideLoadingDialog();
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Emma doesn't understand " + text,
                        Snackbar.LENGTH_SHORT).show();
            }
        }));
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
