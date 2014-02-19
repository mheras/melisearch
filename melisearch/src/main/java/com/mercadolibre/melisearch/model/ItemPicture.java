package com.mercadolibre.melisearch.model;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by Martin A. Heras on 18/02/14.
 */
public class ItemPicture implements Serializable {

    private URL mUrl;

    public URL getUrl() {
        return mUrl;
    }

    public void setUrl(URL url) {
        this.mUrl = url;
    }
}
