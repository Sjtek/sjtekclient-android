package nl.sjtek.client.android.fragments;

/**
 * Fragment for showing the Sonarr web interface.
 */
public class FragmentSonarr extends BaseFragmentWeb {
    @Override
    protected String getUrl() {
        return "https://sjtek.nl/sonarr";
    }
}
