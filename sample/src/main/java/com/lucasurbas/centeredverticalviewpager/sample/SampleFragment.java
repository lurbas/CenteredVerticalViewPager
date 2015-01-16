package com.lucasurbas.centeredverticalviewpager.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lucasurbas.centeredverticalviewpager.library.CenteredVerticalViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Lucas on 11/28/14.
 */
public class SampleFragment extends Fragment {

    private CenteredVerticalViewPager vpFeed;
    private SwipeRefreshLayout srlRefresh;

    private SampleAdapter adapter;
    private List<String> items;

    private int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        vpFeed = (CenteredVerticalViewPager) rootView.findViewById(R.id.vpFeed);
        srlRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.srlRefresh);
        //vpFeed.setPageMarginDrawable(android.R.color.black);
        vpFeed.setPageMargin(Utils.dpToPixels(getActivity(), 8));
        vpFeed.setPagePreviewHeight(Utils.dpToPixels(getActivity(), 96));

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srlRefresh.setRefreshing(false);

                        items = generateItems();
                        adapter.setItems(items);
                        adapter.notifyItemRangeChanged(0, items.size());
                    }
                }, 2000);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (items == null) {
            items = generateItems();
        }
        adapter = new SampleAdapter(getActivity(), items);
        vpFeed.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int position = 1;

        switch (menuItem.getItemId()) {
            case R.id.action_insert_item:
                String item = generateItem();
                items.add(position, item);
                adapter.setItems(items);
                adapter.notifyItemRangeInserted(position, 1);
                return true;

            case R.id.action_remove_item:
                items.remove(position);
                adapter.setItems(items);
                adapter.notifyItemRangeRemoved(position, 1);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private String generateItem() {
        String item = "Added Item " + i;
        i++;
        return item;
    }

    private List<String> generateItems() {

        Random r = new Random();

        List<String> items = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            String text = "";
            for (int j = 0; j < 1 + r.nextInt(7); j++) {
                text += ("Item " + i + "\n");
            }
            items.add(text.substring(0, text.length() - 1));
        }
        return items;
    }
}
