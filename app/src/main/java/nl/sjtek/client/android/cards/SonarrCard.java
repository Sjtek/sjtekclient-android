package nl.sjtek.client.android.cards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.sjtek.client.android.R;
import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.responses.SonarrResponse;

/**
 * Created by wouter on 15-11-16.
 */

public class SonarrCard extends BaseCard {

    private static final String EPISODE_NAME_TEMPLATE = "S%02dE%02d %s";
    private static final String DISK_TEMPLATE = "%02d%%";

    @BindView(R.id.textViewUpcoming)
    TextView textViewTitleUpcoming;
    @BindView(R.id.textViewShowTitle)
    TextView textViewShowTitle;
    @BindView(R.id.textViewEpisodeName)
    TextView textViewEpisodeName;
    @BindView(R.id.textViewDiskRootPercentage)
    TextView textViewDiskRoot;
    @BindView(R.id.textViewDiskDataPercentage)
    TextView textViewDiskData;

    public SonarrCard(Context context) {
        super(context);
    }

    public SonarrCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SonarrCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_sonarr, this);
        ButterKnife.bind(this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onUpdate(ResponseCollection update) {
        List<SonarrResponse.Episode> episodes = update.getSonarr().getUpcoming();
        Map<String, SonarrResponse.Disk> disks = update.getSonarr().getDiskUsage();

        boolean available = episodes.size() != 0;
        int visibility = available ? View.VISIBLE : View.GONE;
        textViewTitleUpcoming.setVisibility(visibility);
        textViewShowTitle.setVisibility(visibility);
        textViewEpisodeName.setVisibility(visibility);

        if (available) {
            SonarrResponse.Episode episode = episodes.get(0);
            textViewShowTitle.setText(episode.getSeriesTitle());
            textViewEpisodeName.setText(String.format(EPISODE_NAME_TEMPLATE, episode.getSeasonInt(), episode.getEpisodeInt(), episode.getEpisodeName()));
        }

        textViewDiskRoot.setText(getDiskPercentage(disks.get("/")));
        textViewDiskData.setText(getDiskPercentage(disks.get("/tv")));
    }

    @SuppressLint("DefaultLocale")
    private String getDiskPercentage(SonarrResponse.Disk disk) {
        return String.format(DISK_TEMPLATE, (int) ((disk.getTotal() - disk.getFree()) / disk.getTotal() * 100));
    }
}
