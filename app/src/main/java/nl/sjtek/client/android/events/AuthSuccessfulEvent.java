package nl.sjtek.client.android.events;


import java.util.List;

import nl.sjtek.control.data.staticdata.User;

/**
 * Event when authentication was successful.
 */
public class AuthSuccessfulEvent {
    private final List<User> users;

    public AuthSuccessfulEvent(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
