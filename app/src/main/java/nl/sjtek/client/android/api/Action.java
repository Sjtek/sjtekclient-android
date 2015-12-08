package nl.sjtek.client.android.api;

/**
 * Created by Wouter Habets on 21-10-15.
 */
public enum Action {
    REFRESH("/info"),
    SWITCH("/switch");

    public static final String API_BASE = "https://sjtek.nl/api";
    private final String url;

    Action(String urlAction) {
        this.url = API_BASE + urlAction;
    }

    @Override
    public String toString() {
        return url;
    }


    public enum Light {
        TOGGLE_1("/toggle1"),
        TOGGLE_1_ON("/toggle1on"),
        TOGGLE_1_OFF("/toggle1off"),
        TOGGLE_2("/toggle2"),
        TOGGLE_2_ON("/toggle2on"),
        TOGGLE_2_OFF("/toggle2off"),
        TOGGLE_3("/toggle3"),
        TOGGLE_3_ON("/toggle3on"),
        TOGGLE_3_OFF("/toggle3off");

        private static final String BASE = API_BASE + "/lights";
        private final String url;

        Light(String urlAction) {
            this.url = BASE + urlAction;
        }


        @Override
        public String toString() {
            return url;
        }
    }

    public enum Music {
        TOGGLE("/toggle"),
        PLAY("/play"),
        PAUSE("/pause"),
        STOP("/stop"),
        NEXT("/next"),
        PREVIOUS("/previous"),
        SHUFFLE("/shuffle"),
        CLEAR("/clear"),
        VOLUME_LOWER("/volumelower"),
        VOLUME_RAISE("/volumeraise"),
        VOLUME_NEUTRAL("/volumeneutral");

        private static final String BASE = API_BASE + "/music";
        private final String url;

        Music(String urlAction) {
            this.url = BASE + urlAction;
        }


        @Override
        public String toString() {
            return url;
        }
    }

    public enum TV {
        POWER_OFF("/off"),
        VOLUME_LOWER("/volumelower"),
        VOLUME_RAISE("/volumeraise");


        private static final String BASE = API_BASE + "/tv";
        private final String url;

        TV(String urlAction) {
            this.url = BASE + urlAction;
        }


        @Override
        public String toString() {
            return url;
        }
    }
}
