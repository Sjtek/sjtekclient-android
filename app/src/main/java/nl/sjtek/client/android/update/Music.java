package nl.sjtek.client.android.update;

import org.json.JSONException;
import org.json.JSONObject;

public class Music {

    private final PlayerStatus status;
    private final int volume;
    private final Song song;
    public Music(JSONObject jsonObject) throws JSONException {
        this.status = PlayerStatus.valueOf(jsonObject.getString("state"));
        this.volume = jsonObject.getInt("volume");
        this.song = new Song(jsonObject.getJSONObject("song"));
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public int getVolume() {
        return volume;
    }

    public Song getSong() {
        return song;
    }

    public enum PlayerStatus {
        STATUS_PLAYING,
        STATUS_PAUSED,
        STATUS_STOPPED,
        ERROR

    }

    public class Song {
        private final String artist;
        private final String album;
        private final String title;
        private final int elapsed;
        private final int total;

        public Song(JSONObject jsonObject) throws JSONException {
            this.artist = jsonObject.getString("artist");
            this.album = jsonObject.getString("album");
            this.title = jsonObject.getString("title");
            this.elapsed = jsonObject.getInt("elapsed");
            this.total = jsonObject.getInt("total");
        }

        public String getArtist() {
            return artist;
        }

        public String getAlbum() {
            return album;
        }

        public String getTitle() {
            return title;
        }

        public int getElapsed() {
            return elapsed;
        }

        public int getTotal() {
            return total;
        }
    }
}