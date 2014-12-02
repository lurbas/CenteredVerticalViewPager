package com.lucasurbas.centeredverticalviewpager.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucasurbas.centeredverticalviewpager.library.CenteredPagerAdapter;
import com.lucasurbas.centeredverticalviewpager.library.ICenteredItem;

import java.util.ArrayList;

/**
 * Created by Lucas on 11/28/14.
 */
public class SampleAdapter extends CenteredPagerAdapter {
    // Declare Variables
    private Context context;
    private ArrayList<String> items;
    private LayoutInflater inflater;

    public SampleAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public ICenteredItem instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        TextView tvContent;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_page, container,
                false);

        // Locate the TextViews in item_page.xml
        tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        tvContent.setText(items.get(position));

        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return (ICenteredItem)itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove item_page.xml from ViewPager
        container.removeView((View) object);
    }
}
