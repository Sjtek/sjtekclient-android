package nl.sjtek.client.android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import java.util.Random;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;
import nl.sjtek.client.android.fragments.FragmentDashboard;
import nl.sjtek.client.android.fragments.FragmentLED;
import nl.sjtek.client.android.fragments.FragmentMusic;
import nl.sjtek.client.android.fragments.FragmentSonarr;
import nl.sjtek.client.android.fragments.FragmentTransmission;
import nl.sjtek.client.android.receiver.WiFiReceiver;
import nl.sjtek.client.android.utils.Storage;

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

    private IntentFilter intentFilter = new IntentFilter(ACTION_CHANGE_FRAGMENT);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String username = Storage.getInstance(this).getUsername().toLowerCase();
        if (Storage.getInstance(this).isCredentialsSet() && toolbar != null) {
            toolbar.setSubtitle(String.format("Hallo %s%s", username.substring(0, 1).toUpperCase(), username.substring(1)));
        }

        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                API.action(getApplicationContext(), Action.SWITCH, new Arguments().setDefaultUser(getApplicationContext()));
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Context context = getApplicationContext();
                API.action(context, Action.SWITCH,
                        new Arguments()
                                .setDefaultUser(context)
                                .setUseVoice(true)
                                .setDefaultPlaylist(context));
                return true;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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
        menu.findItem(R.id.action_led).setVisible(Storage.getInstance(this).getCheckExtraLights());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_led:
                replaceFragment(new FragmentLED(), true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Storage.getInstance(this).areCredentialsChanged()) {
            Storage.getInstance(this).setCredentialsChanged(false);
            recreate();
        }
        registerReceiver(fragmentBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(fragmentBroadcastReceiver);
    }

    private void initNavigationHeader(NavigationView navigationView) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_activity_main, null);
//        textViewTemp = (TextView) headerView.findViewById(R.id.textViewHeaderTemp);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            API.action(getApplicationContext(), Action.Music.VOLUME_LOWER);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            API.action(getApplicationContext(), Action.Music.VOLUME_RAISE);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
