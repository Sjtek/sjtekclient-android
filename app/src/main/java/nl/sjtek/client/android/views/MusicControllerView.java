package nl.sjtek.client.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import nl.sjtek.client.android.R;

/**
 * Created by Wouter Habets on 11-1-16.
 */
public class MusicControllerView extends RelativeLayout {

    public MusicControllerView(Context context) {
        this(context, null);
    }

    public MusicControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.music_bar, this);
    }
}
