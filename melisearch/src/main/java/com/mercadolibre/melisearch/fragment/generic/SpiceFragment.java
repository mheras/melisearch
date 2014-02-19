package com.mercadolibre.melisearch.fragment.generic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mercadolibre.melisearch.activity.generic.SpiceActivity;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by Martin A. Heras on 12/02/14.
 */
public abstract class SpiceFragment extends Fragment {

    protected SpiceManager mSpiceManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            SpiceActivity spiceActivity = (SpiceActivity) getActivity();
            mSpiceManager = spiceActivity.getSpiceManager();
        } catch (ClassCastException e) {
            throw new RuntimeException(getActivity().getClass().getSimpleName() + " must extend from " + SpiceActivity.class.getSimpleName() + " in order to attach a " + getClass().getSimpleName() + " into it");
        }
    }
}
