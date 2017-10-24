package example.com.beijingnews.Listener;

import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import example.com.beijingnews.base.BasePager;

/**
 * Created by Administrator on 2017/10/24.
 */

public class ContentOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private final ArrayList<BasePager> basePagers;

    public ContentOnPageChangeListener(ArrayList<BasePager> basePagers) {
        if (basePagers != null){
            this.basePagers = basePagers;
        }else{
            this.basePagers = new ArrayList<>();
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        basePagers.get(position).initData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
