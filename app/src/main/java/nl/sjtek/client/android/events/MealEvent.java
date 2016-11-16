package nl.sjtek.client.android.events;

/**
 * Created by wouter on 15-11-16.
 */

public class MealEvent {
    private final String name;

    public MealEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
