package nl.sjtek.client.android.cards;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by wouter on 14-11-16.
 */

public class CoffeeCard extends BaseCard {

    @BindView(R.id.imageViewCup)
    ImageView imageView;
    @BindView(R.id.textViewStatus)
    TextView textViewStatus;
    private boolean heated = false;

    public CoffeeCard(Context context) {
        super(context);
    }

    public CoffeeCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoffeeCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_coffee, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonStart)
    public void onStartClick() {
        API.action(getContext(), Action.Coffee.START);
    }

    @Override
    protected void onUpdate(ResponseCollection update) {
        boolean heated = update.getCoffee().isHeated();
        if (this.heated != heated) {
            this.heated = heated;
            updateViews();
        }
    }

    private void updateViews() {
        int color = ContextCompat.getColor(getContext(), heated ? R.color.coffee_heated : R.color.coffee_cold);
        textViewStatus.setTextColor(color);
        textViewStatus.setText(heated ? "Aan" : "Uit");
        imageView.setColorFilter(color);
    }
}
