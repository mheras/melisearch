package com.mercadolibre.melisearch.activity.concrete;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.activity.generic.SpiceActivity;
import com.mercadolibre.melisearch.fragment.concrete.DetailsFragment;
import com.mercadolibre.melisearch.fragment.concrete.SearchResultsFragment;

public class SearchResultsActivity extends SpiceActivity implements SearchResultsFragment.Listener {

    public static final String SEARCH_CRITERIA = "com.mercadolibre.melisearch.SearchResultsActivity.SEARCH_CRITERIA";

    private View mDetailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.search_results_container, SearchResultsFragment.newInstance(getIntent().getStringExtra(SEARCH_CRITERIA))).commit();
        }

        mDetailsContainer = findViewById(R.id.details_container);
        if (mDetailsContainer != null) {
            getFragmentManager().beginTransaction().replace(R.id.details_container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public void onSelectedItem(String itemId) {

        if (mDetailsContainer == null) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.ITEM_ID, itemId);
            startActivity(intent);
        } else {
            getFragmentManager().beginTransaction().replace(R.id.details_container, DetailsFragment.newInstance(itemId)).commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_details_placeholder, container, false);
        }
    }
}
