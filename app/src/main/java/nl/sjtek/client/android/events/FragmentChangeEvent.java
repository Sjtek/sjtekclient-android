package nl.sjtek.client.android.events;

/**
 * Created by wouter on 15-11-16.
 */

public class FragmentChangeEvent {

    private final Type type;
    private final boolean addToBackstack;

    public FragmentChangeEvent(Type type, boolean addToBackstack) {
        this.type = type;
        this.addToBackstack = addToBackstack;
    }

    public Type getType() {
        return type;
    }

    public boolean isAddToBackstack() {
        return addToBackstack;
    }

    public enum Type {
        DASHBOARD,
        MUSIC,
        SONARR,
        TRANSMISSION,
    }
}
