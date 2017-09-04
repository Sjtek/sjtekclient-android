package nl.sjtek.client.android.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
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
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.cards.MusicSheetCard;
import nl.sjtek.client.android.events.FragmentChangeEvent;
import nl.sjtek.client.android.fragments.FragmentDashboard;
import nl.sjtek.client.android.fragments.FragmentMusic;
import nl.sjtek.client.android.fragments.FragmentSonarr;
import nl.sjtek.client.android.fragments.FragmentTransmission;
import nl.sjtek.client.android.geofence.GeofenceUtils;
import nl.sjtek.client.android.services.UpdateService;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;
import nl.sjtek.control.data.actions.Actions;

/**
 * Main Activity displaying various fragments that can be switched with the navigation drawer.
 */
public class ActivityMain extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MusicSheetCard.SheetClickListener {

    public static final String ACTION_TARGET = "nl.sjtek.client.android.action.TARGET";
    public static final String EXTRA_TARGET = "extra_target";
    public static final String TARGET_MUSIC = "music";
    private DrawerLayout drawer;
    private MusicSheetCard musicSheetCard;
    private View viewShade;

    /**
     * Callbacks for the bottom sheet (music)
     */
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            // Make the screen darker
            viewShade.setAlpha(slideOffset);
            // Pass the offset to the sheet
            musicSheetCard.onSlide(slideOffset);
        }
    };
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    @SuppressLint("DefaultLocale")
    protected void onCreate(Bundle savedInstanceState) {
        // Reset the theme so the splash screen will be hidden.
        setTheme(R.style.AppTheme_NoActionBar);
        // Set night mode to auto
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set greeting in Toolbar
        String username = Preferences.getInstance(this).getUsername().toLowerCase();
        if (Preferences.getInstance(this).isCredentialsSet() && toolbar != null) {
            toolbar.setSubtitle(String.format(getString(R.string.toolbar_user), username.substring(0, 1).toUpperCase(), username.substring(1)));
        }

        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Update header in the navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationHeader(navigationView);

        // Setup the bottom sheet (music)
        viewShade = findViewById(R.id.viewShade);
        musicSheetCard = findViewById(R.id.bottom_sheet_music);
        musicSheetCard.setSheetClickListener(this);
        bottomSheetBehavior = BottomSheetBehavior.from(musicSheetCard);
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);

        // Init the application preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Start geofencing
        GeofenceUtils.INSTANCE.start(this);

        if (getIntent().getAction() != null && getIntent().getAction().equals(ACTION_TARGET)) {
            switch (getIntent().getStringExtra(EXTRA_TARGET)) {
                case TARGET_MUSIC:
                    replaceFragment(FragmentChangeEvent.Type.MUSIC, false);
                    break;
            }
        } else {
            replaceFragment(FragmentChangeEvent.Type.DASHBOARD, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_screen:
                startActivity(new Intent(this, ScreenActivity.class));
                return true;
            case R.id.action_nightmode:
                API.toggleNightMode(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getInstance(this).areCredentialsChanged()) {
            Preferences.getInstance(this).clearCredentialsChangedFlag();
            recreate();
            return;
        }
        EventBus.getDefault().register(this);
        API.data(getApplicationContext());
        startService(new Intent(this, UpdateService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        StateManager.INSTANCE.save(getApplicationContext());
        stopService(new Intent(this, UpdateService.class));
    }

    /**
     * Initialise the navigation view and select a random quote.
     *
     * @param navigationView Navigation view
     */
    private void initNavigationHeader(NavigationView navigationView) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_activity_main, drawer, false);
        TextView textViewLine = headerView.findViewById(R.id.textViewHeaderLine);

        List<String> quotes = StateManager.INSTANCE.getQuotes();
        if (quotes == null) quotes = new ArrayList<>();
        if (quotes.size() > 1) {
            Random random = new Random();
            textViewLine.setText(quotes.get(random.nextInt(quotes.size() - 1)));
        }

        navigationView.addHeaderView(headerView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        } else if (id == R.id.nav_hue) {
            Intent hueIntent = getPackageManager().getLaunchIntentForPackage("com.philips.lighting.hue2");
            if (hueIntent != null) {
                startActivity(hueIntent);
            }
        } else if (id == R.id.nav_nap) {
            Intent napIntent = getPackageManager().getLaunchIntentForPackage("io.habets.nap");
            if (napIntent != null) {
                startActivity(napIntent);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void replaceFragment(FragmentChangeEvent event) {
        replaceFragment(event.getType(), event.isAddToBackStack());
    }

    private void replaceFragment(FragmentChangeEvent.Type target, boolean addToBackStack) {
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

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        transaction.commit();
    }

    /**
     * Override key down to make it change the SjtekControl volume.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            API.action(getApplicationContext(), Actions.INSTANCE.getMusic().volumeLower());
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            API.action(getApplicationContext(), Actions.INSTANCE.getMusic().volumeIncrease());
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onSheetClick() {
        switch (bottomSheetBehavior.getState()) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }
}
