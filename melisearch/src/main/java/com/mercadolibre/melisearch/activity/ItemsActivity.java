package com.mercadolibre.melisearch.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.fragment.ItemDetailsFragment;
import com.mercadolibre.melisearch.fragment.SearchItemsFragment;

public class ItemsActivity extends AbstractActivity implements SearchItemsFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        MenuItem searchItem = menu.findItem(R.id.items_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        return true;
    }

    private void search(String query) {
        getSupportFragmentManager().beginTransaction().replace(R.id.template_container, SearchItemsFragment.newInstance(query.trim())).addToBackStack(null).commit();
    }

    @Override
    public void onSelectedItem(String itemId) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.template_container, ItemDetailsFragment.newInstance(itemId)).addToBackStack(null).commit();
    }
}
