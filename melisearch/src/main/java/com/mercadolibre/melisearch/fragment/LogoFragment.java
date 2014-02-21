package com.mercadolibre.melisearch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mercadolibre.melisearch.R;

/**
 * Created by Martin A. Heras on 21/02/14.
 */
public class LogoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logo, container, false);
    }
}
