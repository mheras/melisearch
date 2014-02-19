package com.mercadolibre.melisearch.repository.generic;

/**
 * Created by Martin A. Heras on 11/02/14.
 */
public interface Repository<ObjectType, ObjectIdType> {

    // **********************************************
    // FIND
    // **********************************************

    public static interface FindCallbacks<FindCallbacksObjectType, FindCallbacksObjectIdType> {

        public void onFindSuccess(FindCallbacksObjectIdType id, FindCallbacksObjectType object);

        public void onFindFail(FindCallbacksObjectIdType id);
    }

    public void setFindCallbacks(FindCallbacks<ObjectType, ObjectIdType> findCallbacks);

    public void find(ObjectIdType id);

    // **********************************************
    // SEARCH
    // **********************************************

    public Paginator<ObjectType> search(String query);

}
