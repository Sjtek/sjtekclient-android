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
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.ActionInterface;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.control.data.responses.LightsResponse;
import nl.sjtek.control.data.responses.ResponseCollection;

public class LightsCard extends BaseCard implements View.OnClickListener {

    @BindView(R.id.switch1)
    SwitchCompat switch1;
    @BindView(R.id.switch2)
    SwitchCompat switch2;
    @BindView(R.id.switch3)
    SwitchCompat switch3;

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

        if (Preferences.getInstance(getContext()).getCheckExtraLights()) {
            switch3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUpdate(ResponseCollection update) {
        LightsResponse lights = update.getLights();
        switch1.setChecked(lights.isLight1());
        switch2.setChecked(lights.isLight2());
        switch3.setChecked(lights.isLight3());
    }

    @OnClick({R.id.switch1, R.id.switch2, R.id.switch3, R.id.switch4})
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
        }
    }

    private void toggle(ActionInterface action) {
        API.action(getContext(), action);
    }
}