package nl.sjtek.client.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.LEDRequest;

/**
 * Created by Wouter Habets on 20-3-16.
 */
public class FragmentLED extends BaseFragment implements View.OnTouchListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_led, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);

        Button buttonEnable = (Button) rootView.findViewById(R.id.buttonEnable);
        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(2, true);
            }
        });

        Button buttonDisable = (Button) rootView.findViewById(R.id.buttonDisable);
        buttonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(1, false);
            }
        });

        Button buttonDemoEnable = (Button) rootView.findViewById(R.id.buttonDemoEnable);
        buttonDemoEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(3, true);
            }
        });

        Button buttonDemoDisable = (Button) rootView.findViewById(R.id.buttonDemoDisable);
        buttonDemoDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(4, true);
            }
        });

        return rootView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                float width = v.getWidth();
                float x = event.getX();
                float code = map(x, 0f, width, 100f, 699f);
                Log.d(this.getClass().getSimpleName(), "Width: " + width + " X: " + x + " Code: " + code);
                sendCode((int) code, true);
                return true;
        }
        return false;
    }

    private float map(float x, float inMin, float inMax, float outMin, float outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    private void sendCode(int code, boolean enable) {
        if (!areRequestsRunning()) addRequest(new LEDRequest(code, enable, this, this));
    }
}
