package com.mercadolibre.melisearch.request.concrete;

import com.mercadolibre.melisearch.api.ItemAPI;
import com.mercadolibre.melisearch.model.Item;
import com.mercadolibre.melisearch.request.generic.RetroSpiceRequest;
import com.octo.android.robospice.persistence.DurationInMillis;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public class FindItemRequest extends RetroSpiceRequest<Item, ItemAPI> {

    private String mId;

    public FindItemRequest(String id) {
        super(Item.class, ItemAPI.class);
        mId = id;
    }

    @Override
    public Item loadDataFromNetwork() throws Exception {
        return getService().find(mId);
    }

    @Override
    protected String getUniqueCacheKey() {
        return mId;
    }

    @Override
    public long getCacheExpiryDurationInMilliseconds() {
        // TODO: Remove this method
        return DurationInMillis.ALWAYS_EXPIRED;
    }
}
