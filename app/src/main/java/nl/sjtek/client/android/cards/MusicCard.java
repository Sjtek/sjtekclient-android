package nl.sjtek.client.android.cards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;
import nl.sjtek.client.android.events.FragmentChangeEvent;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.control.data.responses.MusicResponse;
import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.settings.DataCollection;

/**
 * Created by Wouter Habets on 26-1-16.
 */
public class MusicCard extends BaseCard {

    private final Map<String, String> playlistMap = new HashMap<>();
    @BindView(R.id.musicInfo)
    View viewMusicInfo;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewArtist)
    TextView textViewArtist;
    @BindView(R.id.textViewTimeElapsed)
    TextView textViewElapsed;
    @BindView(R.id.textViewTimeTotal)
    TextView textViewTotal;
    @BindView(R.id.imageViewAlbumArt)
    ImageView imageViewAlbumArt;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String[] playlistNames = new String[]{};
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
        ButterKnife.bind(this);
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
    protected void onDataUpdate(DataCollection data) {
        String user = Preferences.getInstance(getContext()).getUsername();
        playlistMap.putAll(data.getUsers().get(user).getPlaylists());
        Set<String> names = playlistMap.keySet();
        playlistNames = names.toArray(new String[names.size()]);
    }

    @OnClick({R.id.buttonStart, R.id.buttonMusicBox, R.id.buttonPlay, R.id.buttonNext})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                showPlaylists();
                break;
            case R.id.buttonMusicBox:
                EventBus.getDefault().post(new FragmentChangeEvent(FragmentChangeEvent.Type.MUSIC, true));
                break;
            case R.id.buttonPlay:
                if (state == MusicResponse.State.STATUS_PLAYING) {
                    API.action(getContext(), Action.Music.PAUSE);
                } else {
                    API.action(getContext(), Action.Music.PLAY);
                }
                break;
            case R.id.buttonNext:
                API.action(getContext(), Action.Music.NEXT);
        }
    }

    private void showPlaylists() {
        new MaterialDialog.Builder(getContext())
                .title("Start muziek")
                .items(playlistNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        dialog.dismiss();
                        String playlist = playlistMap.get(playlistNames[position]);
                        API.action(getContext(), Action.Music.START, new Arguments().setUrl(playlist));
                    }
                })
                .build()
                .show();
    }
}
