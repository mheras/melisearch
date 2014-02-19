package com.mercadolibre.melisearch.request.concrete;

import com.mercadolibre.melisearch.api.ItemAPI;
import com.mercadolibre.melisearch.repository.concrete.ItemPaginatedResponse;
import com.mercadolibre.melisearch.request.generic.RetroSpicePaginatorRequest;
import com.octo.android.robospice.persistence.DurationInMillis;

/**
 * Created by Martin A. Heras on 13/02/14.
 */
public class SearchItemRequest extends RetroSpicePaginatorRequest<ItemPaginatedResponse, ItemAPI> {

    private String mQuery;

    public SearchItemRequest(final String query, int offset, int limit) {
        super(ItemPaginatedResponse.class, ItemAPI.class, offset, limit);
        this.mQuery = query;
    }

    @Override
    protected String getUniqueCacheKey() {
        return new StringBuilder(super.getUniqueCacheKey()).append("##").append("MLA").append("##").append(mQuery).toString();
    }

    @Override
    public ItemPaginatedResponse loadDataFromNetwork() throws Exception {
        // TODO: Hardcoded site!
        return getService().search("MLA", mQuery, mOffset, mLimit);
    }

    @Override
    public long getCacheExpiryDurationInMilliseconds() {
        return DurationInMillis.ALWAYS_RETURNED;
    }
}
