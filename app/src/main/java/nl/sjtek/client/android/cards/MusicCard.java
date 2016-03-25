package nl.sjtek.client.android.cards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.activities.ActivityMain;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.control.data.responses.MusicResponse;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 26-1-16.
 */
public class MusicCard extends BaseCard implements View.OnClickListener {

    private View viewMusicInfo;

    private TextView textViewTitle;
    private TextView textViewArtist;
    private TextView textViewElapsed;
    private TextView textViewTotal;
    private ImageView imageViewAlbumArt;
    private ProgressBar progressBar;

    private ImageButton buttonStart;
    private ImageButton buttonMusicBox;
    private ImageButton buttonPlay;
    private ImageButton buttonNext;

    private String albumArtUrl = "";
    private MusicResponse.State state = MusicResponse.State.ERROR;

    public MusicCard(Context context) {
        super(context);
    }

    public MusicCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_music, this);

        viewMusicInfo = findViewById(R.id.musicInfo);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewArtist = (TextView) findViewById(R.id.textViewArtist);
        textViewElapsed = (TextView) findViewById(R.id.textViewTimeElapsed);
        textViewTotal = (TextView) findViewById(R.id.textViewTimeTotal);
        imageViewAlbumArt = (ImageView) findViewById(R.id.imageViewAlbumArt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonStart = (ImageButton) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(this);
        buttonMusicBox = (ImageButton) findViewById(R.id.buttonMusicBox);
        buttonMusicBox.setOnClickListener(this);
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
        buttonNext = (ImageButton) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void onUpdate(ResponseCollection update) {
        MusicResponse music = update.getMusic();
        MusicResponse.Song song = music.getSong();
        textViewTitle.setText(song.getTitle());
        textViewArtist.setText(song.getArtist());
        String elapsed = String.format("%02d:%02d", song.getTimeElapsed() / 60, song.getTimeElapsed() % 60);
        String total = String.format("%02d:%02d", song.getTimeTotal() / 60, song.getTimeTotal() % 60);
        textViewElapsed.setText(elapsed);
        textViewTotal.setText(total);
        progressBar.setMax((int) song.getTimeTotal());
        progressBar.setProgress((int) song.getTimeElapsed());

        if (!song.getAlbumArt().equals(albumArtUrl) && !song.getAlbumArt().isEmpty()) {
            albumArtUrl = song.getAlbumArt();
            Picasso.with(getContext())
                    .load(song.getAlbumArt())
                    .into(imageViewAlbumArt);
        }

        state = music.getState();

        if (state == MusicResponse.State.ERROR || state == MusicResponse.State.STATUS_STOPPED) {
            viewMusicInfo.setVisibility(View.GONE);
        } else {
            viewMusicInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                // TODO: 3-2-16 Start playlist selection
                break;
            case R.id.buttonMusicBox:
                Intent musicIntent = new Intent(ActivityMain.ACTION_CHANGE_FRAGMENT);
                musicIntent.putExtra(ActivityMain.EXTRA_TARGET_FRAGMENT, ActivityMain.TARGET_MUSIC);
                getContext().sendBroadcast(musicIntent);
                break;
            case R.id.buttonPlay:
                if (state == MusicResponse.State.STATUS_PLAYING) {
                    postAction(Action.Music.PAUSE);
                } else {
                    postAction(Action.Music.PLAY);
                }
                break;
            case R.id.buttonNext:
                postAction(Action.Music.NEXT);
        }
    }
}
