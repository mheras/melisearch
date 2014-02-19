package com.mercadolibre.melisearch.repository.generic.retrospice;

import java.util.List;

/**
 * Created by Martin A. Heras on 13/02/14.
 */
public abstract class PaginatedResponse<ObjectType> {

    private PagingInfo mPaging;
    private List<ObjectType> mResults;

    public PagingInfo getPaging() {
        return mPaging;
    }

    public void setPaging(PagingInfo mPaging) {
        this.mPaging = mPaging;
    }

    public List<ObjectType> getResults() {
        return mResults;
    }

    public void setResults(List<ObjectType> mResults) {
        this.mResults = mResults;
    }

}
