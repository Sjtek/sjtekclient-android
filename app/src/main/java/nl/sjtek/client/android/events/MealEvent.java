package nl.sjtek.client.android.events;

/**
 * Event for a new updated dinner suggestion.
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
