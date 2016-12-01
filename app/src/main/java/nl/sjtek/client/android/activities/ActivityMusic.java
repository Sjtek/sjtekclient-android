package nl.sjtek.client.android.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.adapters.PlaylistAdapter;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.services.SjtekService;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;
import nl.sjtek.control.data.actions.Action;
import nl.sjtek.control.data.responses.MusicResponse;
import nl.sjtek.control.data.responses.ResponseCollection;

public class ActivityMusic extends AppCompatActivity {

    @BindView(R.id.imageViewAlbumArt)
    ImageView imageViewAlbumArt;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewArtist)
    TextView textViewArtist;
    @BindView(R.id.buttonPlay)
    ImageButton imageButtonPlay;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layoutList)
    View layoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
        }


        if (StateManager.getInstance(this).isReady()) {
            long start = System.currentTimeMillis();
            StateManager stateManager = StateManager.getInstance(this);
            onUpdate(stateManager.getResponseCollection());

            String username;
            if (Preferences.getInstance(this).isCredentialsSet()) {
                username = Preferences.getInstance(this).getUsername();
            } else {
                username = "default";
            }
            Map<String, String> playlistMap = stateManager.getDataCollection().getUsers().get(username).getPlaylists();
            List<String> playlistNames = new ArrayList<>();
            playlistNames.addAll(playlistMap.keySet());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setAutoMeasureEnabled(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new PlaylistAdapter(playlistMap, playlistNames));
            Log.d(getClass().getSimpleName(), "Duration: " + (System.currentTimeMillis() - start));
        } else {
            layoutList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Debug.stopMethodTracing();
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
    }

    @OnClick(R.id.imageViewAlbumArt)
    public void onImageClick() {
        supportFinishAfterTransition();
    }

    @OnClick(R.id.buttonPrevious)
    public void onPreviousClick() {
        API.action(this, Action.Music.PREVIOUS);
    }

    @OnClick(R.id.buttonPlay)
    public void onPlayClick() {
        API.action(this, Action.Music.TOGGLE);
    }

    @OnClick(R.id.buttonNext)
    public void onNextClick() {
        API.action(this, Action.Music.NEXT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdate(ResponseCollection responseCollection) {
        MusicResponse music = responseCollection.getMusic();
        MusicResponse.Song song = music.getSong();

        int buttonImage;
        if (music.getState() == MusicResponse.State.STATUS_PLAYING) {
            buttonImage = R.drawable.ic_pause_black_24dp;
        } else {
            buttonImage = R.drawable.ic_play_arrow_black_24dp;
        }

        imageButtonPlay.setImageResource(buttonImage);

        textViewTitle.setText(song.getTitle());
        textViewArtist.setText(TextUtils.isEmpty(song.getArtist()) ? "Music stopped" : song.getArtist());

        String image = "";
        if (!TextUtils.isEmpty(song.getAlbum())) {
            image = song.getAlbumArt();
        } else if (!TextUtils.isEmpty(song.getArtistArt())) {
            image = song.getArtistArt();
        }

        if (TextUtils.isEmpty(image)) {
            imageViewAlbumArt.setImageDrawable(null);
        } else {
            Picasso.with(this)
                    .load(image)
                    .into(imageViewAlbumArt);
        }
    }
}
