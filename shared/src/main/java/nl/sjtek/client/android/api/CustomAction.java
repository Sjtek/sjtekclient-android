package nl.sjtek.client.android.api;

/**
 * Custom API action.
 */
public class CustomAction implements ActionInterface {
    private final String action;

    public CustomAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}
