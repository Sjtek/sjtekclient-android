package nl.sjtek.client.android.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.interfaces.CardCallbackListener;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.update.Music;
import nl.sjtek.client.android.update.Update;

/**
 * Created by Wouter Habets on 22-10-15.
 */
public class MusicCard extends Card implements View.OnClickListener {

    private Holder holder;

    public MusicCard(CardCallbackListener cardCallbackListener) {
        super(cardCallbackListener);
    }

    @Override
    public View inflateView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.card_music, null);
        holder = new Holder(rootView);
        return rootView;
    }

    private void initClickListeners() {
        holder.buttonMusicNext.setOnClickListener(this);
        holder.buttonMusicToggle.setOnClickListener(this);
    }

    @Override
    public void onUpdate(Update update) {
        holder.textViewMusicTitle.setText(update.getMusic().getSong().getTitle());
        holder.textViewMusicArtist.setText(update.getMusic().getSong().getArtist());
        holder.buttonMusicToggle.setImageResource(
                (update.getMusic().getStatus() == Music.PlayerStatus.STATUS_PLAYING ?
                        R.drawable.ic_action_pause : R.drawable.ic_action_play));
    }

    @Override
    public void onClick(View v) {
        if (cardCallbackListener == null) return;
        switch (v.getId()) {
            case R.id.buttonMusicToggle:
                cardCallbackListener.sendRequest(Action.Music.TOGGLE.toString());
                break;
            case R.id.buttonMusicNext:
                cardCallbackListener.sendRequest(Action.Music.NEXT.toString());
                break;
        }
    }

    private class Holder {
        ImageButton buttonMusicNext;
        ImageButton buttonMusicToggle;
        TextView textViewMusicTitle;
        TextView textViewMusicArtist;

        public Holder(View view) {
            buttonMusicNext = (ImageButton) view.findViewById(R.id.buttonMusicNext);
            buttonMusicToggle = (ImageButton) view.findViewById(R.id.buttonMusicToggle);
            textViewMusicTitle = (TextView) view.findViewById(R.id.textViewMusicTitle);
            textViewMusicArtist = (TextView) view.findViewById(R.id.textViewMusicArtist);
        }
    }
}
