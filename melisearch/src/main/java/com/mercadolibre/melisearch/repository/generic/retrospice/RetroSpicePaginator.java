package com.mercadolibre.melisearch.repository.generic.retrospice;

import android.os.Bundle;
import android.util.Log;

import com.mercadolibre.melisearch.repository.generic.Paginator;
import com.mercadolibre.melisearch.request.generic.RetroSpicePaginatorRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin A. Heras on 13/02/14.
 */
public class RetroSpicePaginator<ObjectType, RetrofitAPIType> implements Paginator<ObjectType> {

    // TODO: We should think how to apply the cache duration based on the activity lifecycle and configuration changes... For the sake of this mini-project, we simply use the same duration over and over again.
    private static final long CACHE_EXPIRY_DURATION = DurationInMillis.ONE_MINUTE;

    public static class RetroSpicePaginatorException extends RuntimeException {
        public RetroSpicePaginatorException(Throwable throwable) {
            super(throwable);
        }

        public RetroSpicePaginatorException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public RetroSpicePaginatorException(String detailMessage) {
            super(detailMessage);
        }

        public RetroSpicePaginatorException() {
        }
    }

    private static final String LOADED_OBJECTS_KEY = "loadedObjects";
    private static final String TOTAL_COUNT_KEY = "totalCount";

    // Spice manager.
    private SpiceManager mSpiceManager;

    // Instance state key to save and restore state with the given bundle.
    private String mInstanceStateKey;

    // Loaded objects so far.
    private List<ObjectType> mLoadedObjects;

    // Total count of objects, including objects not already loaded.
    private int mTotalCount;

    // Whether or not the paginator is currently loading a page.
    private boolean mLoadingPage;

    // Weak reference to the callbacks listener.
    private WeakReference<Paginator.Callbacks<ObjectType>> mPaginatorCallbacksWeakRef;

    // Requests creator.
    private RetroSpicePaginatorRequestCreator<? extends PaginatedResponse<ObjectType>, RetrofitAPIType> mPaginatorRequestCreator;

    /**
     * Creates a new {@link com.mercadolibre.melisearch.repository.generic.Paginator} that implements the behavior to handle RoboSpice and Retrofit to make all the HTTP requests.
     *
     * @param instanceStateKey        A unique key to use when saving and restoring the instance state.
     * @param spiceManager            An instance of {@link com.octo.android.robospice.SpiceManager}.
     * @param paginatorRequestCreator An instance of {@link com.mercadolibre.melisearch.repository.generic.retrospice.RetroSpicePaginatorRequestCreator} that creates {@link com.mercadolibre.melisearch.request.generic.RetroSpicePaginatorRequest} instances for the paginator.
     */
    public RetroSpicePaginator(String instanceStateKey, SpiceManager spiceManager, RetroSpicePaginatorRequestCreator<? extends PaginatedResponse<ObjectType>, RetrofitAPIType> paginatorRequestCreator) {

        mPaginatorRequestCreator = paginatorRequestCreator;
        mSpiceManager = spiceManager;
        mInstanceStateKey = instanceStateKey;

        reset();
    }

    @Override
    public List<ObjectType> getLoadedObjects() {
        return mLoadedObjects;
    }

    @Override
    public int getTotalCount() {
        return mTotalCount;
    }

    @Override
    public boolean hasMorePages() {
        return mTotalCount == TOTAL_COUNT_UNKNOWN || mLoadedObjects.size() < mTotalCount;
    }

    @Override
    public boolean isLoadingPage() {
        return mLoadingPage;
    }

    @Override
    public synchronized void loadNextPage() {

        RetroSpicePaginatorRequest<? extends PaginatedResponse<ObjectType>, RetrofitAPIType> request = mPaginatorRequestCreator.createPaginatorRequest(mLoadedObjects.size());
        if (request == null) {
            throw new NullPointerException(getClass().getName() + " does not support the 'loadNextPage' operation. Implementation of 'createPaginatorRequest' method cannot return null.");
        }

        if (isLoadingPage()) {
            return;
        }

        mLoadingPage = true;
        mSpiceManager.execute(request, request.createCacheKey(), CACHE_EXPIRY_DURATION, new PaginatorListener(this));
    }

    @Override
    public void setPaginatorCallbacks(Paginator.Callbacks<ObjectType> paginatorCallbacks) {
        mPaginatorCallbacksWeakRef = new WeakReference<Paginator.Callbacks<ObjectType>>(paginatorCallbacks);
    }

    @Override
    public void reset() {

        if (isLoadingPage()) {
            throw new RetroSpicePaginatorException("Cannot reset the paginator while it is loading a page.");
        }

        mLoadedObjects = new ArrayList<ObjectType>();
        mTotalCount = TOTAL_COUNT_UNKNOWN;
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        outState.putSerializable(new StringBuilder(mInstanceStateKey).append('#').append(LOADED_OBJECTS_KEY).toString(), (Serializable) mLoadedObjects);
        outState.putInt(new StringBuilder(mInstanceStateKey).append('#').append(TOTAL_COUNT_KEY).toString(), mTotalCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreInstanceState(Bundle savedInstanceState) {
        mLoadedObjects = (List<ObjectType>) savedInstanceState.getSerializable(new StringBuilder(mInstanceStateKey).append('#').append(LOADED_OBJECTS_KEY).toString());
        mTotalCount = savedInstanceState.getInt(new StringBuilder(mInstanceStateKey).append('#').append(TOTAL_COUNT_KEY).toString());
    }

    private class PaginatorListener<PaginatedResponseType extends PaginatedResponse<ObjectType>> implements RequestListener<PaginatedResponseType> {

        private Paginator<ObjectType> mPaginator;

        public PaginatorListener(Paginator<ObjectType> paginator) {
            mPaginator = paginator;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(getClass().getName(), "Page loading failed");

            // Update state.
            mLoadingPage = false;

            // TODO: Implement this!
        }

        @Override
        public void onRequestSuccess(PaginatedResponseType paginatedResponse) {

            // Update state.
            mLoadingPage = false;

            // Add page the loaded objects.
            mLoadedObjects.addAll(paginatedResponse.getResults());
            // Refresh total count.
            mTotalCount = paginatedResponse.getPaging().getTotal();

            Log.d(getClass().getName(), "Page loading succeeded");
            if (mPaginatorCallbacksWeakRef != null) {
                Paginator.Callbacks<ObjectType> callbacks = mPaginatorCallbacksWeakRef.get();
                if (callbacks != null) {
                    callbacks.onPageLoadSuccess(mPaginator, paginatedResponse.getResults());
                }
            }
        }
    }
}
