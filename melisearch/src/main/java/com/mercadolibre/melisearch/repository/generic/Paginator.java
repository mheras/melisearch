package com.mercadolibre.melisearch.repository.generic;

import android.os.Bundle;

import java.util.List;

/**
 * Created by Martin A. Heras on 14/02/14.
 */
public interface Paginator<ObjectType> {

    /**
     * Callbacks interface to listen for {@link com.mercadolibre.melisearch.repository.generic.Paginator} events.
     *
     * @param <PaginatorCallbacksObjectType>
     */
    public static interface Callbacks<PaginatorCallbacksObjectType> {

        public void onPageLoadSuccess(Paginator<PaginatorCallbacksObjectType> paginator, List<PaginatorCallbacksObjectType> loadedObjects);

        public void onPageLoadFail();
    }

    public static final int TOTAL_COUNT_UNKNOWN = -1;

    /**
     * Saves the current state in the given bundle.
     *
     * @param outState The {@link android.os.Bundle} in which the state is going to be saved in.
     */
    public void saveInstanceState(Bundle outState);

    /**
     * Restores the state from the given bundle.
     *
     * @param savedInstanceState The {@link android.os.Bundle} from which the state is going to be restored.
     */
    public void restoreInstanceState(Bundle savedInstanceState);

    /**
     * Gets all the loaded objects so far.
     *
     * @return A list of {@link ObjectType} objects.
     */
    public List<ObjectType> getLoadedObjects();

    /**
     * Gets the total count of {@link ObjectType} objects, including those in pages that have not been loaded yet.
     *
     * @return The total count.
     */
    public int getTotalCount();

    /**
     * Loads the next page asynchronously. Implement {@link com.mercadolibre.melisearch.repository.generic.Paginator.Callbacks} to listen for the callback and set it with {@link #setPaginatorCallbacks(com.mercadolibre.melisearch.repository.generic.Paginator.Callbacks)}.
     */
    public void loadNextPage();

    /**
     * Gets whether the paginator has more pages to be loaded.
     *
     * @return A boolean indicating whether there are more pages or not.
     */
    public boolean hasMorePages();

    /**
     * Gets whether the paginator is currently loading a page or not.
     *
     * @return A boolean indicating whether the paginator is currently loading a page or not.
     */
    public boolean isLoadingPage();

    /**
     * Resets the paginator.
     */
    public void reset();

    /**
     * Sets the callbacks listener.
     *
     * @param paginatorCallbacks A {@link com.mercadolibre.melisearch.repository.generic.Paginator.Callbacks} instance.
     */
    public void setPaginatorCallbacks(Callbacks<ObjectType> paginatorCallbacks);
}
