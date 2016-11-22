package nl.sjtek.client.android.fragments;

/**
 * Fragment for showing the Transmission web interface.
 */
public class FragmentTransmission extends BaseFragmentWeb {
    @Override
    protected String getUrl() {
        return "https://sjtek.nl/transmission";
    }
}
