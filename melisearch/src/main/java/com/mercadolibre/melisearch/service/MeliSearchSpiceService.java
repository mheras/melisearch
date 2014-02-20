package com.mercadolibre.melisearch.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.octo.android.robospice.retrofit.RetrofitJackson2SpiceService;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public class MeliSearchSpiceService extends RetrofitJackson2SpiceService implements RequestInterceptor {

    private static final String BASE_URL = "https://mobile.mercadolibre.com.ar";

    @Override
    protected Converter createConverter() {

        // Custom Jackson object mapper.
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.USE_ANNOTATIONS, false);

        return new JacksonConverter(mapper);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return super.createRestAdapterBuilder().setRequestInterceptor(this);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

    @Override
    public void intercept(RequestFacade request) {
        // Add HTTP headers to all requests here.
    }
}
