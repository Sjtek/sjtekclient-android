package nl.sjtek.client.android.events;

/**
 * Event for {@link nl.sjtek.client.android.activities.ActivityMain} for fragment changes.
 */
public class FragmentChangeEvent {

    private final Type type;
    private final boolean addToBackStack;

    public FragmentChangeEvent(Type type, boolean addToBackStack) {
        this.type = type;
        this.addToBackStack = addToBackStack;
    }

    public Type getType() {
        return type;
    }

    public boolean isAddToBackStack() {
        return addToBackStack;
    }

    public enum Type {
        DASHBOARD,
        MUSIC,
        SONARR,
        TRANSMISSION,
    }
}
