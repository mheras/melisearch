package com.mercadolibre.melisearch.repository.generic.retrospice;

/**
 * Created by Martin A. Heras on 05/02/14.
 */
public class PagingInfo {

    private int mTotal;
    private int mOffset;
    private int mLimit;

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        this.mTotal = total;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        this.mLimit = limit;
    }
}
