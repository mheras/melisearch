package com.mercadolibre.melisearch.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.fragment.ItemDetailsFragment;
import com.mercadolibre.melisearch.fragment.LogoFragment;
import com.mercadolibre.melisearch.fragment.SearchItemsFragment;

public class ItemsActivity extends AbstractActivity implements SearchItemsFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.template_container, new LogoFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        final MenuItem searchItem = menu.findItem(R.id.items_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MenuItemCompat.collapseActionView(searchItem);
                search(query);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.template_container, SearchItemsFragment.newInstance(query.trim()));
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSelectedItem(String itemId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.template_container, ItemDetailsFragment.newInstance(itemId));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
