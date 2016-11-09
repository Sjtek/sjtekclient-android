package nl.sjtek.client.android.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.sjtek.client.android.R;
import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.responses.TemperatureResponse;

/**
 * Created by Wouter Habets on 3-2-16.
 */
public class TemperatureCard extends BaseCard {

    @BindView(R.id.textViewTempInside)
    TextView textViewTempIn;
    @BindView(R.id.textViewTempOutside)
    TextView textViewTempOut;
    @BindView(R.id.imageViewWeather)
    ImageView imageViewWeather;
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
        ButterKnife.bind(this);
    }

    @Override
    public void onUpdate(ResponseCollection update) {
        TemperatureResponse temperature = update.getTemperature();
        textViewTempIn.setText(String.format("%s", (int) temperature.getInside()));
        textViewTempOut.setText(String.format("%s", (int) temperature.getOutside()));
        String newUrl = temperature.getIcon();
        if (!weatherUrl.equals(newUrl)) {
            weatherUrl = newUrl;
            Picasso.with(getContext()).load(weatherUrl).into(imageViewWeather);
        }
    }
}
