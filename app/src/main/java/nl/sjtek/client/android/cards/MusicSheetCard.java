package nl.sjtek.client.android.cards;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.control.data.responses.MusicResponse;
import nl.sjtek.control.data.responses.ResponseCollection;

public class MusicSheetCard extends LinearLayout {

    @BindView(R.id.textViewTopTitle)
    TextView textViewTopTitle;
    @BindView(R.id.textViewTopArtist)
    TextView textViewTopArtist;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewArtist)
    TextView textViewArtist;
    @BindView(R.id.buttonPlay)
    ImageButton imageButtonPlay;
    @BindView(R.id.buttonTopPlay)
    ImageButton imageButtonTopPlay;
    @BindView(R.id.imageViewAlbumArt)
    ImageView imageViewAlbumArt;
    @BindView(R.id.imageViewArtistArt)
    ImageView imageViewArtistArt;
    @BindView(R.id.viewTitle)
    View viewTitle;
    @BindView(R.id.viewInfo)
    View viewInfo;

    private SheetClickListener sheetClickListener;

    public MusicSheetCard(Context context) {
        this(context, null);
    }

    public MusicSheetCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicSheetCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.card_music_sheet, this);
        ButterKnife.bind(this);
        viewTitle.setAlpha(0);
    }

    public void setSheetClickListener(SheetClickListener sheetClickListener) {
        this.sheetClickListener = sheetClickListener;
    }


    public void onSlide(float slideOffset) {
        viewTitle.setAlpha(slideOffset);
        viewInfo.setAlpha(1 - slideOffset);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdate(ResponseCollection responseCollection) {
        MusicResponse music = responseCollection.getMusic();
        setMusicInfo(music);
    }

    @OnClick({R.id.buttonPrevious})
    public void onPreviousClick() {
        API.action(getContext(), Action.Music.PREVIOUS);
    }

    @OnClick({R.id.buttonNext, R.id.buttonTopNext})
    public void onNextClick() {
        API.action(getContext(), Action.Music.NEXT);
    }

    @OnClick({R.id.buttonPlay, R.id.buttonTopPlay})
    public void onToggleClick() {
        API.action(getContext(), Action.Music.TOGGLE);
    }

    @OnClick(R.id.viewTop)
    public void onTopClick() {
        if (sheetClickListener != null) sheetClickListener.onSheetClick();
    }

    private void setMusicInfo(MusicResponse music) {
        MusicResponse.Song song = music.getSong();

        int buttonImage;
        if (music.getState() == MusicResponse.State.STATUS_PLAYING) {
            buttonImage = R.drawable.ic_pause_black_24dp;
        } else {
            buttonImage = R.drawable.ic_play_arrow_black_24dp;
        }

        imageButtonPlay.setImageResource(buttonImage);
        imageButtonTopPlay.setImageResource(buttonImage);

        textViewTopTitle.setText(song.getTitle());
        textViewTopArtist.setText(TextUtils.isEmpty(song.getArtist()) ? "Music stopped" : song.getArtist());

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
            imageViewArtistArt.setImageDrawable(null);
        } else {
            Picasso.with(getContext())
                    .load(image)
                    .into(imageViewAlbumArt);

//            Picasso.with(getContext())
//                    .load(song.getArtistArt())
//                    .fit()
//                    .centerCrop()
//                    .transform(new BlurTransformation(getContext(), 20))
//                    .into(imageViewArtistArt);
        }
    }

    public interface SheetClickListener {
        void onSheetClick();
    }
}
