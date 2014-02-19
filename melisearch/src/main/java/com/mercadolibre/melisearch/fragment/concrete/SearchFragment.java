package com.mercadolibre.melisearch.fragment.concrete;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.fragment.generic.SpiceFragment;

/**
 * Created by Martin A. Heras on 10/02/14.
 */
public class SearchFragment extends SpiceFragment {

    public static interface Listener {
        public void onSearchSubmitted(String searchCriteria);
    }

    private Listener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        Button searchButton = (Button) view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText searchEditText = (EditText) view.findViewById(R.id.search_edit_text);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                mListener.onSearchSubmitted(searchEditText.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
