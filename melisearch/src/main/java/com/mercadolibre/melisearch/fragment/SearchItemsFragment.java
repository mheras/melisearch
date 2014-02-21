package com.mercadolibre.melisearch.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.adapter.ItemsAdapter;
import com.mercadolibre.melisearch.model.Item;
import com.mercadolibre.melisearch.repository.concrete.ItemRepository;
import com.mercadolibre.melisearch.repository.generic.Paginator;

import java.util.List;

/**
 * Created by Martin A. Heras on 21/02/14.
 */
public class SearchItemsFragment extends AbstractFragment implements Paginator.Callbacks<Item> {

    public static interface Listener {
        public void onSelectedItem(String itemId);
    }

    private final static String SEARCH_CRITERIA = "SEARCH_CRITERIA";

    private ItemRepository mItemRepository;
    private Paginator<Item> mItemPaginator;

    private ListView mItemsListView;
    private View mListViewFooter;

    private Listener mListener;
    private ItemsAdapter mItemsAdapter;

    public static SearchItemsFragment newInstance(String searchCriteria) {
        Bundle args = new Bundle();
        args.putString(SEARCH_CRITERIA, searchCriteria);
        SearchItemsFragment fragment = new SearchItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement SearchItemsFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create the repository.
        mItemRepository = new ItemRepository(mSpiceManager);

        // Create a paginator to search for items.
        mItemPaginator = mItemRepository.search(getArguments().getString(SEARCH_CRITERIA));

        // Set the callbacks.
        mItemPaginator.setPaginatorCallbacks(this);

        if (savedInstanceState == null) {
            loadNextPage();
        } else {
            mItemPaginator.restoreInstanceState(savedInstanceState);
            updateSearchResultsCount(getView());
        }

        // Set adapter.
        mItemsAdapter = new ItemsAdapter(getActivity(), mItemPaginator.getLoadedObjects());
        mItemsListView.setAdapter(mItemsAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mItemPaginator.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void addLoadingFooter(LayoutInflater inflater, ListView itemsListView) {
        mListViewFooter = inflater.inflate(R.layout.list_view_footer, null);
        itemsListView.addFooterView(mListViewFooter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_items, container, false);

        mItemsListView = (ListView) view.findViewById(R.id.items_list_view);

        if (savedInstanceState == null) {

            // Make list view invisible.
            mItemsListView.setVisibility(View.INVISIBLE);

            // Make the search results count text view invisible as well...
            TextView searchResultsCountTextView = (TextView) view.findViewById(R.id.search_results_count_text_view);
            searchResultsCountTextView.setVisibility(View.INVISIBLE);

        } else {
            // Make the progress bar invisible.
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.search_progress_bar);
            progressBar.setVisibility(View.INVISIBLE);
        }

        // Add footer for dynamic loading.
        addLoadingFooter(inflater, mItemsListView);

        // Setup list view listeners.
        mItemsListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Do nothing...
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (mItemPaginator != null && firstVisibleItem + visibleItemCount == totalItemCount) {
                    loadNextPage();
                }
            }
        });

        mItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mListener != null) {
                    Item item = mItemPaginator.getLoadedObjects().get(position);
                    mListener.onSelectedItem(item.getId());
                }
            }
        });

        return view;
    }

    private void loadNextPage() {

        if (mItemPaginator.hasMorePages() && !mItemPaginator.isLoadingPage()) {

            // Check connectivity.
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (mListViewFooter != null) {
                    mListViewFooter.findViewById(R.id.list_view_footer_progress_bar).setVisibility(View.VISIBLE);
                }

                mItemPaginator.loadNextPage();
            } else {
                Toast.makeText(getActivity(), R.string.no_connectivity, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateSearchResultsCount(View rootView) {
        TextView searchResultsCountTextView = (TextView) rootView.findViewById(R.id.search_results_count_text_view);
        String resultsCount = String.format(getResources().getString(R.string.search_results_count), mItemPaginator.getTotalCount());
        searchResultsCountTextView.setText(resultsCount);
    }

    @Override
    public void onPageLoadSuccess(Paginator<Item> paginator, List<Item> loadedObjects) {

        // The adapter is already bound to the paginator.
        mItemsAdapter.notifyDataSetChanged();

        if (loadedObjects.size() == paginator.getTotalCount() || paginator.getTotalCount() == 0) {
            mListViewFooter.findViewById(R.id.list_view_footer_progress_bar).setVisibility(View.GONE);
        }

        ListView itemsListView = (ListView) getView().findViewById(R.id.items_list_view);
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.search_progress_bar);
        TextView searchResultsCountTextView = (TextView) getView().findViewById(R.id.search_results_count_text_view);

        updateSearchResultsCount(getView());

        itemsListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        searchResultsCountTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageLoadFail() {
        // TODO: Implement this method.
    }
}
