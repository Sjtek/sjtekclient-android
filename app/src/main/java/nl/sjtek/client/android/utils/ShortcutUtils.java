package nl.sjtek.client.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;

import java.util.Arrays;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.activities.ActivityMain;

/**
 * Created by wouter on 20-10-16.
 */

public class ShortcutUtils {

    private static final String ID_MUSIC = "id_music";
    private static final String ID_TOGGLE = "id_toggle";

    private ShortcutUtils() {

    }

    public static void setShortcuts(Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N_MR1) return;
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);

        Intent musicIntent = new Intent(context, ActivityMain.class);
        musicIntent.setAction(ActivityMain.ACTION_CHANGE_FRAGMENT);
        musicIntent.putExtra(ActivityMain.EXTRA_TARGET_FRAGMENT, ActivityMain.TARGET_MUSIC);
        musicIntent.putExtra(ActivityMain.EXTRA_BACKSTACK, false);

        ShortcutInfo shortcutMusic = new ShortcutInfo.Builder(context, ID_MUSIC)
                .setShortLabel(context.getString(R.string.shortcut_music))
                .setLongLabel(context.getString(R.string.shortcut_music))
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_shortcut_music))
                .setIntent(musicIntent)
                .build();

        ShortcutInfo shortcutToggle = new ShortcutInfo.Builder(context, ID_TOGGLE)
                .setShortLabel(context.getString(R.string.shortcut_toggle))
                .setLongLabel(context.getString(R.string.shortcut_toggle))
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_shortcut_toggle))
                .setIntent(new Intent("nl.sjtek.client.android.ACTION_SWITCH"))
                .build();

        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcutMusic, shortcutToggle));

    }
}