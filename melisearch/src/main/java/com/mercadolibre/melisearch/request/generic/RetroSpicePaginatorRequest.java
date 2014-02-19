package com.mercadolibre.melisearch.request.generic;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public abstract class RetroSpicePaginatorRequest<ResponseType, RetrofitAPIType> extends RetroSpiceRequest<ResponseType, RetrofitAPIType> {

    protected int mOffset;
    protected int mLimit;

    public RetroSpicePaginatorRequest(Class<ResponseType> responseTypeClass, Class<RetrofitAPIType> retrofitedInterfaceClass, int offset, int limit) {
        super(responseTypeClass, retrofitedInterfaceClass);
        mOffset = offset;
        mLimit = limit;
    }

    @Override
    protected String getUniqueCacheKey() {
        return new StringBuilder().append(mOffset).append("##").append(mLimit).toString();
    }

}
