package nl.sjtek.client.android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.fragments.FragmentGrid;
import nl.sjtek.client.android.fragments.LightsFragment;
import nl.sjtek.client.android.fragments.MusicFragment;
import nl.sjtek.client.android.fragments.PlaylistFragment;
import nl.sjtek.client.android.fragments.WeatherFragment;

public class MainActivity extends WearableActivity {

    private FragmentGrid grid;

    private GridViewPager viewPager;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = new FragmentGrid(
                new FragmentGrid.Row(new LightsFragment()),
                new FragmentGrid.Row(new MusicFragment(), new PlaylistFragment()),
                new FragmentGrid.Row(new WeatherFragment())
        );

        adapter = new CardAdapter(getFragmentManager());

        viewPager = (GridViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);
    }

    private class CardAdapter extends FragmentGridPagerAdapter {

        CardAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getRowCount() {
            return grid.getRowsCount();
        }

        @Override
        public int getColumnCount(int row) {
            return grid.getColumnCount(row);
        }

        @Override
        public Fragment getFragment(int row, int column) {
            return grid.get(row, column);
        }
    }
}
