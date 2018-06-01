package com.norestlabs.restlesswallet.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.norestlabs.restlesswallet.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_swap)
public class SwapFragment extends Fragment {

    @ViewById
    Spinner spinnerFrom, spinnerTo;

    ArrayAdapter<String> adapterFrom, adapterTo;

    @AfterViews
    void init() {
        final List<String> list = new ArrayList<>();
        list.add("Bitcoin");
        list.add("Ethereum");
        list.add("OmiseGo");

        adapterFrom = new ArrayAdapter<>(getContext(), R.layout.spinner_item, list);
        spinnerFrom.setAdapter(adapterFrom);

        adapterTo = new ArrayAdapter<>(getContext(), R.layout.spinner_item, list);
        spinnerTo.setAdapter(adapterTo);
    }
}
