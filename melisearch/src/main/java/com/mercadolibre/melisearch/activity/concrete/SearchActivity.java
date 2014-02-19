package com.mercadolibre.melisearch.activity.concrete;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.activity.generic.SpiceActivity;
import com.mercadolibre.melisearch.fragment.concrete.SearchFragment;

public class SearchActivity extends SpiceActivity implements SearchFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_container);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.template_container, new SearchFragment()).commit();
        }
    }

    @Override
    public void onSearchSubmitted(String searchCriteria) {

        searchCriteria = searchCriteria.trim();

        if (searchCriteria.isEmpty()) {
            Toast.makeText(this, R.string.empty_search_criteria, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(SearchResultsActivity.SEARCH_CRITERIA, searchCriteria);
        startActivity(intent);
    }
}
