package com.github.andrei1993ak.finances.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.activities.CategoryStartingActivity;
import com.github.andrei1993ak.finances.app.activities.CostActivity;
import com.github.andrei1993ak.finances.app.activities.CurrencyActivity;
import com.github.andrei1993ak.finances.app.activities.IncomeActivity;
import com.github.andrei1993ak.finances.app.activities.ReportsActivity;
import com.github.andrei1993ak.finances.app.activities.SettingsActivity;
import com.github.andrei1993ak.finances.app.activities.TransferActivity;
import com.github.andrei1993ak.finances.app.activities.WalletActivity;
import com.github.andrei1993ak.finances.control.adapters.WalletsRecycleViewAdapter;
import com.github.andrei1993ak.finances.control.loaders.WalletCursorLoader;
import com.github.andrei1993ak.finances.signinByAppEngine.SignInActivity;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.RoundedImageView;
import com.github.andrei1993ak.finances.util.universalLoader.SignInImageLoader;

public class StartingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int REQUEST_CODE_SETTING = 0;
    public static final int REQUEST_CODE_AUTH = 1;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setStyle();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                return onNavigationMenuSelected(item.getItemId());
            }
        });
        checkForAuthorisation();
        getSupportLoaderManager().restartLoader(Constants.MAIN_LOADER_ID, null, this);
    }

    private void setStyle() {
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.THEME, false)) {
            setTheme(R.style.DarkNoActionBar);
        } else {
            setTheme(R.style.LightNoActionBar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((DrawerLayout) findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Loader<Cursor> loader = getSupportLoaderManager().getLoader(Constants.MAIN_LOADER_ID);

        if (loader != null) {
            loader.forceLoad();
        }
    }

    public void onSelectedCategoryViewClick(final View view) {
        switch (view.getId()) {
            case R.id.tvCurrency:
                startActivity(new Intent(this, CurrencyActivity.class));

                break;
            case R.id.tvWallet:
                startActivity(new Intent(this, WalletActivity.class));

                break;
            case R.id.tvCategories:
                startActivity(new Intent(this, CategoryStartingActivity.class));

                break;
            case R.id.tvIncomes:
                startActivity(new Intent(this, IncomeActivity.class));

                break;
            case R.id.tvCosts:
                startActivity(new Intent(this, CostActivity.class));

                break;
            case R.id.tvTransfers:
                startActivity(new Intent(this, TransferActivity.class));

                break;
            case R.id.tvReports:
                startActivity(new Intent(this, ReportsActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_SETTING) {
            finish();
            startActivity(new Intent(StartingActivity.this, StartingActivity.class));
        } else if (requestCode == REQUEST_CODE_AUTH && resultCode == RESULT_OK) {
            checkForAuthorisation();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new WalletCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        final WalletsRecycleViewAdapter adapter = new WalletsRecycleViewAdapter(data, this);
        recyclerView.swapAdapter(adapter, true);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        recyclerView.swapAdapter(null, true);
    }

    private boolean onNavigationMenuSelected(final int itemId) {
        switch (itemId) {
            case R.id.nav_manage:
                startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_CODE_SETTING);
                break;
            case R.id.nav_share:
                final Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.LINK_TO_DONLOAD);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
            case R.id.nav_login:
                startActivityForResult(new Intent(this, SignInActivity.class), REQUEST_CODE_AUTH);
                break;
        }
        return true;
    }

    private void checkForAuthorisation() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final View headerView = navigationView.getHeaderView(0);
        final RoundedImageView photo = (RoundedImageView) headerView.findViewById(R.id.google_acc_photo);
        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constants.IS_LOGIN, false)) {
            ((TextView) headerView.findViewById(R.id.google_acc_name)).setText(sharedPreferences.getString(Constants.GOOGLE_ACC_NAME, ""));
            final String photoUri = sharedPreferences.getString(Constants.USER_PHOTO_URI, "null");
            if (!photoUri.equals("null")) {
                new SignInImageLoader(this).load(photoUri, photo);
            }
        }
    }
}
