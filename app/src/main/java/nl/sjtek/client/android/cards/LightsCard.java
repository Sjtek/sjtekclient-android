package nl.sjtek.client.android.cards;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.storage.StateManager;
import nl.sjtek.control.data.actions.Action;
import nl.sjtek.control.data.actions.ActionInterface;
import nl.sjtek.control.data.actions.CustomAction;
import nl.sjtek.control.data.responses.LightsResponse;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Card for controlling the lights.
 */
public class LightsCard extends BaseCard implements View.OnClickListener {

    @BindView(R.id.switch1)
    SwitchCompat switch1;
    @BindView(R.id.switch2)
    SwitchCompat switch2;
    @BindView(R.id.switch3)
    SwitchCompat switch3;
    @BindView(R.id.switch5)
    SwitchCompat switch5;
    @BindView(R.id.switch7)
    SwitchCompat switch7;

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
        ButterKnife.bind(this);

        // Only show the third toggle (LED strip) if the user is allowed to see it.
        if (StateManager.getInstance(getContext()).areExtraLightsEnabled(getContext())) {
            switch3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUpdate(ResponseCollection update) {
        LightsResponse lights = update.getLights();
        switch1.setChecked(lights.isLight1());
        switch2.setChecked(lights.isLight2());
        switch3.setChecked(lights.isLight3());
        switch5.setChecked(lights.isLight5());
        switch7.setChecked(lights.isLight7());
    }

    // TODO: 15-3-17 Fix hardcoded URL
    @OnClick({R.id.switch1, R.id.switch2, R.id.switch3, R.id.switch5, R.id.switch7})
    public void onClick(View v) {
        boolean enabled = ((SwitchCompat) v).isChecked();
        switch (v.getId()) {
            case R.id.switch1:
                toggle(enabled ? Action.Light.TOGGLE_1_ON : Action.Light.TOGGLE_1_OFF);
                break;
            case R.id.switch2:
                toggle(enabled ? Action.Light.TOGGLE_2_ON : Action.Light.TOGGLE_2_OFF);
                break;
            case R.id.switch3:
                toggle(enabled ? Action.Light.TOGGLE_3_ON : Action.Light.TOGGLE_3_OFF);
                break;
            case R.id.switch5:
                toggle(enabled ? new CustomAction("https://sjtek.nl/api/lights/toggle5on") : new CustomAction("https://sjtek.nl/api/lights/toggle5off"));
                break;
            case R.id.switch7:
                toggle(enabled ? new CustomAction("https://sjtek.nl/api/lights/toggle7on") : new CustomAction("https://sjtek.nl/api/lights/toggle7off"));
                break;
        }
    }

    private void toggle(ActionInterface action) {
        API.action(getContext(), action);
    }
}