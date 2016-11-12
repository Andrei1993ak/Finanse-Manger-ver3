package com.gmail.a93ak.andrei19.finance30.view.reports;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PieChartItemAdapter;
import com.gmail.a93ak.andrei19.finance30.control.loaders.PieReportLoader;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.PieChartItem;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

public class PieChartActivityNext extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<PieChartItem>> {

    public static final int MAIN_LOADER = 0;
    private ListView listView;
    private AppCompatSpinner spinner;
    private PieChartItemAdapter adapter;
    private GraphicalView mChartView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_income_pie_next);
        final Bundle args = new Bundle();
        args.putLong(Income.PURSE_ID, getIntent().getLongExtra(Income.PURSE_ID, -1));
        args.putLong(Income.CATEGORY_ID, getIntent().getLongExtra(Income.CATEGORY_ID, -1));
        args.putBoolean(PieChartItem.TYPE, getIntent().getBooleanExtra(PieChartItem.TYPE, false));
        getSupportLoaderManager().restartLoader(MAIN_LOADER, args, this);
        getSupportLoaderManager().getLoader(MAIN_LOADER).forceLoad();
    }

    public GraphicalView buildView(final String[] bars, final Double[] values) {
        final int[] colors = new int[]{0xFFA5EA8C, 0xFFEAA28C, 0xFF8CA5EA,
                0xFFEAE18C, 0xFFEA8CA4};
        final CategorySeries series = new CategorySeries("Pie Chart");
        final DefaultRenderer dr = new DefaultRenderer();
        for (int v = 0; v < bars.length; v++) {
            series.add(bars[v], values[v]);
            final SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[v % 6]);
            dr.addSeriesRenderer(r);
        }
        dr.setZoomButtonsVisible(false);
        dr.setZoomEnabled(false);
        dr.setPanEnabled(false);
        dr.setShowLegend(false);
        dr.setStartAngle(270);
        dr.setLabelsTextSize(30);
        return ChartFactory.getPieChartView(this, series, dr);
    }

    @Override
    public Loader<ArrayList<PieChartItem>> onCreateLoader(final int id, final Bundle args) {
        return new PieReportLoader(this, args);
    }

    protected void onResume() {
        super.onResume();
        if (mChartView != null) {
            mChartView.repaint();
        }
    }

    @Override
    public void onLoadFinished(final Loader<ArrayList<PieChartItem>> loader, final ArrayList<PieChartItem> data) {
        listView = (ListView) findViewById(R.id.pieListView_next);
        adapter = new PieChartItemAdapter(this, data);
        listView.setAdapter(adapter);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chart_next);
        layout.removeAllViews();
        final String[] names = new String[data.size()];
        final Double[] values = new Double[data.size()];
        int i = 0;
        for (final PieChartItem pieChartItem : data) {
            names[i] = pieChartItem.getCategoryName();
            values[i++] = pieChartItem.getAmount();
        }
        mChartView = buildView(names, values);
        layout.addView(mChartView, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onLoaderReset(final Loader<ArrayList<PieChartItem>> loader) {

    }
}
