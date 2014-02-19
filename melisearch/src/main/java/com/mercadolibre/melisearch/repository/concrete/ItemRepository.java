package com.mercadolibre.melisearch.repository.concrete;

import com.mercadolibre.melisearch.api.ItemAPI;
import com.mercadolibre.melisearch.model.Item;
import com.mercadolibre.melisearch.repository.generic.retrospice.RetroSpicePaginatorRequestCreator;
import com.mercadolibre.melisearch.repository.generic.retrospice.RetroSpiceRepository;
import com.mercadolibre.melisearch.request.concrete.FindItemRequest;
import com.mercadolibre.melisearch.request.concrete.SearchItemRequest;
import com.mercadolibre.melisearch.request.generic.RetroSpicePaginatorRequest;
import com.mercadolibre.melisearch.request.generic.RetroSpiceRequest;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public class ItemRepository extends RetroSpiceRepository<Item, String, ItemAPI> {

    public ItemRepository(SpiceManager spiceManager) {
        super(spiceManager);
    }

    @Override
    protected RetroSpiceRequest<Item, ItemAPI> createFindRequest(String id) {
        return new FindItemRequest(id);
    }

    @Override
    protected RetroSpicePaginatorRequestCreator<ItemPaginatedResponse, ItemAPI> createSearchPaginatorRequestCreator(final String query) {
        return new RetroSpicePaginatorRequestCreator<ItemPaginatedResponse, ItemAPI>() {

            private static final int SEARCH_PAGE_SIZE = 10;

            @Override
            public RetroSpicePaginatorRequest<ItemPaginatedResponse, ItemAPI> createPaginatorRequest(int offset) {
                return new SearchItemRequest(query, offset, SEARCH_PAGE_SIZE);
            }
        };
    }
}
