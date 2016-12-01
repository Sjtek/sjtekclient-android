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
import nl.sjtek.control.data.actions.Action;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Card for controlling the coffee machine.
 */
public class CoffeeCard extends BaseCard {

    @BindView(R.id.imageViewCup)
    ImageView imageView;
    @BindView(R.id.textViewStatus)
    TextView textViewStatus;

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
        updateViews(update.getCoffee().isHeated());
    }

    private void updateViews(boolean heated) {
        int color = ContextCompat.getColor(getContext(), heated ?
                R.color.coffee_heated : R.color.coffee_cold);
        textViewStatus.setTextColor(color);
        textViewStatus.setText(heated ? getContext().getString(R.string.card_coffee_heated_true) : getContext().getString(R.string.card_coffee_heated_false));
        imageView.setColorFilter(color);
    }
}
