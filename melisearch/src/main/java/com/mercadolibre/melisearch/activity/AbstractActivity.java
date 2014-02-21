package com.mercadolibre.melisearch.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.mercadolibre.melisearch.service.MeliSearchSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by Martin A. Heras on 21/02/14.
 */
public abstract class AbstractActivity extends ActionBarActivity {

    private final SpiceManager mSpiceManager = new SpiceManager(MeliSearchSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpiceManager.start(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mSpiceManager.shouldStop();
        super.onDestroy();
    }

    public SpiceManager getSpiceManager() {
        return mSpiceManager;
    }
}
