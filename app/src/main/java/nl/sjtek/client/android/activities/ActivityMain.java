package nl.sjtek.client.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Random;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;
import nl.sjtek.client.android.events.FragmentChangeEvent;
import nl.sjtek.client.android.fragments.FragmentDashboard;
import nl.sjtek.client.android.fragments.FragmentLED;
import nl.sjtek.client.android.fragments.FragmentMusic;
import nl.sjtek.client.android.fragments.FragmentSonarr;
import nl.sjtek.client.android.fragments.FragmentTransmission;
import nl.sjtek.client.android.receiver.WiFiReceiver;
import nl.sjtek.client.android.services.SjtekService;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String username = Preferences.getInstance(this).getUsername().toLowerCase();
        if (Preferences.getInstance(this).isCredentialsSet() && toolbar != null) {
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationHeader(navigationView);

        replaceFragment(FragmentChangeEvent.Type.DASHBOARD, false);

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
        menu.findItem(R.id.action_led).setVisible(Preferences.getInstance(this).getCheckExtraLights());
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        API.data(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Preferences.getInstance(this).areCredentialsChanged()) {
            Preferences.getInstance(this).clearCredentialsChangedFlag();
            recreate();
        }
        startService(new Intent(this, SjtekService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, SjtekService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        StateManager.getInstance(getApplicationContext()).save(getApplicationContext());
    }

    private void initNavigationHeader(NavigationView navigationView) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_activity_main, drawer, false);
        TextView textViewLine = (TextView) headerView.findViewById(R.id.textViewHeaderLine);

        String[] lines = StateManager.getInstance(this).getDataCollection().getQuotes();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            replaceFragment(FragmentChangeEvent.Type.DASHBOARD, false);
        } else if (id == R.id.nav_music) {
            replaceFragment(FragmentChangeEvent.Type.MUSIC, false);
        } else if (id == R.id.nav_sonarr) {
            replaceFragment(FragmentChangeEvent.Type.SONARR, false);
        } else if (id == R.id.nav_transmission) {
            replaceFragment(FragmentChangeEvent.Type.TRANSMISSION, false);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, ActivitySettings.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe
    public void replaceFragment(FragmentChangeEvent event) {
        replaceFragment(event.getType(), event.isAddToBackstack());
    }

    public void replaceFragment(FragmentChangeEvent.Type target, boolean addToBackStack) {
        switch (target) {
            case DASHBOARD:
                replaceFragment(new FragmentDashboard(), addToBackStack);
                break;
            case MUSIC:
                replaceFragment(new FragmentMusic(), addToBackStack);
                break;
            case SONARR:
                replaceFragment(new FragmentSonarr(), addToBackStack);
                break;
            case TRANSMISSION:
                replaceFragment(new FragmentTransmission(), addToBackStack);
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
