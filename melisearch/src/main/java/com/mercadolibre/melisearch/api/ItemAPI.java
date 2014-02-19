package com.mercadolibre.melisearch.api;

import com.mercadolibre.melisearch.model.Item;
import com.mercadolibre.melisearch.repository.concrete.ItemPaginatedResponse;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public interface ItemAPI {

    @GET("/sites/{site}/search")
    public ItemPaginatedResponse search(@Path("site") String site, @Query("q") String query, @Query("offset") int offset, @Query("limit") int limit);

    @GET("/items/{id}")
    public Item find(@Path("id") String id);

}
