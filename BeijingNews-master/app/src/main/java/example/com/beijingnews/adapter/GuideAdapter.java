package example.com.beijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/24.
 */

public class GuideAdapter extends PagerAdapter {
    private final ArrayList<ImageView> imageViews;

    public GuideAdapter(ArrayList<ImageView> imageViews) {
        if (imageViews != null) {
            this.imageViews = imageViews;
        }else {
            this.imageViews = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

