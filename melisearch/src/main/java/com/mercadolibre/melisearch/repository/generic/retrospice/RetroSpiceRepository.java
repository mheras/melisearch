package com.mercadolibre.melisearch.repository.generic.retrospice;

import android.util.Log;

import com.mercadolibre.melisearch.repository.generic.Paginator;
import com.mercadolibre.melisearch.repository.generic.Repository;
import com.mercadolibre.melisearch.request.generic.RetroSpiceRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.lang.ref.WeakReference;

import javax.xml.datatype.Duration;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public abstract class RetroSpiceRepository<ObjectType, ObjectIdType, RetrofitAPIType> implements Repository<ObjectType, ObjectIdType> {

    // TODO: We should think how to apply the cache duration based on the activity lifecycle and configuration changes... For the sake of this mini-project, we simply use the same duration over and over again.
    private static final long CACHE_EXPIRY_DURATION = DurationInMillis.ONE_MINUTE;

    // The SpiceManager that can be set after repository initialization.
    private SpiceManager mSpiceManager;

    // Callbacks.
    private WeakReference<FindCallbacks<ObjectType, ObjectIdType>> mFindCallbacksWeakRef;

    /**
     * Creates a new {@link com.mercadolibre.melisearch.repository.generic.retrospice.RetroSpiceRepository} taking into account a previous state. Useful to maintain state during configuration changes.
     *
     * @param spiceManager An instance of {@link com.octo.android.robospice.SpiceManager}.
     */
    public RetroSpiceRepository(SpiceManager spiceManager) {
        mSpiceManager = spiceManager;
    }

    private void checkSpiceManager() {
        if (mSpiceManager == null) {
            throw new NullPointerException("Cannot perform operation since the SpiceManager is null");
        }
    }

    // **********************************************
    // FIND
    // **********************************************

    @Override
    public void find(ObjectIdType id) {

        checkSpiceManager();

        RetroSpiceRequest<ObjectType, RetrofitAPIType> request = createFindRequest(id);
        if (request == null) {
            throw new NullPointerException(getClass().getSimpleName() + " does not support the 'find' operation. Implementation of 'createFindRequest' method cannot return null.");
        }

        mSpiceManager.execute(request, request.createCacheKey(), CACHE_EXPIRY_DURATION, new FindListener(id));
    }

    @Override
    public void setFindCallbacks(FindCallbacks<ObjectType, ObjectIdType> findCallbacks) {
        mFindCallbacksWeakRef = new WeakReference<FindCallbacks<ObjectType, ObjectIdType>>(findCallbacks);
    }

    protected abstract RetroSpiceRequest<ObjectType, RetrofitAPIType> createFindRequest(ObjectIdType id);

    private class FindListener implements RequestListener<ObjectType> {

        private ObjectIdType mId;

        public FindListener(ObjectIdType id) {
            mId = id;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(getClass().getName(), "Callback onRequestFailure called");
            if (mFindCallbacksWeakRef != null) {
                FindCallbacks<ObjectType, ObjectIdType> findCallbacks = mFindCallbacksWeakRef.get();
                if (findCallbacks != null) {
                    Log.d(getClass().getName(), "Notifying " + FindCallbacks.class.getName());
                    findCallbacks.onFindFail(mId);
                }
            }
        }

        @Override
        public void onRequestSuccess(ObjectType object) {
            Log.d(getClass().getName(), "Callback onRequestSuccess called");
            if (mFindCallbacksWeakRef != null) {
                FindCallbacks<ObjectType, ObjectIdType> findCallbacks = mFindCallbacksWeakRef.get();
                if (findCallbacks != null) {
                    Log.d(getClass().getName(), "Notifying " + FindCallbacks.class.getName());
                    findCallbacks.onFindSuccess(mId, object);
                }
            }
        }
    }

    // **********************************************
    // SEARCH
    // **********************************************

    @Override
    @SuppressWarnings("unchecked")
    public Paginator<ObjectType> search(String query) {

        checkSpiceManager();

        RetroSpicePaginatorRequestCreator<? extends PaginatedResponse<ObjectType>, RetrofitAPIType> searchPaginatorRequestCreator = createSearchPaginatorRequestCreator(query);
        if (searchPaginatorRequestCreator == null) {
            throw new NullPointerException(getClass().getName() + " does not support the 'search' operation. Implementation of 'createSearchPaginatorRequestCreator' method cannot return null.");
        }

        return new RetroSpicePaginator<ObjectType, RetrofitAPIType>(new StringBuilder(getClass().getName()).append('#').append("search").append('#').append(query).toString(), mSpiceManager, searchPaginatorRequestCreator);
    }

    protected abstract RetroSpicePaginatorRequestCreator<? extends PaginatedResponse<ObjectType>, RetrofitAPIType> createSearchPaginatorRequestCreator(final String query);
}
