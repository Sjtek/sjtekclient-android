package nl.sjtek.client.android.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthenticationUtil {

    private static final String URL = Action.DATA.toString();

    private AuthenticationUtil() {

    }

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
