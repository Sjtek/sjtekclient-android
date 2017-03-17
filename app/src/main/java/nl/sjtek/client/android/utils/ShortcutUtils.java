package nl.sjtek.client.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;

import java.util.ArrayList;
import java.util.List;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.activities.ActivityMain;

/**
 * Utility for setting the launcher shortcuts.
 */
public class ShortcutUtils {

    private static final String ID_MUSIC = "id_music";
    private static final String ID_TOGGLE = "id_toggle";
    private static final String ID_HUE = "id_hue";
    private static final String ID_NAP = "id_nap";

    private ShortcutUtils() {

    }

    @SuppressLint("NewApi")
    public static void setShortcuts(Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N_MR1) return;
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);

        Intent musicIntent = new Intent(context, ActivityMain.class);
        musicIntent.setAction(ActivityMain.ACTION_TARGET);
        musicIntent.putExtra(ActivityMain.EXTRA_TARGET, ActivityMain.TARGET_MUSIC);

        List<ShortcutInfo> shortcuts = new ArrayList<>();

        Intent napIntent = context.getPackageManager().getLaunchIntentForPackage("io.habets.nap");
        if (napIntent != null) {
            ShortcutInfo shortcutNap = new ShortcutInfo.Builder(context, ID_NAP)
                    .setShortLabel(context.getString(R.string.shortcut_nap))
                    .setLongLabel(context.getString(R.string.shortcut_nap))
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_nap))
                    .setIntent(napIntent)
                    .build();
            shortcuts.add(shortcutNap);
        }


        Intent hueIntent = context.getPackageManager().getLaunchIntentForPackage("com.philips.lighting.hue2");
        if (hueIntent != null) {
            ShortcutInfo shortcutHue = new ShortcutInfo.Builder(context, ID_HUE)
                    .setShortLabel(context.getString(R.string.shortcut_hue))
                    .setLongLabel(context.getString(R.string.shortcut_hue))
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_hue))
                    .setIntent(hueIntent)
                    .build();
            shortcuts.add(shortcutHue);
        }


        ShortcutInfo shortcutMusic = new ShortcutInfo.Builder(context, ID_MUSIC)
                .setShortLabel(context.getString(R.string.shortcut_music))
                .setLongLabel(context.getString(R.string.shortcut_music))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_music))
                .setIntent(musicIntent)
                .build();
        shortcuts.add(shortcutMusic);

        ShortcutInfo shortcutToggle = new ShortcutInfo.Builder(context, ID_TOGGLE)
                .setShortLabel(context.getString(R.string.shortcut_toggle))
                .setLongLabel(context.getString(R.string.shortcut_toggle))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_toggle))
                .setIntent(new Intent("nl.sjtek.client.android.ACTION_SWITCH"))
                .build();
        shortcuts.add(shortcutToggle);

        shortcutManager.setDynamicShortcuts(shortcuts);

    }
}
