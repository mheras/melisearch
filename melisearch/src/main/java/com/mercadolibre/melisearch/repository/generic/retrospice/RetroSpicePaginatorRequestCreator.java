package com.mercadolibre.melisearch.repository.generic.retrospice;

import com.mercadolibre.melisearch.request.generic.RetroSpicePaginatorRequest;

/**
 * Created by Martin A. Heras on 13/02/14.
 */
public interface RetroSpicePaginatorRequestCreator<ResponseType extends PaginatedResponse<?>, RetrofitAPIType> {

    public RetroSpicePaginatorRequest<ResponseType, RetrofitAPIType> createPaginatorRequest(int offset);

}
