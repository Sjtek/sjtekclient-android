package nl.sjtek.client.android.cards;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.activities.ActivityMain;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.utils.Storage;
import nl.sjtek.control.data.responses.LightsResponse;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 24-1-16.
 */
public class LightsCard extends BaseCard implements View.OnClickListener {

    private SwitchCompat switch1, switch2, switch3, switch4;

    public LightsCard(Context context) {
        super(context);
    }

    public LightsCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightsCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_lights, this);
        switch1 = (SwitchCompat) findViewById(R.id.switch1);
        switch1.setOnClickListener(this);
        switch2 = (SwitchCompat) findViewById(R.id.switch2);
        switch2.setOnClickListener(this);
        switch3 = (SwitchCompat) findViewById(R.id.switch3);
        switch3.setOnClickListener(this);
        switch4 = (SwitchCompat) findViewById(R.id.switch4);
        switch4.setOnClickListener(this);
        Button buttonLEDController = (Button) findViewById(R.id.button);
        buttonLEDController.setOnClickListener(this);

        if (Storage.getInstance().getCheckExtraLights()) {
            switch3.setVisibility(View.VISIBLE);
            buttonLEDController.setVisibility(View.VISIBLE);
        }

        switch4.setVisibility(View.GONE);
    }

    @Override
    public void onUpdate(ResponseCollection update) {
        LightsResponse lights = update.getLights();
        switch1.setChecked(lights.isLight1());
        switch2.setChecked(lights.isLight2());
        switch3.setChecked(lights.isLight3());
        switch4.setChecked(lights.isLight4());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button) {
            Intent ledIntent = new Intent(ActivityMain.ACTION_CHANGE_FRAGMENT);
            ledIntent.putExtra(ActivityMain.EXTRA_TARGET_FRAGMENT, ActivityMain.TARGET_LED);
            getContext().sendBroadcast(ledIntent);
        } else {
            SwitchCompat switchCompat = (SwitchCompat) v;
            boolean enabled = switchCompat.isChecked();
            switch (id) {
                case R.id.switch1:
                    postAction(enabled ? Action.Light.TOGGLE_1_ON : Action.Light.TOGGLE_1_OFF);
                    break;
                case R.id.switch2:
                    postAction(enabled ? Action.Light.TOGGLE_2_ON : Action.Light.TOGGLE_2_OFF);
                    break;
                case R.id.switch3:
                    postAction(enabled ? Action.Light.TOGGLE_3_ON : Action.Light.TOGGLE_3_OFF);
                    break;
                case R.id.switch4:
                    postAction(enabled ? Action.Light.TOGGLE_4_ON : Action.Light.TOGGLE_4_OFF);
                    break;
            }
        }
    }
}
