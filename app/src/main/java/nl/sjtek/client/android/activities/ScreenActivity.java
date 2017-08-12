package nl.sjtek.client.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.services.UpdateService;
import nl.sjtek.control.data.actions.Action;
import nl.sjtek.control.data.actions.ActionInterface;
import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.responses.ScreenResponse;

public class ScreenActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        startService(new Intent(this, UpdateService.class));
        API.info(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, UpdateService.class));
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onUpdate(ResponseCollection responseCollection) {
        ScreenResponse screen = (ScreenResponse) responseCollection.get("screen");
        String text = "Sjtek screen" +
                "\nCurrent state: " + screen.getState().toString() +
                "\nCurrent time : " + screen.getCurrentTime() +
                "\nTrigger time : " + screen.getNextTrigger() +
                "\n" +
                "\nTitle    : " + screen.getTitle() +
                "\nHeader   : " + screen.getHeader() +
                "\nSubtitle : " + screen.getSubtitle();
        textView.setText(text);
    }

    @OnClick(R.id.buttonFullscreen)
    public void onFullscreen() {
        API.action(this, Action.Screen.MODE_FULLSCREEN);
    }

    @OnClick(R.id.buttonMusic)
    public void onMusic() {
        API.action(this, Action.Screen.MODE_MUSIC);
    }

    @OnClick(R.id.buttonTV)
    public void onTV() {
        API.action(this, Action.Screen.MODE_TV);
    }

    @OnClick(R.id.buttonCountdown)
    public void onCountdown() {
        API.action(this, Action.Screen.MODE_COUNTDOWN);
    }

    @OnClick(R.id.buttonNewYear)
    public void onNewYear() {
        API.action(this, Action.Screen.MODE_NEW_YEAR);
    }

    @OnClick(R.id.buttonRefresh)
    public void onRefresh() {
        API.action(this, Action.Screen.REFRESH);
    }

    @OnLongClick(R.id.buttonTrigger)
    public boolean onTrigger() {
        API.action(this, Trigger.TRIGGER);
        return true;
    }

    public enum Trigger implements ActionInterface {
        MODE_FULLSCREEN("/fullscreen"),
        MODE_MUSIC("/music"),
        MODE_TV("/tv"),
        MODE_COUNTDOWN("/countdown"),
        MODE_NEW_YEAR("/newyear"),
        REFRESH("/refresh"),
        TRIGGER("/triggered");

        private static final String BASE = "/screen";
        private final String path;

        Trigger(String urlAction) {
            this.path = urlAction;
        }

        public String getPath() {
            return "/screen" + this.path;
        }

        public String getUrl() {
            return "https://sjtek.nl/api/screen" + this.path;
        }

        public String toString() {
            return this.getUrl();
        }
    }
}
