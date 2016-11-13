package nl.sjtek.client.android.fragments;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentGrid {

    private final List<Row> rows = new ArrayList<>();

    public FragmentGrid(Row... rows) {
        Collections.addAll(this.rows, rows);
    }

    public Fragment get(int row, int column) {
        return rows.get(row).getFragment(column);
    }

    public int getRowsCount() {
        return rows.size();
    }

    public int getColumnCount(int row) {
        return rows.get(row).getColumnCount();
    }

    public static class Row {
        private final List<Fragment> columns = new ArrayList<>();

        public Row(Fragment... fragments) {
            Collections.addAll(columns, fragments);
        }

        Fragment getFragment(int column) {
            return columns.get(column);
        }

        int getColumnCount() {
            return columns.size();
        }
    }
}
