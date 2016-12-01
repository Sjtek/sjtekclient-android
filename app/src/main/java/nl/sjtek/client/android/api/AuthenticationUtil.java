package nl.sjtek.client.android.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.sjtek.control.data.actions.Action;

/**
 * Util for authenticating with the API without Volley (for synchronous calls).
 */
public class AuthenticationUtil {

    private static final String URL = Action.DATA.toString();

    private AuthenticationUtil() {

    }

    /**
     * Authenticate with the API synchronously.
     *
     * @param token BASE64 encrypted username and password.
     * @return Returns true if the authentication is successful
     */
    public static boolean authenticate(String token) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + token);
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            switch (code) {
                case 401:
                    return false;
                case 200:
                    return true;
                default:
                    return false;
            }
        } catch (IOException e) {
            return false;
        } finally {
            if (connection != null) connection.disconnect();

        }

    }
}
