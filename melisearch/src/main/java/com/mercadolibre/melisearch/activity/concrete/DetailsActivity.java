package com.mercadolibre.melisearch.activity.concrete;

import android.os.Bundle;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.activity.generic.SpiceActivity;
import com.mercadolibre.melisearch.fragment.concrete.DetailsFragment;

public class DetailsActivity extends SpiceActivity {

    public static final String ITEM_ID = "com.mercadolibre.melisearch.DetailsActivity.ITEM_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_container);

        if (savedInstanceState == null) {
            String itemId = getIntent().getStringExtra(ITEM_ID);
            DetailsFragment fragment = DetailsFragment.newInstance(itemId);
            getFragmentManager().beginTransaction().add(R.id.template_container, fragment).commit();
        }
    }
}
