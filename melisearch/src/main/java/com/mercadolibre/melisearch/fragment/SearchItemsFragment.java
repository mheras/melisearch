package com.mercadolibre.melisearch.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

        // Create a paginator to search for items.
        if (mItemPaginator == null) {
            mItemPaginator = new ItemRepository(mSpiceManager).search(getArguments().getString(SEARCH_CRITERIA));
        }

        // Set the callbacks.
        mItemPaginator.setPaginatorCallbacks(this);

        if (savedInstanceState == null) {
            if (mItemPaginator.getTotalCount() == Paginator.TOTAL_COUNT_UNKNOWN) {

                // Make progress bar container appear.
                View progressBarContainer = getView().findViewById(R.id.search_progress_bar_container);
                progressBarContainer.setVisibility(View.VISIBLE);

                // Load.
                mItemPaginator.loadNextPage();
            }
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

        // Add footer for dynamic loading.
        addLoadingFooter(inflater, mItemsListView);

        // Setup list view listeners.
        mItemsListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private boolean mScrolled;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    mScrolled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (mScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {
                    mItemPaginator.loadNextPage();
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

        updateSearchResultsCount(getView());

        // Make progress bar container appear.
        View progressBarContainer = getView().findViewById(R.id.search_progress_bar_container);
        progressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void onPageLoadFail() {
        // TODO: Implement this method.
    }
}
