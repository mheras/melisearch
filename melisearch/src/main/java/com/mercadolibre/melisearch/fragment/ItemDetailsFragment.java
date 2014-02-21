package com.mercadolibre.melisearch.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.model.Item;
import com.mercadolibre.melisearch.repository.concrete.ItemRepository;
import com.mercadolibre.melisearch.repository.generic.Repository;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by Martin A. Heras on 21/02/14.
 */
public class ItemDetailsFragment extends AbstractFragment implements Repository.FindCallbacks<Item, String>, ViewPager.OnPageChangeListener {

    private static final String ITEM_ID = "ITEM_ID";
    private static final String ITEM_INSTANCE_STATE_KEY = "item";
    private ItemRepository mItemRepository;
    private Item mItem;
    private PicturesAdapter mPicturesAdapter;
    private ViewPager mPicturesViewPager;

    public static ItemDetailsFragment newInstance(String itemId) {
        Bundle args = new Bundle();
        args.putString(ITEM_ID, itemId);
        ItemDetailsFragment fragment = new ItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mItemRepository = new ItemRepository(mSpiceManager);
        mItemRepository.setFindCallbacks(this);

        mPicturesViewPager = (ViewPager) getView().findViewById(R.id.details_pictures_view_pager);
        mPicturesViewPager.setOnPageChangeListener(this);

        mPicturesAdapter = new PicturesAdapter();
        mPicturesViewPager.setAdapter(mPicturesAdapter);

        if (savedInstanceState == null) {
            String itemId = getArguments().getString(ITEM_ID);
            mItemRepository.find(itemId);
        } else {
            mItem = (Item) savedInstanceState.getSerializable(ITEM_INSTANCE_STATE_KEY);
            updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ITEM_INSTANCE_STATE_KEY, mItem);
        super.onSaveInstanceState(outState);
    }

    private void updateUI() {
        if (mItem != null) {
            // Get all views.
            TextView titleTextView = (TextView) getView().findViewById(R.id.details_title_text_view);

            // Update views.
            titleTextView.setText(mItem.getTitle());

            mPicturesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_details, container, false);
    }

    @Override
    public void onFindSuccess(String id, Item item) {
        mItem = item;
        updateUI();
    }

    @Override
    public void onFindFail(String id) {
        // TODO: Implement this method.
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPicturesViewPager.getParent().requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class PicturesAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mItem != null && mItem.getPictures() != null) {
                return mItem.getPictures().size();
            }

            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView imageView = new ImageView(getActivity());
            container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            imageView.post(new Runnable() {
                @Override
                public void run() {
                    URL pictureUrl = mItem.getPictures().get(position);
                    Picasso.with(getActivity()).load(pictureUrl != null ? pictureUrl.toString() : null).placeholder(R.drawable.placeholder).resize(imageView.getWidth(), imageView.getHeight()).centerCrop().into(imageView);
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }
}
