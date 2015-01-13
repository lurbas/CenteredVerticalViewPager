package com.lucasurbas.centeredverticalviewpager.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucasurbas.centeredverticalviewpager.library.CenteredPagerAdapter;

import java.util.List;

/**
 * Created by Lucas on 11/28/14.
 */
public class SampleAdapter extends CenteredPagerAdapter {
    // Declare Variables
    private Context context;
    private List<String> items;
    private LayoutInflater inflater;

    public SampleAdapter(Context context, List<String> items) {
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
    public void updatePosition(Object object, int position) {

        // Update view,
        // this will prevent from recreation all children views after calling notifyItmRangeChanged()
        if (object instanceof View) {
            updateView((View) object, position);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_page, container,
                false);

        // Update view
        updateView(itemView, position);

        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return itemView;
    }

    private void updateView(View view, int position) {
        // Declare Variables
        TextView tvContent;

        // Locate the TextViews in item_page.xml
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvContent.setText(items.get(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove item_page.xml from ViewPager
        container.removeView((View) object);
    }

    public void setItems(List<String> items){
        this.items = items;
    }
}
