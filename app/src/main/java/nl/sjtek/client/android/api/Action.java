package nl.sjtek.client.android.api;

/**
 * Created by Wouter Habets on 21-10-15.
 */
public enum Action implements ActionInterface {
    REFRESH("/info"),
    SWITCH("/toggle"),
    DATA("/data");

    public static final String API_BASE = "https://sjtek.nl/api";
    private final String url;

    Action(String urlAction) {
        this.url = API_BASE + urlAction;
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public enum Light implements ActionInterface {
        TOGGLE_1("/toggle1"),
        TOGGLE_1_ON("/toggle1on"),
        TOGGLE_1_OFF("/toggle1off"),
        TOGGLE_2("/toggle2"),
        TOGGLE_2_ON("/toggle2on"),
        TOGGLE_2_OFF("/toggle2off"),
        TOGGLE_3("/toggle3"),
        TOGGLE_3_ON("/toggle3on"),
        TOGGLE_3_OFF("/toggle3off"),
        TOGGLE_4("/toggle4"),
        TOGGLE_4_ON("/toggle4on"),
        TOGGLE_4_OFF("/toggle4off");


        private static final String BASE = API_BASE + "/lights";
        private final String url;

        Light(String urlAction) {
            this.url = BASE + urlAction;
        }


        @Override
        public String toString() {
            return url;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

    public enum Music implements ActionInterface {
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
        VOLUME_NEUTRAL("/volumeneutral"),
        INFO("/info?voice"),
        START("/start");

        private static final String BASE = API_BASE + "/music";
        private final String url;

        Music(String urlAction) {
            this.url = BASE + urlAction;
        }


        @Override
        public String toString() {
            return url;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

    public enum TV implements ActionInterface {
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

        @Override
        public String getUrl() {
            return url;
        }
    }

    public enum Coffee implements ActionInterface {
        START("/start");

        private static final String BASE = API_BASE + "/coffee";
        private final String url;

        Coffee(String urlAction) {
            this.url = BASE + urlAction;
        }

        @Override
        public String toString() {
            return url;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }
}
