package nl.sjtek.client.android.cards;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.events.MealEvent;

/**
 * Created by wouter on 15-11-16.
 */

public class MealCard extends BaseCard {

    @BindView(R.id.textViewMeal)
    TextView textView;

    public MealCard(Context context) {
        super(context);
    }

    public MealCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MealCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_meal, this);
        ButterKnife.bind(this);
        refresh();
    }

    @OnClick(R.id.buttonRefresh)
    public void refresh() {
        API.meal(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(MealEvent event) {
        if (TextUtils.isEmpty(event.getName())) {
            textView.setText(R.string.card_meal_default);
        } else {
            textView.setText(event.getName());
        }
    }
}
