package com.github.andrei1993ak.finances.app.reportsActivities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.adapters.PieChartItemAdapter;
import com.github.andrei1993ak.finances.control.loaders.PieReportLoader;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;
import com.github.andrei1993ak.finances.util.Constants;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

public class PieChartActivityNext extends BaseActivity implements LoaderManager.LoaderCallbacks<ArrayList<PieChartItem>> {

    public static final int MAIN_LOADER = 0;
    private GraphicalView mChartView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_income_pie_next);
        if (getIntent().getBooleanExtra(Constants.PIE_CHART_TYPE, false)) {
            setTitle(R.string.incomesByCategories);
        } else {
            setTitle(R.string.costsByCategories);
        }
        final Bundle args = new Bundle();
        args.putLong(Income.WALLET_ID, getIntent().getLongExtra(Income.WALLET_ID, -1));
        args.putLong(Income.CATEGORY_ID, getIntent().getLongExtra(Income.CATEGORY_ID, -1));
        args.putBoolean(Constants.PIE_CHART_TYPE, getIntent().getBooleanExtra(Constants.PIE_CHART_TYPE, false));
        getSupportLoaderManager().restartLoader(MAIN_LOADER, args, this);
        getSupportLoaderManager().getLoader(MAIN_LOADER).forceLoad();
    }

    public GraphicalView buildView(final String[] bars, final Double[] values) {
        final int[] colors = getBaseContext().getResources().getIntArray(R.array.pieChartColors);
        final CategorySeries series = new CategorySeries("Pie Chart");
        final DefaultRenderer dr = new DefaultRenderer();
        for (int v = 0; v < bars.length; v++) {
            series.add(bars[v], values[v]);
            final SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[v % colors.length]);
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
        final ListView listView = (ListView) findViewById(R.id.pieListView_next);
        final PieChartItemAdapter adapter = new PieChartItemAdapter(this, data);
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
