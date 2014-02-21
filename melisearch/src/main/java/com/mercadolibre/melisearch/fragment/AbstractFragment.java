package com.mercadolibre.melisearch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mercadolibre.melisearch.activity.AbstractActivity;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by Martin A. Heras on 21/02/14.
 */
public abstract class AbstractFragment extends Fragment {

    protected SpiceManager mSpiceManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            AbstractActivity activity = (AbstractActivity) getActivity();
            mSpiceManager = activity.getSpiceManager();
        } catch (ClassCastException e) {
            throw new RuntimeException(getActivity().getClass().getSimpleName() + " must extend from " + AbstractActivity.class.getSimpleName());
        }
    }

}
