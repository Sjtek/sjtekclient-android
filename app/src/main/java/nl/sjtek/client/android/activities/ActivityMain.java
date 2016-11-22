package nl.sjtek.client.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
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

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.cards.MusicSheetCard;
import nl.sjtek.client.android.events.FragmentChangeEvent;
import nl.sjtek.client.android.fragments.FragmentDashboard;
import nl.sjtek.client.android.fragments.FragmentMusic;
import nl.sjtek.client.android.fragments.FragmentSonarr;
import nl.sjtek.client.android.fragments.FragmentTransmission;
import nl.sjtek.client.android.receiver.WiFiReceiver;
import nl.sjtek.client.android.services.SjtekService;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;

/**
 * Main Activity displaying various fragments that can be switched with the navigation drawer.
 */
public class ActivityMain extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ColorChooserDialog.ColorCallback, MusicSheetCard.SheetClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        // Reset the theme so the splash screen will be hidden.
        setTheme(R.style.AppTheme_NoActionBar);
        // Set night mode to auto
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set greeting in Toolbar
        String username = Preferences.getInstance(this).getUsername().toLowerCase();
        if (Preferences.getInstance(this).isCredentialsSet() && toolbar != null) {
            toolbar.setSubtitle(String.format(getString(R.string.toolbar_user), username.substring(0, 1).toUpperCase(), username.substring(1)));
        }

        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Update header in the navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationHeader(navigationView);

        // Setup the bottom sheet (music)
        viewShade = findViewById(R.id.viewShade);
        musicSheetCard = (MusicSheetCard) findViewById(R.id.bottom_sheet_music);
        musicSheetCard.setSheetClickListener(this);
        bottomSheetBehavior = BottomSheetBehavior.from(musicSheetCard);
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);

        // Init the application preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Check if the notification should be shown
        WiFiReceiver.updateNotification(this.getApplicationContext());

        replaceFragment(FragmentChangeEvent.Type.DASHBOARD, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Only show the LED button if it's available for the user
        menu.findItem(R.id.action_led).setVisible(StateManager.getInstance(this).areExtraLightsEnabled(this));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_led:
                showLedDialog();
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

    /**
     * Initialise the navigation view and select a random quote.
     *
     * @param navigationView Navigation view
     */
    private void initNavigationHeader(NavigationView navigationView) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_activity_main, drawer, false);
        TextView textViewLine = (TextView) headerView.findViewById(R.id.textViewHeaderLine);

        if (StateManager.getInstance(this).isReady()) {
            String[] lines = StateManager.getInstance(this).getDataCollection().getQuotes();
            Random random = new Random();
            textViewLine.setText(lines[random.nextInt(lines.length - 1)]);
        }

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

    @Override
    /**
     * Override key down to make it change the SjtekControl volume.
     */
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

    private void showLedDialog() {
        new ColorChooserDialog.Builder(this, R.string.dialog_led_title)
                .doneButton(R.string.dialog_led_done)
                .cancelButton(R.string.dialog_led_cancel)
                .show();
    }

    @Override
    /**
     * Callback for the LED color selection.<br>
     * This will convert the given int to a hex value and to an RGB value.
     */
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        String hex = String.format("#%06X", 0xFFFFFF & selectedColor);
        API.led(this,
                Integer.valueOf(hex.substring(1, 3), 16),
                Integer.valueOf(hex.substring(3, 5), 16),
                Integer.valueOf(hex.substring(5, 7), 16));
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
