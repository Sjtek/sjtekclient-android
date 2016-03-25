package nl.sjtek.client.android.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nl.sjtek.client.android.R;
import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.responses.TemperatureResponse;

/**
 * Created by Wouter Habets on 3-2-16.
 */
public class TemperatureCard extends BaseCard {

    private TextView textViewTempIn;
    private TextView textViewTempOut;
    private ImageView imageViewWeather;
    private String weatherUrl = "";

    public TemperatureCard(Context context) {
        super(context);
    }

    public TemperatureCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemperatureCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_temperature, this);
        textViewTempIn = (TextView) findViewById(R.id.textViewTempInside);
        textViewTempOut = (TextView) findViewById(R.id.textViewTempOutside);
        imageViewWeather = (ImageView) findViewById(R.id.imageViewWeather);
    }

    @Override
    public void onUpdate(ResponseCollection update) {
        TemperatureResponse temperature = update.getTemperature();
        textViewTempIn.setText(String.format("%s", temperature.getInside()));
        textViewTempOut.setText(String.format("%s", temperature.getOutside()));
        String newUrl = temperature.getIcon();
        if (!weatherUrl.equals(newUrl)) {
            weatherUrl = newUrl;
            Picasso.with(getContext()).load(weatherUrl).into(imageViewWeather);
        }
    }
}
