package com.mercadolibre.melisearch.request.generic;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public abstract class RetroSpiceRequest<ResponseType, RetrofitAPIType> extends RetrofitSpiceRequest<ResponseType, RetrofitAPIType> {

    public static final long DEFAULT_CACHE_EXPIRY_DURATION = 5 * DurationInMillis.ONE_MINUTE;

    public RetroSpiceRequest(Class<ResponseType> clazz, Class<RetrofitAPIType> retrofitedInterfaceClass) {
        super(clazz, retrofitedInterfaceClass);
    }

    public String createCacheKey() {
        return new StringBuilder().append(getClass().getCanonicalName()).append("##").append(getUniqueCacheKey()).toString();
    }

    protected abstract String getUniqueCacheKey();

    public long getCacheExpiryDurationInMilliseconds() {
        return DEFAULT_CACHE_EXPIRY_DURATION;
    }
}
