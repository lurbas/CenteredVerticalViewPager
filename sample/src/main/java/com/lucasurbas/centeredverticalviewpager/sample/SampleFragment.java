package com.lucasurbas.centeredverticalviewpager.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucasurbas.centeredverticalviewpager.library.CenteredVerticalViewPager;

import java.util.ArrayList;


/**
 * Created by Lucas on 11/28/14.
 */
public class SampleFragment extends Fragment {

    CenteredVerticalViewPager vpFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        vpFeed = (CenteredVerticalViewPager) rootView.findViewById(R.id.vpFeed);
        vpFeed.setPageMarginDrawable(android.R.color.black);
        vpFeed.setPageMargin(50);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        com.lucasurbas.centeredverticalviewpager.sample.SampleAdapter adapter = new SampleAdapter(getActivity(), generateItems());
        vpFeed.setAdapter(adapter);
    }

    private ArrayList<String> generateItems() {

        ArrayList<String> items = new ArrayList<String>();
        for(int i = 0; i < 10; i++){
            items.add("Item " + i);
        }
        return items;
    }
}
