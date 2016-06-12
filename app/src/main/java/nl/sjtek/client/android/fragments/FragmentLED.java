package nl.sjtek.client.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.InfoRequest;
import nl.sjtek.client.android.api.LEDRequest;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 20-3-16.
 */
public class FragmentLED extends Fragment implements View.OnTouchListener {

    private static final int SEND_INTERVAL = 50;
    private RequestQueue ledRequestQueue;
    private boolean requestsRunning = false;
    private Response.Listener<ResponseCollection> infoListener = new Response.Listener<ResponseCollection>() {
        @Override
        public void onResponse(ResponseCollection response) {

        }
    };
    private Response.ErrorListener infoErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };
    private Response.Listener<Boolean> ledListener = new Response.Listener<Boolean>() {
        @Override
        public void onResponse(Boolean response) {
            requestsRunning = false;
        }
    };
    private Response.ErrorListener ledErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    private int previousCode = 0;
    private int currentCode = 0;

    private CountDownTimer countDownTimer = new CountDownTimer(SEND_INTERVAL, SEND_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (previousCode != currentCode && !requestsRunning) {
                previousCode = currentCode;
                sendCode(currentCode);
            }
            countDownTimer.start();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ledRequestQueue = Volley.newRequestQueue(context);
    }

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
                ledRequestQueue.add(new InfoRequest(Action.Light.TOGGLE_3_ON, infoListener, infoErrorListener));
            }
        });

        Button buttonDisable = (Button) rootView.findViewById(R.id.buttonDisable);
        buttonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledRequestQueue.add(new InfoRequest(Action.Light.TOGGLE_3_OFF, infoListener, infoErrorListener));
            }
        });

        Button buttonDemoEnable = (Button) rootView.findViewById(R.id.buttonDemoEnable);
        buttonDemoEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(3);
            }
        });

        Button buttonDemoDisable = (Button) rootView.findViewById(R.id.buttonDemoDisable);
        buttonDemoDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(4);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        countDownTimer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                parseEvent(v, event);
                return true;
            case MotionEvent.ACTION_MOVE:
                parseEvent(v, event);
                return true;
            case MotionEvent.ACTION_UP:
                parseEvent(v, event);
                return true;
        }
        return false;
    }

    private void parseEvent(View v, MotionEvent event) {
        float width = v.getWidth();
        float x = event.getX();
        float code = map(x, 0f, width, 100f, 699f);
        currentCode = (int) code;
    }

    private float map(float x, float inMin, float inMax, float outMin, float outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    private void sendCode(int code) {
        if (!requestsRunning) {
            ledRequestQueue.add(new LEDRequest(code, ledListener, ledErrorListener));
        }
    }
}
